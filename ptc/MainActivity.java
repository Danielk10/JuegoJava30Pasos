package com.diamon.ptc;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.documentfile.provider.DocumentFile;

import com.diamon.ptc.databinding.ActivityMainBinding;
import com.diamon.ptc.policy.PolicyActivity;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "CPicPrefs";
    private static final String KEY_EXPORT_URI = "export_uri";
    private static final String KEY_ASM_COUNTER = "asm_counter";
    private static final String KEY_C_COUNTER = "c_counter";
    private static final String KEY_SELECTED_LANGUAGE = "selected_language";
    private static final String KEY_SELECTED_PIC = "selected_pic";

    private static final String DEFAULT_ASM = "; Código de prueba para PIC16F628A\n" +
            "    PROCESSOR 16F628A\n" +
            "    INCLUDE \"P16F628A.INC\"\n\n" +
            "    ORG 0x00\n" +
            "START:\n" +
            "    BANKSEL TRISB\n" +
            "    CLRF TRISB\n" +
            "LOOP:\n" +
            "    MOVLW 0xFF\n" +
            "    MOVWF PORTB\n" +
            "    GOTO LOOP\n" +
            "    END\n";

    private static final String DEFAULT_C = "#include <pic14/pic16f628a.h>\n\n" +
            "// Ejemplo básico para PIC16F628A\n" +
            "void main(void) {\n" +
            "    TRISB = 0x00; // Puerto B como salida\n" +
            "    while(1) {\n" +
            "        PORTB = 0xFF;\n" +
            "        for(unsigned int i=0; i<1000; i++); // Retardo\n" +
            "        PORTB = 0x00;\n" +
            "        for(unsigned int i=0; i<1000; i++);\n" +
            "    }\n" +
            "}\n";

    private static final Pattern C_PATTERN = Pattern.compile("\\b(void|int|char|unsigned|if|else|while|for|return|static|const|struct|switch|case|break|volatile|typedef|enum|union|signed|long|short|float|double|sizeof|do|goto|extern|register|continue|default|auto|inline|restrict|_Bool|_Complex|_Imaginary)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern C_PREPROCESSOR_PATTERN = Pattern.compile("(?m)^\\s*#\\s*(include|define|ifdef|ifndef|if|elif|else|endif|pragma|error|warning|undef)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern C_COMMENT_PATTERN = Pattern.compile("(?s)/\\*.*?\\*/|//[^\\n\\r]*");
    private static final Pattern C_STRING_PATTERN = Pattern.compile("\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'");
    private static final Pattern C_NUMBER_PATTERN = Pattern.compile("\\b(0x[0-9a-fA-F]+|0b[01]+|0[0-7]+|\\d+(?:\\.\\d+)?)\\b");
    private static final Pattern C_FUNCTION_PATTERN = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*(?=\\()", Pattern.MULTILINE);

    private static final Pattern ASM_PATTERN = Pattern.compile("\\b(PROCESSOR|INCLUDE|ORG|END|MOVLW|MOVWF|GOTO|CALL|CLRF|BSF|BCF|BANKSEL|EQU|CONFIG|__CONFIG|TRIS[A-E]?|PORT[A-E]?|BTFSC|BTFSS|INCF|DECF|DECFSZ|RLF|RRF|NOP|RETLW|MOVF|ADDWF|SUBWF|ANDWF|IORWF|XORWF|COMF|SWAPF|RLF|RRF|BC|BZ|BNZ|BRA|RETURN|RETFIE|CLRW|CLRWDT|SLEEP|ADDLW|SUBLW|ANDLW|IORLW|XORLW|RETLW|DT|DB|DW|RES|SETC|CLRC|SKPNC|SKPC|SKPNZ|SKPZ)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern ASM_COMMENT_PATTERN = Pattern.compile(";.*$", Pattern.MULTILINE);
    private static final Pattern ASM_LABEL_PATTERN = Pattern.compile("(?m)^\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*:");
    private static final Pattern ASM_NUMBER_PATTERN = Pattern.compile("\\b(0x[0-9a-fA-F]+|0b[01]+|[0-9]+[Hh]|[0-9]+)\\b");
    private static final Pattern ASM_DIRECTIVE_PATTERN = Pattern.compile("(?m)^\\s*(#[a-zA-Z_][a-zA-Z0-9_]*|__[a-zA-Z_][a-zA-Z0-9_]*|\\.[a-zA-Z_][a-zA-Z0-9_]*)\\b");
    private static final Pattern ASM_STRING_PATTERN = Pattern.compile("\"(?:\\\\.|[^\"\\\\])*\"");

    private static class ModuleState {
        final LinkedHashMap<String, String> files = new LinkedHashMap<>();
        String activeFile;
        String currentProjectName; // Nombre efectivo actual (visual y lógico)
        String genericBaseName;    // Nombre genérico base (ej: asm_project1) reservado
    }

    private ActivityMainBinding binding;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private GpUtilsExecutor gpUtils;
    private SdccExecutor sdcc;
    private ActivityResultLauncher<Intent> folderPickerLauncher;
    private ActivityResultLauncher<Intent> sourceFilePickerLauncher;

    private final ModuleState asmState = new ModuleState();
    private final ModuleState cState = new ModuleState();
    private boolean isApplyingHighlight;
    private boolean isUpdatingProjectName; // Flag para evitar bucles infinitos en el TextWatcher
    private boolean currentModeIsC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        gpUtils = new GpUtilsExecutor(this);
        sdcc = new SdccExecutor(this);

        initInitialGenericNames();
        initModuleStates();
        
        currentModeIsC = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getBoolean(KEY_SELECTED_LANGUAGE, false);
        binding.toggleLanguage.check(currentModeIsC ? binding.btnLangC.getId() : binding.btnLangAsm.getId());
        
        setupFolderPicker();
        setupSourceFilePicker();
        setupListeners();
        setupLogCopySupport();
        renderCurrentModule();
        initResources();
    }

    private void initInitialGenericNames() {
        // Calcular nombres genéricos al inicio. Se incrementan para que sean únicos.
        int nextAsm = resolveNextProjectIndex("asm_project", KEY_ASM_COUNTER);
        int nextC = resolveNextProjectIndex("c_project", KEY_C_COUNTER);

        // Guardamos el nuevo contador para la próxima vez
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                .putInt(KEY_ASM_COUNTER, nextAsm)
                .putInt(KEY_C_COUNTER, nextC)
                .apply();

        asmState.genericBaseName = "asm_project" + nextAsm;
        asmState.currentProjectName = asmState.genericBaseName;

        cState.genericBaseName = "c_project" + nextC;
        cState.currentProjectName = cState.genericBaseName;
    }

    private void initModuleStates() {
        // Iniciamos con el archivo principal usando el nombre genérico calculado
        String asmFile = asmState.genericBaseName + ".asm";
        asmState.files.put(asmFile, DEFAULT_ASM);
        asmState.activeFile = asmFile;

        String cFile = cState.genericBaseName + ".c";
        cState.files.put(cFile, DEFAULT_C);
        cState.activeFile = cFile;
    }
    
    private void setupLogCopySupport() {
        binding.textLogs.setTextIsSelectable(true);
        binding.textLogs.setLongClickable(true);
        binding.textLogs.setOnLongClickListener(v -> {
            String logs = binding.textLogs.getText() == null ? "" : binding.textLogs.getText().toString();
            if (logs.trim().isEmpty()) {
                Toast.makeText(this, "No hay texto en el log para copiar.", Toast.LENGTH_SHORT).show();
                return true;
            }

            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (clipboard == null) {
                Toast.makeText(this, "No se pudo acceder al portapapeles.", Toast.LENGTH_SHORT).show();
                return true;
            }

            clipboard.setPrimaryClip(ClipData.newPlainText("logs", logs));
            Toast.makeText(this, "Logs copiados al portapapeles.", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void setupFolderPicker() {
        folderPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        Uri uri = result.getData().getData();
                        saveExportUri(uri);
                        updateLogs("Carpeta de exportación actualizada: " + uri);
                        exportToSelectedFolder(uri);
                    }
                });
    }

    private void setupSourceFilePicker() {
        sourceFilePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                        return;
                    }
                    List<Uri> uris = new ArrayList<>();
                    Intent data = result.getData();
                    if (data.getData() != null) {
                        uris.add(data.getData());
                    }
                    if (data.getClipData() != null) {
                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                            Uri uri = data.getClipData().getItemAt(i).getUri();
                            if (uri != null) {
                                uris.add(uri);
                            }
                        }
                    }
                    importSourceFiles(uris);
                });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_logs) {
            binding.textLogs.setText("Logs reiniciados.");
            updateLogs("Sistema listo.");
            return true;
        } else if (id == R.id.action_clear_editor) {
            confirmClearEditor();
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        } else if (id == R.id.action_import_sources) {
            launchSourceFilePicker();
            return true;
        } else if (id == R.id.action_change_export_folder) {
            launchFolderPicker(true);
            return true;
        } else if (id == R.id.action_policy) {
            startActivity(new Intent(this, PolicyActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListeners() {
        binding.btnAssemble.setOnClickListener(v -> assembleCode());
        binding.btnViewHex.setOnClickListener(v -> viewGeneratedFile(".hex"));
        binding.btnExport.setOnClickListener(v -> exportFiles());
        
        // Listener para el nombre del proyecto (Sincronización en tiempo real)
        binding.editProjectName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            
            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdatingProjectName) return;
                
                String inputName = s.toString().trim();
                syncProjectNameAndRenameTab(inputName);
            }
        });

        binding.editAsm.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                if (isApplyingHighlight) return;
                saveActiveEditorContent();
                updateLineNumbers();
                applySyntaxHighlighting();
            }
        });

        binding.toggleLanguage.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            boolean targetIsC = checkedId == binding.btnLangC.getId();
            if (targetIsC == currentModeIsC) return;

            saveActiveEditorContentForMode(currentModeIsC);
            currentModeIsC = targetIsC;
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putBoolean(KEY_SELECTED_LANGUAGE, currentModeIsC)
                    .apply();
            renderCurrentModule();
            updateLogs(isCurrentCMode() ? "Modo C (SDCC)" : "Modo ASM (GPUTILS)");
        });
    }
    
    /**
     * Sincroniza el nombre del proyecto con la pestaña principal en tiempo real.
     */
    private void syncProjectNameAndRenameTab(String inputName) {
        ModuleState state = getCurrentState();
        String oldEffectiveName = state.currentProjectName;
        String newEffectiveName;
        
        // Si el usuario borra todo el texto, volvemos al nombre genérico (ej. asm_project1)
        if (inputName.isEmpty()) {
            newEffectiveName = state.genericBaseName;
        } else {
            // Si el usuario escribe algo, ese es el nuevo nombre
            newEffectiveName = normalizeProjectName(inputName, isCurrentCMode());
        }
        
        if (newEffectiveName.equals(oldEffectiveName)) return;

        // Intentar renombrar la pestaña que coincide con el nombre anterior
        // Esto asegura que la pestaña principal siempre tenga el nombre del proyecto
        String ext = isCurrentCMode() ? ".c" : ".asm";
        String oldFileName = oldEffectiveName + ext;
        String newFileName = newEffectiveName + ext;

        if (state.files.containsKey(oldFileName)) {
            // Renombrar la clave en el mapa preservando el contenido
            String content = state.files.remove(oldFileName);
            
            // Colocamos el contenido en la nueva clave (nuevo nombre)
            if (content != null) {
                state.files.put(newFileName, content);
            }

            // Si el archivo activo era el que renombramos, actualizamos la referencia
            if (oldFileName.equals(state.activeFile)) {
                state.activeFile = newFileName;
            }
            
            // Actualizamos el nombre actual del proyecto en el estado
            state.currentProjectName = newEffectiveName;

            refreshTabs();
        }
    }

    private ModuleState getCurrentState() {
        return isCurrentCMode() ? cState : asmState;
    }

    private void renderCurrentModule() {
        isUpdatingProjectName = true; // Evitar disparar el TextWatcher
        ModuleState state = getCurrentState();
        
        binding.btnAssemble.setText(isCurrentCMode() ? "COMPILAR" : "ENSAMBLAR");
        binding.editAsm.setHint(isCurrentCMode()
                ? "// Escribe tu código C aquí..."
                : "; Escribe tu código ASM aquí...");

        // Configurar el EditText con el nombre del usuario.
        // Si el nombre actual es el genérico, dejamos el campo vacío (o con hint)
        // para indicar que no hay un nombre personalizado aún.
        if (state.currentProjectName.equals(state.genericBaseName)) {
            binding.editProjectName.setText("");
            binding.editProjectName.setHint(state.genericBaseName);
        } else {
            binding.editProjectName.setText(state.currentProjectName);
        }

        refreshTabs();
        loadActiveFileInEditor();
        isUpdatingProjectName = false;
    }

    private void refreshTabs() {
        ModuleState state = getCurrentState();
        binding.layoutTabs.removeAllViews();
        for (String fileName : state.files.keySet()) {
            android.widget.LinearLayout tabContainer = new android.widget.LinearLayout(this);
            tabContainer.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            tabContainer.setPadding(0, 0, 12, 0);

            MaterialButton tab = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
            tab.setText(fileName);
            tab.setTextSize(11f);
            tab.setAllCaps(false);
            tab.setPadding(16, 8, 16, 8);
            if (fileName.equals(state.activeFile)) {
                tab.setStrokeWidth(3);
            }
            tab.setOnClickListener(v -> {
                saveActiveEditorContent();
                state.activeFile = fileName;
                refreshTabs();
                loadActiveFileInEditor();
            });

            MaterialButton closeBtn = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
            closeBtn.setText("✕");
            closeBtn.setTextSize(11f);
            closeBtn.setAllCaps(false);
            closeBtn.setMinWidth(0);
            closeBtn.setMinimumWidth(0);
            closeBtn.setPadding(12, 8, 12, 8);
            closeBtn.setOnClickListener(v -> confirmCloseTab(fileName));

            tabContainer.addView(tab);
            tabContainer.addView(closeBtn);
            binding.layoutTabs.addView(tabContainer);
        }

        MaterialButton addTab = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
        addTab.setText("+");
        addTab.setTextSize(14f);
        addTab.setAllCaps(false);
        addTab.setMinWidth(0);
        addTab.setMinimumWidth(0);
        addTab.setPadding(18, 8, 18, 8);
        addTab.setOnClickListener(v -> showAddFileDialog());
        binding.layoutTabs.addView(addTab);
    }

    private void confirmCloseTab(String fileName) {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar archivo")
                .setMessage("¿Eliminar la pestaña '" + fileName + "'?")
                .setPositiveButton("Cerrar", (d, w) -> closeFileTab(fileName))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void closeFileTab(String fileName) {
        ModuleState state = getCurrentState();
        removeSourceFromProjectIfExists(fileName);
        state.files.remove(fileName);

        if (state.files.isEmpty()) {
            // Si se cierra todo, se crea un nuevo archivo con el nombre ACTUAL del proyecto.
            // Esto respeta si el usuario puso un nombre o si está usando el genérico.
            String ext = isCurrentCMode() ? ".c" : ".asm";
            String defaultName = state.currentProjectName + ext;
            
            state.files.put(defaultName, "");
            state.activeFile = defaultName;
            updateLogs("Se creó una pestaña por defecto: " + defaultName);
        } else if (fileName.equals(state.activeFile)) {
            state.activeFile = state.files.keySet().iterator().next();
        }

        refreshTabs();
        loadActiveFileInEditor();
    }

    private void removeSourceFromProjectIfExists(String fileName) {
        String projectName = resolveProjectName(); // Obtener nombre actual
        if (projectName == null) {
            return;
        }

        File file = new File(getProjectDir(projectName), fileName);
        if (file.exists()) {
            if (file.delete()) {
                updateLogs("Archivo desligado del proyecto al cerrar pestaña: " + fileName);
            }
        }
    }

    private void showAddFileDialog() {
        final EditText input = new EditText(this);
        input.setHint(isCurrentCMode() ? "ej: archivo.c, utils.c, defs.h" : "ej: archivo.asm, macros.inc");
        input.setTextColor(0xFF121212);
        input.setHintTextColor(0xFF5F6368);
        input.setBackgroundResource(android.R.drawable.editbox_background_normal);
        int horizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, getResources().getDisplayMetrics());
        int verticalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, getResources().getDisplayMetrics());
        input.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        input.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        input.setSingleLine();
        new AlertDialog.Builder(this)
                .setTitle("Nuevo archivo")
                .setView(input)
                .setPositiveButton("Agregar", (d, w) -> {
                    String rawName = input.getText().toString().trim();
                    if (rawName.isEmpty()) {
                        updateLogs("Nombre de archivo inválido.");
                        return;
                    }

                    String name = applyDefaultExtensionIfMissing(rawName);
                    
                    if (!isValidExtensionForCurrentMode(name)) {
                        updateLogs(isCurrentCMode()
                                ? "En modo C solo se permiten .c y .h"
                                : "En modo ASM solo se permiten .asm y .inc");
                        return;
                    }

                    ModuleState state = getCurrentState();
                    if (state.files.containsKey(name)) {
                        updateLogs("Ese archivo ya existe en el proyecto.");
                        return;
                    }

                    state.files.put(name, "");
                    state.activeFile = name;
                    refreshTabs();
                    loadActiveFileInEditor();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private String applyDefaultExtensionIfMissing(String fileName) {
        String normalized = fileName.trim();
        int dot = normalized.lastIndexOf('.');
        boolean hasExplicitExtension = dot > 0 && dot < normalized.length() - 1;
        if (hasExplicitExtension) {
            return normalized;
        }

        while (normalized.endsWith(".")) {
            normalized = normalized.substring(0, normalized.length() - 1).trim();
        }

        if (normalized.isEmpty()) {
            return fileName.trim();
        }

        return normalized + (isCurrentCMode() ? ".c" : ".asm");
    }

    private boolean isValidExtensionForCurrentMode(String name) {
        if (isCurrentCMode()) {
            return isCSourceFile(name);
        }
        return isAsmSourceFile(name);
    }

    private boolean isCSourceFile(String name) {
        String lower = name.toLowerCase(Locale.US);
        return lower.endsWith(".c") || lower.endsWith(".h");
    }

    private boolean isAsmSourceFile(String name) {
        String lower = name.toLowerCase(Locale.US);
        return lower.endsWith(".asm") || lower.endsWith(".inc");
    }

    private void launchSourceFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        sourceFilePickerLauncher.launch(intent);
    }

    private void importSourceFiles(List<Uri> uris) {
        if (uris == null || uris.isEmpty()) {
            updateLogs("No se seleccionaron archivos para importar.");
            return;
        }

        int importedToC = 0;
        int importedToAsm = 0;
        String lastImportedInCurrent = null;
        boolean importedInCurrentModule = false;
        boolean importedInOtherModule = false;
        ModuleState currentState = getCurrentState();

        for (Uri uri : uris) {
            String name = resolveDisplayName(uri);
            if (name == null || name.trim().isEmpty()) {
                updateLogs("No se pudo identificar nombre de archivo: " + uri);
                continue;
            }

            boolean isCFile = isCSourceFile(name);
            boolean isAsmFile = isAsmSourceFile(name);
            if (!isCFile && !isAsmFile) {
                updateLogs("Archivo omitido por extensión no válida (.c/.h/.asm/.inc): " + name);
                continue;
            }

            ModuleState targetState = isCFile ? cState : asmState;
            String content = readTextFromUri(uri);
            String uniqueName = makeUniqueFileName(targetState, name);
            targetState.files.put(uniqueName, content);
            targetState.activeFile = uniqueName;

            if (targetState == currentState) {
                lastImportedInCurrent = uniqueName;
                importedInCurrentModule = true;
            } else {
                importedInOtherModule = true;
            }

            if (isCFile) {
                importedToC++;
                updateLogs("Archivo importado en módulo C: " + uniqueName);
            } else {
                importedToAsm++;
                updateLogs("Archivo importado en módulo ASM: " + uniqueName);
            }
        }

        int totalImported = importedToC + importedToAsm;
        if (totalImported > 0) {
            if (!importedInCurrentModule && importedInOtherModule) {
                if (importedToC > 0 && importedToAsm == 0 && !isCurrentCMode()) {
                    currentModeIsC = true;
                    binding.toggleLanguage.check(binding.btnLangC.getId());
                } else if (importedToAsm > 0 && importedToC == 0 && isCurrentCMode()) {
                    currentModeIsC = false;
                    binding.toggleLanguage.check(binding.btnLangAsm.getId());
                }
            }
            refreshTabs();
            loadActiveFileInEditor();
            updateLogs("> ¡Importación exitosa! " + totalImported + " archivo(s).");
        } else {
            updateLogs("No se importó ningún archivo válido.");
        }
    }

    private String resolveDisplayName(Uri uri) {
        String fallback = uri.getLastPathSegment();
        try (android.database.Cursor cursor = getContentResolver().query(uri,
                new String[]{android.provider.OpenableColumns.DISPLAY_NAME},
                null,
                null,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) {
                    String name = cursor.getString(idx);
                    if (name != null && !name.trim().isEmpty()) return name.trim();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "No se pudo leer nombre del archivo", e);
        }
        return fallback == null ? null : fallback.trim();
    }

    private String readTextFromUri(Uri uri) {
        StringBuilder builder = new StringBuilder();
        try (InputStream in = getContentResolver().openInputStream(uri);
             BufferedReader reader = in == null ? null : new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            if (reader == null) {
                return "";
            }
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (Exception e) {
            Log.e(TAG, "No se pudo leer archivo importado", e);
            updateLogs("No se pudo leer: " + uri);
        }
        return builder.toString();
    }

    private String makeUniqueFileName(ModuleState state, String originalName) {
        if (!state.files.containsKey(originalName)) {
            return originalName;
        }
        String base = originalName;
        String ext = "";
        int dot = originalName.lastIndexOf('.');
        if (dot > 0) {
            base = originalName.substring(0, dot);
            ext = originalName.substring(dot);
        }
        int i = 1;
        while (state.files.containsKey(base + "_" + i + ext)) {
            i++;
        }
        return base + "_" + i + ext;
    }

    private void confirmClearEditor() {
        new AlertDialog.Builder(this)
                .setTitle("Limpiar Editor")
                .setMessage("¿Borrar el contenido del archivo activo?")
                .setPositiveButton("Sí", (d, w) -> {
                    binding.editAsm.setText("");
                    saveActiveEditorContent();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void showAboutDialog() {
        String message = "<b>C PIC Compiler</b><br><br>" +
                "Esta aplicación es una interfaz gráfica (GUI) profesional para las herramientas GPUTILS y SDCC.<br><br>" +
                "<b>GPUTILS:</b><br>" +
                "Colección de herramientas de código abierto para microcontroladores Microchip PIC.<br><br>" +
                "Sitio web: <a href='https://sourceforge.net/projects/gputils/'>sourceforge.net/projects/gputils/</a><br><br>" +
                "<b>SDCC (Small Device C Compiler):</b><br>" +
                "Compilador de C para microcontroladores de 8 bits.<br><br>" +
                "Sitio web: <a href='https://sourceforge.net/projects/sdcc/'>sourceforge.net/projects/sdcc/</a><br><br>" +
                "<b>Licencia del Proyecto:</b><br>" +
                "C PIC Compiler es software libre y está bajo la licencia GNU GPL v3.0.<br><br>" +
                "Los binarios incluidos de GPUTILS y SDCC también se distribuyen bajo sus propias licencias GPL.";

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Acerca de / Licencias")
                .setMessage(HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_COMPACT))
                .setPositiveButton("Cerrar", null)
                .show();

        TextView textView = dialog.findViewById(android.R.id.message);
        if (textView != null) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void saveActiveEditorContent() {
        saveActiveEditorContentForMode(currentModeIsC);
    }

    private void saveActiveEditorContentForMode(boolean isCMode) {
        ModuleState state = isCMode ? cState : asmState;
        if (state.activeFile != null) {
            state.files.put(state.activeFile, binding.editAsm.getText().toString());
        }
    }

    private void loadActiveFileInEditor() {
        ModuleState state = getCurrentState();
        String content = state.files.getOrDefault(state.activeFile, "");
        binding.editAsm.setText(content);
        binding.editAsm.setSelection(binding.editAsm.getText().length());
        applySyntaxHighlighting();
        updateLineNumbers();
    }

    private void updateLineNumbers() {
        int lines = Math.max(1, binding.editAsm.getLineCount());
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lines; i++) {
            sb.append(i);
            if (i < lines) {
                sb.append('\n');
            }
        }
        binding.textLineNumbers.setText(sb.toString());
    }

    private void applySyntaxHighlighting() {
        Editable editable = binding.editAsm.getText();
        if (editable == null) return;

        isApplyingHighlight = true;
        try {
            int start = binding.editAsm.getSelectionStart();
            int end = binding.editAsm.getSelectionEnd();

            SpannableStringBuilder spannable = new SpannableStringBuilder(editable);
            ForegroundColorSpan[] old = spannable.getSpans(0, spannable.length(), ForegroundColorSpan.class);
            for (ForegroundColorSpan span : old) {
                spannable.removeSpan(span);
            }

            @ColorInt int keywordColor = 0xFF80CBC4;
            @ColorInt int preprocessorColor = 0xFFBA68C8;
            @ColorInt int numberColor = 0xFFFFB74D;
            @ColorInt int stringColor = 0xFFE6EE9C;
            @ColorInt int symbolColor = 0xFF64B5F6;
            @ColorInt int commentColor = 0xFF7F8C8D;

            String source = spannable.toString();
            if (isCurrentCMode()) {
                applyPatternColor(spannable, source, C_PATTERN, keywordColor);
                applyPatternColor(spannable, source, C_PREPROCESSOR_PATTERN, preprocessorColor);
                applyPatternColor(spannable, source, C_NUMBER_PATTERN, numberColor);
                applyPatternColor(spannable, source, C_STRING_PATTERN, stringColor);
                applyPatternColor(spannable, source, C_FUNCTION_PATTERN, symbolColor);
                applyPatternColor(spannable, source, C_COMMENT_PATTERN, commentColor);
            } else {
                applyPatternColor(spannable, source, ASM_PATTERN, keywordColor);
                applyPatternColor(spannable, source, ASM_DIRECTIVE_PATTERN, preprocessorColor);
                applyPatternColor(spannable, source, ASM_LABEL_PATTERN, symbolColor);
                applyPatternColor(spannable, source, ASM_NUMBER_PATTERN, numberColor);
                applyPatternColor(spannable, source, ASM_STRING_PATTERN, stringColor);
                applyPatternColor(spannable, source, ASM_COMMENT_PATTERN, commentColor);
            }

            editable.replace(0, editable.length(), spannable);
            int safeStart = Math.max(0, Math.min(start, binding.editAsm.length()));
            int safeEnd = Math.max(0, Math.min(end, binding.editAsm.length()));
            binding.editAsm.setSelection(safeStart, safeEnd);
        } finally {
            isApplyingHighlight = false;
        }
    }

    private void applyPatternColor(SpannableStringBuilder spannable, String source, Pattern pattern, @ColorInt int color) {
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            spannable.setSpan(new ForegroundColorSpan(color), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private boolean isCurrentCMode() {
        return currentModeIsC;
    }

    private void initResources() {
        executor.execute(() -> {
            boolean extracted = AssetExtractor.areAssetsExtracted(this);
            if (!extracted) {
                mainHandler.post(() -> binding.loadingOverlay.setVisibility(View.VISIBLE));
                updateLogs("Preparando recursos (GPUTILS + SDCC)...");
                boolean success = AssetExtractor.extractAssets(this, "data/data/com.diamon.ptc/files/usr", new File(getFilesDir(), "usr"));
                mainHandler.post(() -> binding.loadingOverlay.setVisibility(View.GONE));
                updateLogs(success ? "Recursos extraídos correctamente." : "Error al extraer recursos.");
            }
            loadPicList();
        });
    }

    private void loadPicList() {
        File headerDir = new File(getFilesDir(), "usr/share/gputils/header");
        List<String> pics = new ArrayList<>();
        String[] files = headerDir.list();
        if (files != null) {
            for (String file : files) {
                if (file.toLowerCase(Locale.US).endsWith(".inc")) {
                    String name = file.substring(0, file.length() - 4);
                    if (name.toLowerCase(Locale.US).startsWith("p")) name = name.substring(1);
                    pics.add(name.toUpperCase(Locale.US));
                }
            }
        }

        Collections.sort(pics);
        if (pics.isEmpty()) pics.add("16F628A");

        mainHandler.post(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pics);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerPic.setAdapter(adapter);

            String savedPic = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(KEY_SELECTED_PIC, null);
            int index = savedPic == null ? -1 : pics.indexOf(savedPic);
            if (index < 0) index = pics.indexOf("16F628A");
            if (index < 0) index = pics.indexOf("16F84A");
            if (index >= 0) {
                binding.spinnerPic.setSelection(index);
            }

            binding.spinnerPic.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                    String selected = adapter.getItem(position);
                    if (selected == null) return;
                    getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                            .edit()
                            .putString(KEY_SELECTED_PIC, selected)
                            .apply();
                    binding.editAsm.post(() -> {
                        binding.editAsm.requestFocus();
                        binding.editAsm.setSelection(binding.editAsm.getText().length());
                    });
                }

                @Override
                public void onNothingSelected(android.widget.AdapterView<?> parent) { }
            });
        });
    }

    private void assembleCode() {
        saveActiveEditorContent();
        ModuleState state = getCurrentState();

        if (state.files.isEmpty()) {
            updateLogs("No hay archivos para compilar.");
            return;
        }

        if (!hasAtLeastOneNonEmptySource(state)) {
            updateLogs("No hay archivos de entrada válidos. Agrega código en al menos una pestaña.");
            return;
        }

        if (!runToolchainPreflightChecks(isCurrentCMode())) {
            updateLogs("Prechequeo de herramientas falló. Revisa los logs.");
            return;
        }

        String selectedPic = binding.spinnerPic.getSelectedItem() != null
                ? binding.spinnerPic.getSelectedItem().toString()
                : "16F628A";

        String projectName = resolveProjectName();

        File projectDir = getProjectDir(projectName);
        if (!projectDir.exists() && !projectDir.mkdirs()) {
            updateLogs("No se pudo crear directorio de proyecto.");
            return;
        }

        // Limpiar archivos anteriores para asegurar una compilación limpia
        cleanupProjectDir(projectDir);

        LinkedHashMap<String, String> snapshotFiles = new LinkedHashMap<>(state.files);
        for (String fileName : snapshotFiles.keySet()) {
            boolean saved = FileManager.writeToFile(new File(projectDir, fileName), snapshotFiles.get(fileName));
            if (!saved) {
                updateLogs("No se pudo guardar fuente: " + fileName);
                return;
            }
        }

        if (isCurrentCMode()) {
            compileCProject(projectDir, selectedPic, snapshotFiles, state.activeFile, projectName);
        } else {
            assembleAsmProject(projectDir, selectedPic, snapshotFiles, state.activeFile, projectName);
        }
    }

    private void cleanupProjectDir(File projectDir) {
        File[] files = projectDir.listFiles();
        if (files == null) return;
        for (File f : files) {
            String name = f.getName().toLowerCase(Locale.US);
            if (name.endsWith(".hex") || name.endsWith(".o") || name.endsWith(".rel") || name.endsWith(".ihx") || name.endsWith(".cod")) {
                f.delete();
            }
        }
    }

    private boolean hasAtLeastOneNonEmptySource(ModuleState state) {
        for (String content : state.files.values()) {
            if (content != null && !content.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean runToolchainPreflightChecks(boolean forCModule) {
        List<String> issues = forCModule ? sdcc.getSetupIssues() : gpUtils.getSetupIssues();
        if (issues.isEmpty()) {
            updateLogs("Prechequeo " + (forCModule ? "SDCC" : "GPUTILS") + " OK.");
            return true;
        }
        for (String issue : issues) {
            updateLogs("Prechequeo: " + issue);
        }
        return false;
    }

    private void assembleAsmProject(File projectDir, String selectedPic, LinkedHashMap<String, String> snapshotFiles,
                                    String activeFileName, String projectName) {
        executor.execute(() -> {
            List<String> asmFiles = collectSourceFiles(snapshotFiles, ".asm");
            if (asmFiles.isEmpty()) {
                updateLogs("Agrega al menos un archivo .asm para ensamblar.");
                return;
            }

            // Si el archivo activo es asm, lo priorizamos como punto de entrada.
            // Si no, buscamos uno que coincida con el nombre del proyecto.
            String projectMain = projectName + ".asm";
            String preferredEntry = (activeFileName != null && activeFileName.endsWith(".asm")) 
                                    ? activeFileName 
                                    : projectMain;
                                    
            prioritizeMainSource(asmFiles, preferredEntry);

            // El nombre base de salida será el del nombre del proyecto
            String outputBaseName = projectName;

            List<String> objectFiles = new ArrayList<>();
            String compilePrefix = "Compilando ASM";
            for (String asmFile : asmFiles) {
                if (!new File(projectDir, asmFile).exists()) {
                    updateLogs("No se encontró el archivo fuente: " + asmFile);
                    return;
                }

                updateLogs(compilePrefix + " " + asmFile + "...");
                String asmResult = gpUtils.executeGpasm(projectDir,
                        "-c",
                        "-I", projectDir.getAbsolutePath(),
                        "-p", selectedPic.toLowerCase(Locale.US),
                        asmFile);
                updateLogs("Log GPASM para " + asmFile + ":\n" + asmResult);
                if (didCommandFail(asmResult)) {
                    updateLogs("Error en " + asmFile + ". Se cancela el enlace.");
                    return;
                }

                String objectName = getBaseName(asmFile) + ".o";
                File objectFile = new File(projectDir, objectName);
                if (!objectFile.exists()) {
                    updateLogs("No se generó el objeto esperado: " + objectName);
                    return;
                }
                objectFiles.add(objectName);
            }

            if (objectFiles.isEmpty()) {
                updateLogs("No hay objetos ASM para enlazar.");
                return;
            }

            List<String> linkArgs = new ArrayList<>();
            linkArgs.add("-o");
            linkArgs.add(outputBaseName + ".hex");
            linkArgs.addAll(objectFiles);

            updateLogs("Enlazando ASM del proyecto...");
            String linkResult = gpUtils.executeBinary(projectDir, "gplink", linkArgs.toArray(new String[0]));
            updateLogs("Log GPLINK completo:\n" + linkResult);
            if (didCommandFail(linkResult)) {
                updateLogs("Falló el enlace ASM. No se generó HEX.");
                return;
            }

            checkGenerationSuccess(projectDir, ".hex", false);
        });
    }

    private void compileCProject(File projectDir, String selectedPic, LinkedHashMap<String, String> snapshotFiles,
                                 String activeFileName, String projectName) {
        executor.execute(() -> {
            List<String> cFiles = collectSourceFiles(snapshotFiles, ".c");

            if (cFiles.isEmpty()) {
                updateLogs("Agrega al menos un archivo .c para compilar.");
                return;
            }

            String arch = selectedPic.toUpperCase(Locale.US).startsWith("18") ? "pic16" : "pic14";
            
            String projectMain = projectName + ".c";
            String preferredEntry = (activeFileName != null && activeFileName.endsWith(".c")) 
                                    ? activeFileName 
                                    : projectMain;
            prioritizeMainSource(cFiles, preferredEntry);

            String outputBaseName = projectName;
            
            List<String> objFiles = new ArrayList<>();
            for (String cFile : cFiles) {
                if (!new File(projectDir, cFile).exists()) {
                    updateLogs("No se encontró el archivo fuente: " + cFile);
                    return;
                }

                updateLogs("Compilando C " + cFile + " (Arch: " + arch + ")...");
                List<String> compileArgs = new ArrayList<>(Arrays.asList(
                        "-m" + arch,
                        "-p" + selectedPic.toLowerCase(Locale.US),
                        "--use-non-free",
                        "-c",
                        "-I" + projectDir.getAbsolutePath(),
                        cFile
                ));
                
                String compileResult = sdcc.executeSdcc(projectDir, compileArgs.toArray(new String[0]));
                updateLogs("Log SDCC para " + cFile + ":\n" + compileResult);
                
                if (didCommandFail(compileResult)) {
                    updateLogs("Error en " + cFile + ". Se detiene la compilación.");
                    return;
                }

                String baseName = getBaseName(cFile);
                File objFile = new File(projectDir, baseName + ".o");
                
                // Si no encontramos el .o, buscar .rel
                if (!objFile.exists()) {
                     File relFile = new File(projectDir, baseName + ".rel");
                     if (relFile.exists()) {
                         objFile = relFile;
                     }
                }

                if (!objFile.exists()) {
                    updateLogs("ERROR CRÍTICO: No se generó archivo objeto para " + cFile);
                    return;
                }
                
                objFiles.add(objFile.getName());
            }

            if (objFiles.isEmpty()) {
                updateLogs("No hay objetos C para enlazar.");
                return;
            }

            List<String> linkArgs = new ArrayList<>(Arrays.asList(
                    "-m" + arch,
                    "-p" + selectedPic.toLowerCase(Locale.US),
                    "--use-non-free",
                    "--out-fmt-ihx",
                    "-I" + projectDir.getAbsolutePath()));
            linkArgs.addAll(objFiles);
            linkArgs.add("-o");
            linkArgs.add(outputBaseName + ".hex");

            updateLogs("Enlazando proyecto C...");
            String result = sdcc.executeSdcc(projectDir, linkArgs.toArray(new String[0]));
            updateLogs("Log SDCC enlace:\n" + result);
            if (didCommandFail(result)) {
                updateLogs("Falló el enlace C. No se generó HEX.");
                return;
            }

            // Normalizar salidas
            normalizeCOutputArtifacts(projectDir, outputBaseName);
            checkGenerationSuccess(projectDir, ".hex", true);
        });
    }

    private List<String> collectSourceFiles(LinkedHashMap<String, String> snapshotFiles, String extension) {
        List<String> sources = new ArrayList<>();
        for (String file : snapshotFiles.keySet()) {
            if (file.toLowerCase(Locale.US).endsWith(extension) && !snapshotFiles.getOrDefault(file, "").trim().isEmpty()) {
                sources.add(file);
            }
        }
        return sources;
    }

    private void prioritizeMainSource(List<String> sourceFiles, String preferredFileName) {
        if (preferredFileName == null) {
            return;
        }
        for (int i = 0; i < sourceFiles.size(); i++) {
            if (sourceFiles.get(i).equalsIgnoreCase(preferredFileName)) {
                if (i > 0) {
                    String main = sourceFiles.remove(i);
                    sourceFiles.add(0, main);
                }
                return;
            }
        }
    }

    private String getBaseName(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot > 0 ? fileName.substring(0, dot) : fileName;
    }

    private boolean didCommandFail(String commandOutput) {
        Matcher matcher = Pattern.compile("Código de salida:\\s*(-?\\d+)").matcher(commandOutput == null ? "" : commandOutput);
        if (!matcher.find()) {
            return true;
        }
        try {
            return Integer.parseInt(matcher.group(1)) != 0;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private void normalizeCOutputArtifacts(File projectDir, String outputBaseName) {
        File hexTarget = new File(projectDir, outputBaseName + ".hex");
        
        // Buscar cualquier .ihx o .HEX que coincida con lo que SDCC pudo haber generado
        File[] files = projectDir.listFiles();
        if (files == null) return;

        for (File f : files) {
            String name = f.getName();
            if (name.equalsIgnoreCase(outputBaseName + ".ihx") || 
                name.equalsIgnoreCase(outputBaseName + ".HEX")) {
                
                if (!name.equals(hexTarget.getName())) {
                     if (hexTarget.exists()) hexTarget.delete();
                     f.renameTo(hexTarget);
                     updateLogs("Renombrado salida: " + name + " -> " + hexTarget.getName());
                }
            }
        }
    }

    private void checkGenerationSuccess(File projectDir, String extension, boolean isCModule) {
        File[] files = projectDir.listFiles();
        if (files == null) {
            updateLogs("No se encontró salida para " + extension);
            return;
        }

        List<String> generatedFiles = new ArrayList<>();
        File expectedFile = null;
        
        for (File file : files) {
            String lowerName = file.getName().toLowerCase(Locale.US);
            if (lowerName.endsWith(extension)) {
                 expectedFile = file;
            }
            if (lowerName.endsWith(".hex") || lowerName.endsWith(".ihx") || lowerName.endsWith(".cod") || lowerName.endsWith(".lst")
                    || lowerName.endsWith(".asm") || lowerName.endsWith(".c") || lowerName.endsWith(".inc")
                    || lowerName.endsWith(".h") || lowerName.endsWith(".rel") || lowerName.endsWith(".o")) {
                generatedFiles.add(file.getName());
            }
        }

        Collections.sort(generatedFiles);
        if (!generatedFiles.isEmpty()) {
            updateLogs("Archivos generados: " + TextUtils.join(", ", generatedFiles));
        }

        if (expectedFile != null) {
            updateLogs(isCModule ? "✓ Compilación exitosa." : "✓ Ensamblado exitoso.");
            updateLogs("> ¡Operación completada! Archivo generado: " + expectedFile.getName());
            return;
        }
        updateLogs("No se generó salida esperada (" + extension + "). Revisa logs de error.");
    }

    private File getProjectDir(String projectName) {
        return new File(new File(getFilesDir(), "projects"), projectName);
    }

    /**
     * Devuelve el nombre actual del proyecto. 
     * Si el usuario escribió algo, devuelve eso (sanitizado).
     * Si no, devuelve el genérico (ej. asm_project1).
     */
    private String resolveProjectName() {
        return getCurrentState().currentProjectName;
    }

    private String normalizeProjectName(String name, boolean isC) {
        String trimmed = name.trim().replaceAll("[^a-zA-Z0-9_]", "_");
        if (trimmed.isEmpty()) {
            // Esto no debería ocurrir gracias a la lógica del TextWatcher, pero por seguridad:
            return isC ? cState.genericBaseName : asmState.genericBaseName;
        }
        return trimmed;
    }

    private int resolveNextProjectIndex(String projectPrefix, String counterKey) {
        android.content.SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int max = prefs.getInt(counterKey, 0);
        File projectsDir = new File(getFilesDir(), "projects");
        File[] dirs = projectsDir.listFiles();
        if (dirs != null) {
            for (File dir : dirs) {
                if (!dir.isDirectory()) continue;
                String name = dir.getName();
                if (!name.startsWith(projectPrefix)) continue;
                String suffix = name.substring(projectPrefix.length());
                if (suffix.isEmpty()) continue;
                boolean allDigits = true;
                for (int i = 0; i < suffix.length(); i++) {
                    if (!Character.isDigit(suffix.charAt(i))) {
                        allDigits = false;
                        break;
                    }
                }
                if (!allDigits) continue;
                try {
                    int n = Integer.parseInt(suffix);
                    if (n > max) max = n;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return max + 1;
    }

    private void viewGeneratedFile(String extension) {
        String projectName = resolveProjectName();
        if (projectName == null) {
            updateLogs("Primero compila o ensambla un proyecto.");
            return;
        }

        File projectDir = getProjectDir(projectName);
        
        // Buscar primero el archivo que coincide con el proyecto
        File target = new File(projectDir, projectName + extension);
        if (!target.exists()) {
             // Si no existe con nombre exacto, buscar cualquiera con la extensión
             target = findFirstWithExtension(projectDir, extension);
        }

        if (target == null || !target.exists()) {
            updateLogs("No existe archivo " + extension + " para el proyecto actual.");
            return;
        }

        String content = FileManager.readFile(target);
        if (content.isEmpty()) {
            updateLogs("Archivo vacío: " + target.getName());
            return;
        }

        if (target.getName().toLowerCase(Locale.US).endsWith(".hex")) {
            showAdvancedHexViewer(content);
        } else {
            showSimpleTextViewer(target.getName(), content);
        }
    }

    private File findFirstWithExtension(File dir, String extension) {
        File[] files = dir.listFiles();
        if (files == null) return null;
        for (File file : files) {
            if (file.getName().toLowerCase(Locale.US).endsWith(extension)) {
                return file;
            }
        }
        return null;
    }

    private void showSimpleTextViewer(String title, String content) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("Cerrar", null)
                .show();
    }

    private void showAdvancedHexViewer(String content) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_hex_viewer, null);
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        ListView listView = popupView.findViewById(R.id.list_hex);
        List<String[]> rows = new ArrayList<>();
        TreeMap<Integer, Byte> memory = IntelHexParser.parse(content);

        if (!memory.isEmpty()) {
            List<Integer> addresses = new ArrayList<>(memory.keySet());
            Collections.sort(addresses);

            int currentStartAddr = addresses.get(0);
            List<Byte> currentBytes = new ArrayList<>();

            for (int addr : addresses) {
                byte val = memory.get(addr);
                if (currentBytes.size() == 8 || addr != currentStartAddr + currentBytes.size()) {
                    rows.add(formatHexRow(currentStartAddr, currentBytes));
                    currentStartAddr = addr;
                    currentBytes.clear();
                }
                currentBytes.add(val);
            }
            if (!currentBytes.isEmpty()) {
                rows.add(formatHexRow(currentStartAddr, currentBytes));
            }
        }

        listView.setAdapter(new BaseAdapter() {
            @Override public int getCount() { return rows.size(); }
            @Override public Object getItem(int position) { return rows.get(position); }
            @Override public long getItemId(int position) { return position; }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_hex_row, parent, false);
                }
                String[] data = rows.get(position);
                ((TextView) convertView.findViewById(R.id.tv_addr)).setText(data[0]);
                ((TextView) convertView.findViewById(R.id.tv_hex)).setText(data[1]);
                ((TextView) convertView.findViewById(R.id.tv_ansi)).setText(data[2]);
                return convertView;
            }
        });

        popupView.findViewById(R.id.btn_close_popup).setOnClickListener(v -> popupWindow.dismiss());
        popupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
    }

    private String[] formatHexRow(int startAddr, List<Byte> bytes) {
        StringBuilder hexStr = new StringBuilder();
        StringBuilder ansi = new StringBuilder();

        for (byte b : bytes) {
            hexStr.append(String.format(Locale.US, "%02X ", b));
            ansi.append((b >= 32 && b <= 126) ? (char) b : '.');
        }

        while (hexStr.length() < 24) {
            hexStr.append("   ");
        }

        return new String[] { String.format(Locale.US, "%04X:", startAddr), hexStr.toString().trim(), ansi.toString() };
    }

    private void exportFiles() {
        saveActiveEditorContent();
        Uri uri = getSavedExportUri();
        if (uri == null || !hasPersistedPermission(uri)) {
            launchFolderPicker(false);
        } else {
            exportToSelectedFolder(uri);
        }
    }

    private void launchFolderPicker(boolean forceChange) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        intent.putExtra("android.provider.extra.SHOW_ADVANCED", true);

        if (forceChange) {
            clearSavedExportUri();
        }

        Uri initialUri = resolveInitialFolderUri();
        if (initialUri != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri);
        }

        folderPickerLauncher.launch(intent);
    }

    private Uri resolveInitialFolderUri() {
        Uri saved = getSavedExportUri();
        if (saved != null) {
            return saved;
        }

        try {
            return DocumentsContract.buildTreeDocumentUri("com.android.externalstorage.documents", "primary:Download");
        } catch (Exception ignore) {
            return null;
        }
    }

    private void exportToSelectedFolder(Uri treeUri) {
        String projectName = resolveProjectName();
        // Nota: Ya no forzamos rename aquí, confiamos en la sincronización en tiempo real.
        
        refreshTabs();
        loadActiveFileInEditor();

        File projectDir = getProjectDir(projectName);
        persistCurrentModuleSources(projectDir);

        executor.execute(() -> {
            List<File> filesToExport = collectProjectFilesForExport(projectDir, projectName);
            if (filesToExport.isEmpty()) {
                updateLogs("No hay archivos en el proyecto para exportar.");
                return;
            }

            int count = 0;
            for (File file : filesToExport) {
                String exportName = file.getName();
                if (saveFileToDocumentTree(treeUri, exportName, file)) {
                    count++;
                }
            }

            int finalCount = count;
            updateLogs(finalCount > 0
                    ? "> ¡Exportación exitosa! " + finalCount + " archivos guardados."
                    : "No fue posible exportar archivos.");
        });
    }

    private List<File> collectProjectFilesForExport(File projectDir, String projectName) {
        List<File> files = new ArrayList<>();
        ModuleState state = getCurrentState();

        // 1. Exportar fuentes actuales (ya tienen los nombres correctos)
        for (String sourceName : state.files.keySet()) {
            File source = new File(projectDir, sourceName);
            if (source.exists() && source.isFile()) {
                files.add(source);
            }
        }

        // 2. Exportar artefactos generados (hex, lst, etc.) que coincidan con el nombre del proyecto
        // o con los archivos fuente
        File[] projectEntries = projectDir.listFiles();
        if (projectEntries != null) {
            for (File file : projectEntries) {
                if (!file.isFile()) continue;
                String name = file.getName();
                
                // Si el archivo ya fue añadido (es fuente), saltar
                boolean alreadyAdded = false;
                for (File existing : files) {
                    if (existing.getName().equals(name)) {
                        alreadyAdded = true;
                        break;
                    }
                }
                if (alreadyAdded) continue;

                // Criterio de inclusión para artefactos:
                // Coincide con nombre del proyecto O coincide con nombre de algún archivo fuente (sin ext)
                if (name.startsWith(projectName + ".")) {
                    files.add(file);
                    continue;
                }
                
                // Chequear si corresponde a un fuente secundario (ej: utils.asm -> utils.o)
                for (String sourceName : state.files.keySet()) {
                    String base = getBaseName(sourceName);
                    if (name.startsWith(base + ".")) {
                        files.add(file);
                        break;
                    }
                }
            }
        }

        return files;
    }

    private boolean saveFileToDocumentTree(Uri treeUri, String displayName, File sourceFile) {
        try {
            DocumentFile root = DocumentFile.fromTreeUri(this, treeUri);
            if (root == null) return false;

            DocumentFile file = root.findFile(displayName);
            if (file == null) {
                file = root.createFile(getMimeType(displayName), displayName);
            }
            if (file == null) return false;

            try (InputStream in = new FileInputStream(sourceFile);
                 OutputStream out = getContentResolver().openOutputStream(file.getUri(), "w")) {
                if (out == null) return false;
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                return true;
            }
        } catch (SecurityException sec) {
            Log.e(TAG, "Permiso SAF perdido", sec);
            clearSavedExportUri();
            updateLogs("Permiso de carpeta perdido. Selecciona nuevamente una carpeta de destino.");
            mainHandler.post(() -> launchFolderPicker(false));
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error guardando archivo", e);
            return false;
        }
    }

    private String getMimeType(String fileName) {
        return "application/octet-stream";
    }

    private void persistCurrentModuleSources(File projectDir) {
        if (!projectDir.exists() && !projectDir.mkdirs()) {
            updateLogs("No se pudo preparar directorio para exportación.");
            return;
        }

        ModuleState state = getCurrentState();
        LinkedHashMap<String, String> snapshotFiles = new LinkedHashMap<>(state.files);
        for (String fileName : snapshotFiles.keySet()) {
            boolean saved = FileManager.writeToFile(new File(projectDir, fileName), snapshotFiles.get(fileName));
            if (!saved) {
                updateLogs("No se pudo guardar fuente para exportar: " + fileName);
            }
        }
    }

    private void saveExportUri(Uri uri) {
        try {
            Uri previous = getSavedExportUri();
            if (previous != null && !previous.equals(uri)) {
                try {
                    getContentResolver().releasePersistableUriPermission(previous,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } catch (Exception ignored) {
                }
            }

            getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putString(KEY_EXPORT_URI, uri.toString())
                    .apply();
        } catch (SecurityException sec) {
            Log.e(TAG, "No se pudo persistir permiso SAF", sec);
            updateLogs("No se pudo guardar permiso de carpeta. Vuelve a seleccionarla.");
        }
    }

    private Uri getSavedExportUri() {
        String uri = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(KEY_EXPORT_URI, null);
        return uri == null ? null : Uri.parse(uri);
    }

    private boolean hasPersistedPermission(Uri uri) {
        for (android.content.UriPermission permission : getContentResolver().getPersistedUriPermissions()) {
            if (permission.getUri().equals(uri) && permission.isWritePermission()) {
                return true;
            }
        }
        return false;
    }

    private void clearSavedExportUri() {
        Uri previous = getSavedExportUri();
        if (previous != null) {
            try {
                getContentResolver().releasePersistableUriPermission(previous,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } catch (Exception ignored) {
            }
        }

        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .remove(KEY_EXPORT_URI)
                .apply();
    }

    private void updateLogs(String text) {
        mainHandler.post(() -> {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            String current = binding.textLogs.getText().toString();
            binding.textLogs.setText(current + "\n[" + time + "] " + text);
            binding.scrollLogs.post(() -> binding.scrollLogs.fullScroll(View.FOCUS_DOWN));
        });
    }

    static {
        System.loadLibrary("ptc");
    }

    public native String stringFromJNI();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
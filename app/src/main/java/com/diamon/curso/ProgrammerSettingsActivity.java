package com.diamon.curso;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProgrammerSettingsActivity extends AppCompatActivity {

    private static final String PREFS = "FlashromPrefs";
    private static final String KEY_PROGRAMMER = "selected_programmer";

    private EditText etProgrammerParam;
    private Button btnSaveProgrammer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmer_settings);

        setTitle("Configuración de Programador");

        etProgrammerParam = findViewById(R.id.etProgrammerParam);
        btnSaveProgrammer = findViewById(R.id.btnSaveProgrammer);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String current = prefs.getString(KEY_PROGRAMMER, "ch341a_spi");
        etProgrammerParam.setText(current);

        btnSaveProgrammer.setOnClickListener(v -> {
            String value = etProgrammerParam.getText().toString().trim();
            if (value.isEmpty()) {
                value = "ch341a_spi";
            }
            prefs.edit().putString(KEY_PROGRAMMER, value).apply();
            Toast.makeText(this, "Guardado: " + value, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

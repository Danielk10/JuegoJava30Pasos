package com.diamon.utilidad;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeFormatter {

    // Compilar las expresiones regulares una vez
    private static final Pattern KEYWORD_PATTERN =
            Pattern.compile(
                    "\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|"
                            + "continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|"
                            + "instanceof|int|interface|long|native|new|null|package|private|protected|public|return|short|static|"
                            + "strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while)\\b");
    private static final Pattern TYPE_PATTERN =
            Pattern.compile("\\b(String|int|float|double|boolean|char|long|short|byte)\\b");
    private static final Pattern SINGLE_LINE_COMMENT_PATTERN = Pattern.compile("//.*$");
    private static final Pattern MULTI_LINE_COMMENT_PATTERN = Pattern.compile("/\\*[\\s\\S]*?\\*/");

    public TextView formatCode(Context context, String code) {
        TextView codeTextView = new TextView(context);
        codeTextView.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        codeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        codeTextView.setTypeface(Typeface.MONOSPACE);
        codeTextView.setPadding(16, 16, 16, 16);
        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(Color.parseColor("#1E1E2C")); 
        codeTextView.setBackground(backgroundDrawable);

        codeTextView.setTextColor(Color.WHITE); // Texto blanco
        codeTextView.setVerticalScrollBarEnabled(true);

        // Añadir enumeración de líneas y resaltar sintaxis
        Spannable formattedCode = addLineNumbersAndHighlightSyntax(code);
        codeTextView.setText(formattedCode);

        return codeTextView;
    }

    private Spannable addLineNumbersAndHighlightSyntax(String code) {
        String[] lines = code.split("\n");
        SpannableStringBuilder formattedCode = new SpannableStringBuilder();

        // Añadir enumeración de líneas y resaltar sintaxis
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String lineNumber = String.format("%02d: ", i + 1); // Enumeración de líneas
            formattedCode.append(lineNumber);
            formattedCode.append(highlightSyntax(line));
            formattedCode.append("\n"); // Salto de línea
        }

        return formattedCode;
    }

    private Spannable highlightSyntax(String line) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(line);

        // Resaltar las palabras clave
        highlightPattern(spannable, line, KEYWORD_PATTERN, Color.CYAN);

        // Resaltar los tipos de datos
        highlightPattern(spannable, line, TYPE_PATTERN, Color.CYAN);

        // Resaltar los comentarios
        highlightPattern(spannable, line, SINGLE_LINE_COMMENT_PATTERN, Color.GREEN);
        highlightPattern(spannable, line, MULTI_LINE_COMMENT_PATTERN, Color.GREEN);

        return spannable;
    }

    private void highlightPattern(
            SpannableStringBuilder spannable, String text, Pattern pattern, int color) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            spannable.setSpan(
                    new ForegroundColorSpan(color),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}

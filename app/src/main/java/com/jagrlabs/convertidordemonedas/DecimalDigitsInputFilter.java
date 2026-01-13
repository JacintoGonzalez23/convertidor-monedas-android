package com.jagrlabs.convertidordemonedas;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalDigitsInputFilter implements InputFilter {
    private final int decimalDigits;

    public DecimalDigitsInputFilter(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 1. Creamos el StringBuilder para combinar el texto sin errores/warnings

        String resultText = String.valueOf(dest.subSequence(0, dstart)) +
                source.subSequence(start, end) +
                dest.subSequence(dend, dest.length());

        // 2. Si el texto resultante está vacío, permitirlo (borrado total)
        if (resultText.isEmpty()) {
            return null;
        }

        // 3. Si solo es un punto, permitirlo (para empezar con ".5")
        if (resultText.equals(".")) {
            return null;
        }

        // 4. Verificar si hay más de un punto decimal
        int count = 0;
        for (int i = 0; i < resultText.length(); i++) {
            if (resultText.charAt(i) == '.') count++;
        }
        if (count > 1) return "";

        // 5. Verificar la cantidad de decimales después del punto
        if (resultText.contains(".")) {
            String decimals = resultText.substring(resultText.indexOf(".") + 1);
            if (decimals.length() > decimalDigits) {
                return "";
            }
        }

        return null; // Acepta el cambio si pasa todas las pruebas
    }
}
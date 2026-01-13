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
        // Combinar el texto actual con el nuevo cambio
        String resultText = dest.subSequence(0, dstart).toString()
                + source.subSequence(start, end).toString()
                + dest.subSequence(dend, dest.length()).toString();

        // Si el texto está vacío, permitirlo
        if (resultText.isEmpty()) {
            return null;
        }

        // Si solo es un punto, permitirlo (para que el usuario pueda empezar con ".5")
        if (resultText.equals(".")) {
            return null;
        }

        // Verificar si hay más de un punto decimal
        int count = 0;
        for (int i = 0; i < resultText.length(); i++) {
            if (resultText.charAt(i) == '.') count++;
        }
        if (count > 1) return "";

        // Verificar la cantidad de decimales después del punto
        if (resultText.contains(".")) {
            String decimals = resultText.substring(resultText.indexOf(".") + 1);
            if (decimals.length() > decimalDigits) {
                return "";
            }
        }

        return null; // Acepta el cambio
    }
}
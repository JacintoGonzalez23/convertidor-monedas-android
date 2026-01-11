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
        String currentText = dest.toString();
        String newText = currentText.substring(0, dstart) + source.subSequence(start, end) + currentText.substring(dend);

        // Allow '-' only at the beginning
        if (newText.equals("-") && decimalDigits > 0) {
            return null;
        }

        // Allow only one decimal point
        if (newText.indexOf('.') != -1 && newText.substring(newText.indexOf('.') + 1).length() > decimalDigits) {
            return "";
        }

        // Allow only digits and at most one decimal point
        if (!newText.matches("^\\d+(\\.\\d{0," + decimalDigits + "})?$")) {
            return "";
        }

        return null;
    }
}
package com.jagrlabs.convertidordemonedas;

import android.content.SharedPreferences;
import android.os.Bundle;
// 1. Cambiamos la importación
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ConfiguracionActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        setTitle(getString(R.string.titulo_configuracion));


        SwitchCompat switchModoOscuro = findViewById(R.id.switch_modo_oscuro);

        // Cargar el estado desde SharedPreferences usando tu clase AppPreferences
        SharedPreferences prefs = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE);
        boolean modoOscuroActivado = prefs.getBoolean(AppPreferences.MODO_OSCURO_KEY, false);

        if (switchModoOscuro != null) {
            // Solo marcamos el switch según lo guardado
            switchModoOscuro.setChecked(modoOscuroActivado);

            // El listener se encargará de cambiar el tema SOLO cuando el usuario lo mueva
            switchModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> activarDesactivarModoOscuro(isChecked));
        }


    }

    private void activarDesactivarModoOscuro(boolean activar) {
        // 1. Guardar primero la preferencia
        SharedPreferences.Editor editor = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(AppPreferences.MODO_OSCURO_KEY, activar);
        editor.apply();

        // 2. Aplicar el modo
        if (activar) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


}
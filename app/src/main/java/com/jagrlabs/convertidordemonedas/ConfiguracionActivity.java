package com.jagrlabs.convertidordemonedas;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ConfiguracionActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String MODO_OSCURO_KEY = "modoOscuro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        setTitle(getString(R.string.titulo_configuracion)); // Agregado el recurso de texto

        Switch switchModoOscuro = findViewById(R.id.switch_modo_oscuro);

        // Cargar el estado del modo oscuro desde SharedPreferences
        // En ConfiguracionActivity
        SharedPreferences prefs = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE);
        boolean modoOscuroActivado = prefs.getBoolean(AppPreferences.MODO_OSCURO_KEY, false);


        // Configurar el estado del interruptor según el modo oscuro
        switchModoOscuro.setChecked(modoOscuroActivado);

        // Configurar el listener del interruptor
        switchModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Activar o desactivar el modo oscuro según el estado del interruptor
            activarDesactivarModoOscuro(isChecked);
        });

        // Activa o desactiva el modo oscuro según el estado guardado o predeterminado
        activarDesactivarModoOscuro(modoOscuroActivado);
    }

    private void activarDesactivarModoOscuro(boolean activar) {
        // Aplicar el modo oscuro según el estado actual
        if (activar) {
            // Activar modo oscuro
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // Desactivar modo oscuro
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Actualizar el texto del título después de cambiar el modo
        setTitle(getString(R.string.titulo_configuracion));

        // Guardar el estado del modo oscuro en SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(MODO_OSCURO_KEY, activar);
        editor.apply();
    }
}
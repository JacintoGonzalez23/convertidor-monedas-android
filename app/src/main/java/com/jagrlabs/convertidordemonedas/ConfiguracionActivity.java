package com.jagrlabs.convertidordemonedas;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.activity.OnBackPressedCallback;

public class ConfiguracionActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        setTitle(getString(R.string.titulo_configuracion));

        //  Configura la barra con la flecha de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.titulo_configuracion));
        }
        // --- retrocede con gestos android 13+ ---
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Esto se ejecuta cuando el usuario presiona el botón "atrás" físico
                // o hace el gesto de deslizar en el borde de la pantalla
                finish();
            }
        });

        SwitchCompat switchModoOscuro = findViewById(R.id.switch_modo_oscuro);

        // Carga el estado desde SharedPreferences usando tu clase AppPreferences
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
        //  Guarda primero la preferencia
        SharedPreferences.Editor editor = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(AppPreferences.MODO_OSCURO_KEY, activar);
        editor.apply();

        //  Aplica el modo
        if (activar) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //Hace que la flecha realmente cierre la actividad y vuelva atrás
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Cierra esta actividad y regresa a la anterior (MainActivity)
        return true;
    }


}
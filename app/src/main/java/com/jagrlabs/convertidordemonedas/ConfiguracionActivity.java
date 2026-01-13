package com.jagrlabs.convertidordemonedas;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

public class ConfiguracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        Toolbar toolbar = findViewById(R.id.toolbar_config);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Configuración");
        }

        SwitchCompat switchModo = findViewById(R.id.switch_modo_oscuro);
        SharedPreferences prefs = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE);

        switchModo.setChecked(prefs.getBoolean(AppPreferences.MODO_OSCURO_KEY, false));

        switchModo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //  Guarda la preferencia
            prefs.edit().putBoolean(AppPreferences.MODO_OSCURO_KEY, isChecked).apply();

            //  Usa un Handler con un retraso de 200ms
            // Esto da tiempo a que el Switch termine de moverse antes de cambiar el tema
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }, 200);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

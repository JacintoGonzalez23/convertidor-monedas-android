package com.jagrlabs.convertidordemonedas;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ProgressBar;
import com.google.android.material.snackbar.Snackbar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private EditText editAmount;
    private Spinner spinnerTo;
    private TextView tvResult;
    private ProgressBar progressBar;

    // Se agrega Looper.getMainLooper() para que el Handler funcione correctamente
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configuración de tema (no olvidarse siempre primero sino da problemas)
        SharedPreferences prefs = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE);
        boolean modoOscuro = prefs.getBoolean(AppPreferences.MODO_OSCURO_KEY, false);
        AppCompatDelegate.setDefaultNightMode(modoOscuro ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Inicializa Ads
        MobileAds.initialize(this, initializationStatus -> {});

        //  Configura la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  Llama al método de configuración
        configurarVistasYSpinners();

        //  Carga anuncio con retraso para no bloquear la UI
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            AdView adView = findViewById(R.id.adView);
            if (adView != null) {
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
        }, 500);
    }


    private void configurarVistasYSpinners() {
        //  Inicialización normal de vistas
        editAmount = findViewById(R.id.editAmount);
        spinnerTo = findViewById(R.id.spinnerTo);
        Spinner spinnerFrom = findViewById(R.id.spinnerFrom);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);
        Button btnConvert = findViewById(R.id.btnConvert);

        editAmount.setOnEditorActionListener((v, actionId, event) -> {
            // Detecta cuando el usuario presiona el botón Hecho (osea el check) del teclado
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                convertCurrency(); // Ejecuta la conversión directamente
                return true;
            }
            return false;
        });
        //  Usa un Handler para "empujar" la carga del adaptador al final de la cola de la UI
        new Handler(Looper.getMainLooper()).post(() -> {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.currencies,
                    R.layout.item_spinner_personalizado // El que tiene texto azul
            );
            // esta linea hace que el menu desplegable del spíner tome tambien la letra azul del layout item spinner personalizado
            adapter.setDropDownViewResource(R.layout.item_spinner_personalizado);

            spinnerFrom.setAdapter(adapter);
            spinnerTo.setAdapter(adapter);

            spinnerFrom.setSelection(0);
            spinnerFrom.setEnabled(false);

            // Forzar la actualización al Spinner a redibujarse
            spinnerTo.invalidate();
        });

        btnConvert.setOnClickListener(view -> convertCurrency());
    }

    private void convertCurrency() {
        // Validar conexión a internet con Snackbar
        if (!isNetworkConnected()) {
            mostrarSnackbar(getString(R.string.no_internet_connection));
            return;
        }

        String amountStr = editAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            mostrarSnackbar(getString(R.string.empy_amount));
            return;
        }

        double amount = Double.parseDouble(amountStr);
        String toCurrency = spinnerTo.getSelectedItem().toString();
        String apiKey = "e08261134e50a4e0da1fe4e6";
        String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";

        // --- CAMBIOS PARA EL PROGRESSBAR ---
        tvResult.setVisibility(View.GONE); // Ocultamos el resultado anterior
        progressBar.setVisibility(View.VISIBLE); // MOSTRAR BARRA
        findViewById(R.id.btnConvert).setEnabled(false); // Desactivar botón para evitar spam

        executorService.execute(() -> {

            Double result = performNetworkRequest(apiUrl, toCurrency, amount);

            mainHandler.post(() -> {
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.btnConvert).setEnabled(true);

                if (result != null) {
                    tvResult.setVisibility(View.VISIBLE);
                    tvResult.setAlpha(0f); // se empieza invisible para el Fade In

                    // --- ANIMACIÓN DE CONTADOR ---
                    // se anima desde 0 hasta el resultado obtenido
                    ValueAnimator animator = ValueAnimator.ofFloat(0f, result.floatValue());
                    animator.setDuration(1000); // Duración de 1 segundo

                    animator.addUpdateListener(animation -> {
                        float animatedValue = (float) animation.getAnimatedValue();
                        tvResult.setText(String.format(Locale.getDefault(), "%.2f", animatedValue));
                    });

                    // --- ANIMACIÓN DE FADE IN (Aparece suave) ---
                    tvResult.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .start();

                    animator.start();
                    tvResult.setTextSize(48);
                } else {
                    mostrarSnackbar("Error al conectar con el servidor");
                }
            });
        });

        // Esto cierra el teclado automáticamente al presionar el botón
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editAmount.getWindowToken(), 0);
    }

    // metodo snackbar
    private void mostrarSnackbar(String mensaje) {
        // Usamos el layout principal para mostrar el snackbar
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, mensaje, Snackbar.LENGTH_LONG)
                .setAction("OK", v -> {}) // Botón de cerrar
                .setActionTextColor(getResources().getColor(R.color.primary_blue))
                .show();
    }

    // Lógica de red extraída de AsyncTask
    private Double performNetworkRequest(String apiUrl, String toCurrency, double amount) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream stream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(stream);
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.next());
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("conversion_rates")) {
                JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");
                if (conversionRates.has("USD") && conversionRates.has(toCurrency)) {
                    double usdToCurrencyRate = conversionRates.getDouble(toCurrency);
                    double usdAmount = amount / conversionRates.getDouble("USD");
                    return usdAmount * usdToCurrencyRate;
                }
            }
        } catch (IOException | JSONException e) {
            Log.e("CurrencyConverter", "Error: " + e.getMessage());
        }
        return null;
    }

    // Métodos de navegación y menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lista_monedas_disponibles) { mostrarListadoMonedas(); return true; }
        if (id == R.id.action_settings) { abrirActividadConfiguracion(); return true; }
        if (id == R.id.action_about) { mostrarAcercaDe(); return true; }
        if (id == R.id.salir_aplicacion) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarListadoMonedas() { startActivity(new Intent(this, ListadoMonedasDisponibles.class)); }
    private void abrirActividadConfiguracion() { startActivity(new Intent(this, ConfiguracionActivity.class)); }
    private void mostrarAcercaDe() { startActivity(new Intent(this, AcercaDeActivity.class)); }




    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    @Override
    protected void onPause() {
        if (findViewById(R.id.adView) != null) {
            ((AdView) findViewById(R.id.adView)).pause();
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (findViewById(R.id.adView) != null) {
            ((AdView) findViewById(R.id.adView)).resume();
        }
    }
    @Override
    protected void onDestroy() {
        if (findViewById(R.id.adView) != null) {
            ((AdView) findViewById(R.id.adView)).destroy();
        }
        super.onDestroy();
    }

}

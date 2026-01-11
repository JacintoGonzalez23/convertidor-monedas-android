package com.jagrlabs.convertidordemonedas;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    private EditText editAmount;
    private Spinner spinnerTo;
    private TextView tvResult;

    // Ejecutor para reemplazar AsyncTask
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE);
        boolean modoOscuroActivado = prefs.getBoolean(AppPreferences.MODO_OSCURO_KEY, false);

        if (modoOscuroActivado) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        setContentView(R.layout.activity_main);

        // Inicialización de vistas
        editAmount = findViewById(R.id.editAmount);
        Spinner spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        tvResult = findViewById(R.id.tvResult);
        Button btnConvert = findViewById(R.id.btnConvert);

        // Configuración del botón Calificar (Evita el warning del XML)


        editAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.currencies, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
        spinnerFrom.setSelection(0);
        spinnerFrom.setEnabled(false);

        btnConvert.setOnClickListener(view -> convertCurrency());
    }

    private void convertCurrency() {
        if (!isNetworkConnected()) {
            tvResult.setText(R.string.no_internet_connection);
            tvResult.setTextSize(25);
            return;
        }

        String amountStr = editAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            tvResult.setText(R.string.empy_amount);
            tvResult.setTextSize(25);
            return;
        }

        double amount = Double.parseDouble(amountStr);
        String toCurrency = spinnerTo.getSelectedItem().toString();
        String apiKey = "e08261134e50a4e0da1fe4e6";
        String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";

        // --- REEMPLAZO DE ASYNCTASK ---
        executorService.execute(() -> {
            // Esto corre en segundo plano (doInBackground)
            Double result = performNetworkRequest(apiUrl, toCurrency, amount);

            mainHandler.post(() -> {
                // Esto corre en el hilo principal (onPostExecute)
                if (result != null) {
                    String formattedResult = String.format(Locale.getDefault(), "%.2f", result);
                    tvResult.setText(formattedResult);
                    tvResult.setTextSize(40);
                } else {
                    tvResult.setText(R.string.empy_amount);
                    tvResult.setTextSize(40);
                }
            });
        });

        // Ocultar teclado
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(editAmount.getWindowToken(), 0);
        }
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
}

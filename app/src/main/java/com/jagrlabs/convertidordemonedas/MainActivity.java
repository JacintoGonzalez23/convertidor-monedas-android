package com.jagrlabs.convertidordemonedas;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;
import android.text.InputFilter;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.inputmethod.InputMethodManager;





public class MainActivity extends AppCompatActivity {

    private EditText editAmount;
    private Spinner spinnerFrom, spinnerTo;
    private Button btnConvert;
    private TextView tvResult;

    // Dentro de tu MainActivity



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // En MainActivity
        SharedPreferences prefs = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE);
        boolean modoOscuroActivado = prefs.getBoolean(AppPreferences.MODO_OSCURO_KEY, false);


        // Aplicar el modo oscuro solo si está habilitado en la configuración
        if (modoOscuroActivado) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        setContentView(R.layout.activity_main);



        editAmount = findViewById(R.id.editAmount);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        btnConvert = findViewById(R.id.btnConvert);
        tvResult = findViewById(R.id.tvResult);

        editAmount = findViewById(R.id.editAmount);
        editAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)}); // Ajusta el número de dígitos decimales según tus necesidades

        // Configurar adaptador para los spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.currencies,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Establecer selección inicial por posición (por ejemplo, USD en la posición 0)
        spinnerFrom.setSelection(0);

        spinnerFrom.setEnabled(false); // Esto deshabilitará la selección en el spinnerFrom
        // Configurar el botón de conversión
        btnConvert.setOnClickListener(view -> convertCurrency());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.lista_monedas_disponibles) {
            mostrarListadoMonedas();
            return true;
        } else if (id == R.id.action_settings) {
            abrirActividadConfiguracion();
            return true;
        } else if (id == R.id.action_about) {
            mostrarAcercaDe();
            return true;
        } else if (id == R.id.salir_aplicacion) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void mostrarListadoMonedas() {
        // Aquí puedes mostrar un cuadro de diálogo, abrir una nueva actividad, etc.
        // Por ejemplo, abrir una nueva actividad de Acerca de
        Intent intent = new Intent(this, ListadoMonedasDisponibles.class);
        startActivity(intent);
    }
    private void abrirActividadConfiguracion() {
        Intent intent = new Intent(this, ConfiguracionActivity.class);
        startActivity(intent);
    }
    private void mostrarAcercaDe() {
        // Aquí puedes mostrar un cuadro de diálogo, abrir una nueva actividad, etc.
        // Por ejemplo, abrir una nueva actividad de Acerca de
        Intent intent = new Intent(this, AcercaDeActivity.class);
        startActivity(intent);
    }
    private void convertCurrency() {
        // Verificar si el dispositivo está conectado a Internet
        if (!isNetworkConnected()) {
            // Mostrar mensaje si no hay conexión a Internet
            tvResult.setText(R.string.no_internet_connection);
            tvResult.setTextSize(25);
            return;
        }
        String amountStr = editAmount.getText().toString().trim();
        // Verificar si el campo de monto está vacío
        if (amountStr.isEmpty()) {
            // Mostrar un mensaje al usuario
            tvResult.setText(R.string.empy_amount);
            tvResult.setTextSize(25);
            return; // Salir del método si el monto está vacío
        }
        // Obtener datos de la interfaz
        double amount = Double.parseDouble(amountStr);
        String fromCurrency = spinnerFrom.getSelectedItem().toString();
        String toCurrency = spinnerTo.getSelectedItem().toString();

        // Construir la URL de la API de conversión (reemplaza 'API_KEY' con tu clave real)
        String apiKey = "e08261134e50a4e0da1fe4e6"; // Reemplaza "tu_clave_de_api" con tu clave real
        String apiUrl = "https://v6.exchangerate-api.com/v6/"+apiKey+"/latest/USD";


        // Llamar a la tarea asincrónica para realizar la conversión
        new FetchExchangeRateTask().execute(apiUrl, toCurrency, String.valueOf(amount));


        // Cerrar el teclado virtual después de hacer clic en el botón "Convertir"
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editAmount.getWindowToken(), 0);

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private class FetchExchangeRateTask extends AsyncTask<String, Void, Double> {

        @Override
        protected Double doInBackground(String... params) {
            try {
                String apiUrl = params[0];
                String toCurrency = params[1];
                double amount = Double.parseDouble(params[2]);

                // Imprime la URL de la API para depurar
                Log.d("CurrencyConverter", "API URL: " + apiUrl);

                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream stream = urlConnection.getInputStream();
                Scanner scanner = new Scanner(stream);

                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.next());
                }

                // Imprime la respuesta para depurar


                JSONObject jsonResponse = new JSONObject(response.toString());

                // Verifica si la respuesta contiene las tasas de cambio
                if (jsonResponse.has("conversion_rates")) {
                    JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");

                    // Verifica si la moneda de origen y la moneda de destino están presentes
                    if (conversionRates.has("USD") && conversionRates.has(toCurrency)) {
                        double usdToCurrencyRate = conversionRates.getDouble(toCurrency);
                        double usdAmount = amount / conversionRates.getDouble("USD");

                        // Realiza la conversión y devuelve el resultado
                        return usdAmount * usdToCurrencyRate;
                    } else {
                        // Maneja el caso en el que la moneda de origen o destino no está presente
                        Log.e("CurrencyConverter", "No hay valor para USD " + toCurrency + " en respuesta");
                        return null;
                    }
                } else {
                    // Maneja el caso en el que "conversion_rates" no está presente en el JSON
                    Log.e("CurrencyConverter", "No se encuentra el valor");
                    return null;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.e("CurrencyConverter", "Error al procesar la respuesta de la API");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Double result) {
            if (result != null) {
                // Modificar el formato para mostrar un número más grande
                String formattedResult = String.format(Locale.getDefault(), "%.2f", result);

                // Mostrar el resultado en el TextView
                tvResult.setText(formattedResult);
                tvResult.setTextSize(40);
            } else {
                // Manejar el caso en el que no se pudo obtener la tasa de cambio
                tvResult.setText(R.string.empy_amount);
                tvResult.setTextSize(40);
            }
        }

    }
}

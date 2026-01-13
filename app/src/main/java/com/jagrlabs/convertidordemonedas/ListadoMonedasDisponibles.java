package com.jagrlabs.convertidordemonedas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.OnBackPressedCallback;


public class ListadoMonedasDisponibles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_monedas_dispo);

        // Configuración de la flecha en la barra superior
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Listado de Monedas");
        }

        // --- codigo de gestos de retroceder ---
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Esto maneja el botón físico o el gesto de deslizar del celular
                finish();
            }
        });

        //  Buscamos el RecyclerView
        RecyclerView rv = findViewById(R.id.rvMonedas);

        //  Le decimos que se comporte como una lista vertical
        rv.setLayoutManager(new LinearLayoutManager(this));

        //  Creamos la lista de monedas usando tus strings
        List<Moneda> listaDeMonedas = new ArrayList<>();
        listaDeMonedas.add(new Moneda("USD", getString(R.string.dolar_estadounidense)));
        listaDeMonedas.add(new Moneda("EUR", getString(R.string.euro2)));
        listaDeMonedas.add(new Moneda("MXN", getString(R.string.peso_mexicano)));
        listaDeMonedas.add(new Moneda("PEN", getString(R.string.sol_peruano)));
        listaDeMonedas.add(new Moneda("JPY", getString(R.string.yen_japon_s)));
        listaDeMonedas.add(new Moneda("COP", getString(R.string.peso_colombiano)));
        listaDeMonedas.add(new Moneda("DOP", getString(R.string.peso_dominicano)));
        listaDeMonedas.add(new Moneda("CLP", getString(R.string.peso_chileno)));
        listaDeMonedas.add(new Moneda("CUP", getString(R.string.peso_cubano)));
        listaDeMonedas.add(new Moneda("UYU", getString(R.string.peso_uruguayo)));
        listaDeMonedas.add(new Moneda("VES", getString(R.string.bol_var_soberano_venezolano)));
        // AgregaR aquí todas las demás que quiera MOSTRAR...

        //  Le pasamos la lista al adaptador y el adaptador al RecyclerView
        MonedaAdapter adapter = new MonedaAdapter(listaDeMonedas);
        rv.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Cierra esta actividad y regresa a la anterior (MainActivity)
        return true;
    }
}

package com.jagrlabs.convertidordemonedas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        //  Configurar la Toolbar manualmente (Vital para temas NoActionBar)
        Toolbar toolbar = findViewById(R.id.toolbar_listado);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Monedas Disponibles");
        }

        //  Manejo de retroceso
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        //  Configura RecyclerView
        RecyclerView rv = findViewById(R.id.rvMonedas);
        rv.setLayoutManager(new LinearLayoutManager(this));

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

        MonedaAdapter adapter = new MonedaAdapter(listaDeMonedas);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

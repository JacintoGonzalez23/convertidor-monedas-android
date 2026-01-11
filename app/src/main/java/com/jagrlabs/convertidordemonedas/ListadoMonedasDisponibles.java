package com.jagrlabs.convertidordemonedas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class ListadoMonedasDisponibles extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_monedas_dispo);
        setTitle("Lista de monedas");
    }

}

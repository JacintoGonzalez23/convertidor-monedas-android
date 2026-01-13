package com.jagrlabs.convertidordemonedas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback; // import de los gestos de retorceder
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acercade);

        //  Configura Flecha de Retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Acerca de");
        }

        //  Manejo moderno del botón/gesto atrás
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        Button calificarButton = findViewById(R.id.calificarButton);
        if (calificarButton != null) {
            calificarButton.setOnClickListener(this::mostrarDialogoCalificacion);
        }
    }

    // Maneja el clic en la flecha de la barra superior
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void mostrarDialogoCalificacion(View view) {
        AlertDialog dialog = getAlertDialog();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_NEGATIVE);

            //
            try {
                positiveButton.setTextColor(getResources().getColor(R.color.colorMiBotonCalificar));
                negativeButton.setTextColor(getResources().getColor(R.color.colorMiBotonCancelar));
            } catch (Exception e) {
                // Fallback si los colores no existen
            }
        });

        dialog.show();
    }

    @NonNull
    private AlertDialog getAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Te gusta la aplicación?");
        builder.setMessage("Tu opinión ayuda a seguir mejorando. ¿Podrías regalar una calificación?");
      /*
        builder.setPositiveButton("Calificar", (dialog, which) -> {
            try {
                // Esto intenta abrir la Play Store directamente en la app
                startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse("market://details?id=" + getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                // Si no tiene la app de Play Store (como en algunos emuladores), abre el navegador
                startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });
        */
        builder.setPositiveButton("Calificar", (dialog, which) -> {
            Toast.makeText(AcercaDeActivity.this, "¡Gracias por tu apoyo!", Toast.LENGTH_SHORT).show();
            // Aquí podrías abrir la Play Store en el futuro
        });

        builder.setNegativeButton("Ahora no", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}

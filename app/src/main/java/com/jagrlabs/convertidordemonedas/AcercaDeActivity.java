package com.jagrlabs.convertidordemonedas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.OnBackPressedCallback; // import de los gestos de retorceder
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acercade);

        //  Configura la Toolbar correctamente
        Toolbar toolbar = findViewById(R.id.toolbar_acerca);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Acerca de"); // Título centralizado
        }

        //  Manejo del botón/gesto atrás (Android 13+)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        //  Botón de calificación
        Button calificarButton = findViewById(R.id.calificarButton);
        if (calificarButton != null) {
            calificarButton.setOnClickListener(this::mostrarDialogoCalificacion);
        }
    }

    // Flecha de retroceso de la Toolbar
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

            // Uso de ContextCompat para evitar errores de color según la versión de Android
            try {
                positiveButton.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.colorMiBotonCalificar));
                negativeButton.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.colorMiBotonCancelar));
            } catch (Exception e) {
                // Colores de respaldo si  fallan
            }
        });

        dialog.show();
    }

    @NonNull
    private AlertDialog getAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Te gusta la aplicación?");
        builder.setMessage("Tu opinión ayuda a seguir mejorando. ¿Podrías regalar una calificación?");

        builder.setPositiveButton("Calificar", (dialog, which) -> {
            Toast.makeText(AcercaDeActivity.this, "¡Gracias por tu apoyo!", Toast.LENGTH_SHORT).show();
            // Descomenta el código de Play Store cuando estés listo para publicar
        });

        builder.setNegativeButton("Ahora no", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}

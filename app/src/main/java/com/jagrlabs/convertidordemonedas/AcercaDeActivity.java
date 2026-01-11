package com.jagrlabs.convertidordemonedas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acercade);
        setTitle("Acerca de");
        // Buscamos el botón por su ID
        Button calificarButton = findViewById(R.id.calificarButton);

        // Le asignamos el clic directamente aquí (Explicit wiring)
        if (calificarButton != null) {
            calificarButton.setOnClickListener(this::mostrarDialogoCalificacion);
        }
    }



    public void mostrarDialogoCalificacion(View view) {
        AlertDialog dialog = getAlertDialog();

        // Personalizar el color del texto de ambos botones
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_NEGATIVE);

            // Utiliza los colores definidos en ecolors.xml
            positiveButton.setTextColor(getResources().getColor(R.color.colorMiBotonCalificar));
            negativeButton.setTextColor(getResources().getColor(R.color.colorMiBotonCancelar));
        });

        dialog.show();
    }

    @NonNull
    private AlertDialog getAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Califica la aplicación por favor");
        builder.setMessage("Si realmente te gusta la app, nos gustaría que nos dejes una buena puntuación.");

        // Botón "Calificar"
        builder.setPositiveButton("Calificar", (dialog, which) -> {
            // Puedes redirigir al usuario a la página de calificación de la aplicación en la tienda
            // Por ejemplo, Google Play Store
            //esto es una linea para actualizar
            Toast.makeText(AcercaDeActivity.this, "Redirigir al usuario a la página de calificación", Toast.LENGTH_SHORT).show();
        });

        // Botón "Cancelar"
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }


}

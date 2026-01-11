package com.jagrlabs.convertidordemonedas;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acercade);
        setTitle("Acerca de");
    }


    // Método llamado al hacer clic en el botón "Califíquenos"
    public void mostrarDialogoCalificacion(View view) {
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

        AlertDialog dialog = builder.create();

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


}

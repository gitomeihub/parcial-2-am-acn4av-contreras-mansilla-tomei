package com.example.barberia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InicioActivity extends AppCompatActivity {

    // Botones de la pantalla inicial.
    private Button btnReservarDesdeInicio;
    private Button btnMisTurnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula esta activity con el archivo activity_inicio.xml.
        setContentView(R.layout.activity_inicio);

        // Se obtienen las referencias de los botones definidos en el XML.
        btnReservarDesdeInicio = findViewById(R.id.btnReservarDesdeInicio);
        btnMisTurnos = findViewById(R.id.btnMisTurnos);

        /*
         * Abre la pantalla de reserva que ya existía en el TP1.
         * Más adelante podemos renombrar MainActivity a ReservarTurnoActivity,
         * pero por ahora la mantenemos así para no romper lo que ya funciona.
         */
        btnReservarDesdeInicio.setOnClickListener(view -> {
            Intent intent = new Intent(InicioActivity.this, MainActivity.class);
            startActivity(intent);
        });

        /*
         * Por el momento esta pantalla todavía no está creada.
         * Se muestra un mensaje temporal y luego la reemplazaremos
         * por la activity real de "Mis turnos".
         */
        btnMisTurnos.setOnClickListener(view -> {
            Toast.makeText(
                    InicioActivity.this,
                    getString(R.string.mensaje_mis_turnos_proximamente),
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}
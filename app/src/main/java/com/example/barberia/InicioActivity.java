package com.example.barberia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InicioActivity extends AppCompatActivity {

    // Botones principales de la pantalla de inicio.
    private Button btnReservarDesdeInicio;
    private Button btnMisTurnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula esta activity con el archivo activity_inicio.xml.
        setContentView(R.layout.activity_inicio);

        // Referencias a los botones definidos en el XML.
        btnReservarDesdeInicio = findViewById(R.id.btnReservarDesdeInicio);
        btnMisTurnos = findViewById(R.id.btnMisTurnos);

        /*
         * Abre la pantalla de reserva que ya existía en el TP1.
         */
        btnReservarDesdeInicio.setOnClickListener(view -> {
            Intent intent = new Intent(
                    InicioActivity.this,
                    MainActivity.class
            );
            startActivity(intent);
        });

        /*
         * Abre la pantalla donde se mostrarán las reservas confirmadas.
         */
        btnMisTurnos.setOnClickListener(view -> {
            Intent intent = new Intent(
                    InicioActivity.this,
                    MisTurnosActivity.class
            );
            startActivity(intent);
        });
    }
}
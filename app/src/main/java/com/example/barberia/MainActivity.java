package com.example.barberia;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. LÓGICA DE FECHAS (CARRUSEL)
        android.widget.LinearLayout layoutFechas = findViewById(R.id.layoutFechasDinamico);
        String[] fechas = {"Lun\n13", "Mar\n14", "Mié\n15", "Jue\n16", "Vie\n17", "Sáb\n18"};

        for (int i = 0; i < fechas.length; i++) {
            android.widget.Button btnFecha = new android.widget.Button(this);
            btnFecha.setText(fechas[i]);
            btnFecha.setAllCaps(false);

            android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(160, 200);
            params.setMargins(0, 0, 20, 0);
            btnFecha.setLayoutParams(params);

            // Estado inicial: El primero arranca dorado, los demás grises
            if (i == 0) {
                btnFecha.setBackgroundColor(android.graphics.Color.parseColor("#D4AF37"));
                btnFecha.setTextColor(android.graphics.Color.BLACK);
            } else {
                btnFecha.setBackgroundColor(android.graphics.Color.parseColor("#2A2A2A"));
                btnFecha.setTextColor(android.graphics.Color.WHITE);
            }
            // EVENTO DE CLIC PARA SELECCIONAR
            btnFecha.setOnClickListener(v -> {
                // 1. Pintar todos de gris primero
                for (int j = 0; j < layoutFechas.getChildCount(); j++) {
                    android.widget.Button botonHermano = (android.widget.Button) layoutFechas.getChildAt(j);
                    botonHermano.setBackgroundColor(android.graphics.Color.parseColor("#2A2A2A"));
                    botonHermano.setTextColor(android.graphics.Color.WHITE);
                }
                // 2. Pintar solo el que toqué de dorado
                btnFecha.setBackgroundColor(android.graphics.Color.parseColor("#D4AF37"));
                btnFecha.setTextColor(android.graphics.Color.BLACK);
            });

            layoutFechas.addView(btnFecha);
        }
        // 2. LÓGICA DE HORARIOS (GRILLA)
        android.widget.GridLayout layoutHorarios = findViewById(R.id.layoutHorariosDinamico);
        String[] horarios = {"08:00", "09:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"};

        for (int i = 0; i < horarios.length; i++) {
            android.widget.Button btnHora = new android.widget.Button(this);
            btnHora.setText(horarios[i]);

            android.widget.GridLayout.LayoutParams paramsGrid = new android.widget.GridLayout.LayoutParams();
            paramsGrid.width = android.widget.GridLayout.LayoutParams.WRAP_CONTENT;
            paramsGrid.height = android.widget.GridLayout.LayoutParams.WRAP_CONTENT;
            paramsGrid.setMargins(10, 10, 10, 10);
            btnHora.setLayoutParams(paramsGrid);

            // Estado inicial: todos grises
            btnHora.setBackgroundColor(android.graphics.Color.parseColor("#2A2A2A"));
            btnHora.setTextColor(android.graphics.Color.parseColor("#D4AF37"));

            // EVENTO DE CLIC PARA SELECCIONAR
            btnHora.setOnClickListener(v -> {
                // 1. Pintar todos de gris primero
                for (int j = 0; j < layoutHorarios.getChildCount(); j++) {
                    android.widget.Button botonHermano = (android.widget.Button) layoutHorarios.getChildAt(j);
                    botonHermano.setBackgroundColor(android.graphics.Color.parseColor("#2A2A2A"));
                    botonHermano.setTextColor(android.graphics.Color.parseColor("#D4AF37")); // Texto dorado
                }
                // 2. Pintar el que toqué de dorado con texto negro
                btnHora.setBackgroundColor(android.graphics.Color.parseColor("#D4AF37"));
                btnHora.setTextColor(android.graphics.Color.BLACK);
            });

            layoutHorarios.addView(btnHora);
        }
        // 3. LÓGICA DE SERVICIOS
        android.widget.LinearLayout layoutServicios = findViewById(R.id.layoutServiciosDinamico);
        if (layoutServicios != null) {
            for (int i = 0; i < layoutServicios.getChildCount(); i++) {
                android.view.View servicioCard = layoutServicios.getChildAt(i);

                servicioCard.setOnClickListener(v -> {
                    // 1. Pintar todas las tarjetas de gris oscuro primero
                    for (int j = 0; j < layoutServicios.getChildCount(); j++) {
                        layoutServicios.getChildAt(j).setBackgroundColor(android.graphics.Color.parseColor("#2A2A2A"));
                    }
                    // 2. Pintar la tarjeta que toqué de dorado
                    v.setBackgroundColor(android.graphics.Color.parseColor("#D4AF37"));
                });
            }
        }
        // 4. EVENTO DEL BOTÓN CONFIRMAR FINAL
        android.widget.Button btnConfirmar = findViewById(R.id.btnConfirmarTurno);
        if (btnConfirmar != null) {
            btnConfirmar.setOnClickListener(v -> {
                android.widget.Toast.makeText(this, "¡Turno Reservado con Éxito!", android.widget.Toast.LENGTH_LONG).show();
            });
        }
    }
}
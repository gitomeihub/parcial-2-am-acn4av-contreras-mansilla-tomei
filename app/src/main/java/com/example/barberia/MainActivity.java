package com.example.barberia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    /*
     * Variables donde se guardan las opciones elegidas por el usuario.
     * Luego se envían a ResumenTurnoActivity mediante Intent extras.
     */
    private String fechaSeleccionada = "";
    private String horarioSeleccionado = "";
    private String servicioSeleccionado = "";
    private String precioSeleccionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula esta activity con el layout de la pantalla de reserva.
        setContentView(R.layout.activity_main);

        // Configura el botón para regresar a la pantalla principal.
        configurarBotonVolverInicio();

        // Configura las opciones de reserva.
        configurarFechas();
        configurarHorarios();
        configurarServicios();

        // Configura el botón final que lleva al resumen.
        configurarBotonConfirmar();
    }

    /*
     * Cierra la pantalla de reserva.
     * Como se abrió desde InicioActivity, vuelve automáticamente al inicio.
     */
    private void configurarBotonVolverInicio() {
        Button btnVolverInicio = findViewById(R.id.btnVolverInicio);

        btnVolverInicio.setOnClickListener(v -> finish());
    }

    /*
     * Crea los botones de fecha de forma dinámica.
     * La primera fecha aparece seleccionada por defecto.
     */
    private void configurarFechas() {
        LinearLayout layoutFechas = findViewById(R.id.layoutFechasDinamico);

        String[] fechas = {
                "Lun\n13",
                "Mar\n14",
                "Mié\n15",
                "Jue\n16",
                "Vie\n17",
                "Sáb\n18"
        };

        // Se guarda la primera fecha porque inicia seleccionada visualmente.
        fechaSeleccionada = fechas[0].replace("\n", " ");

        for (int i = 0; i < fechas.length; i++) {
            Button btnFecha = new Button(this);

            // Versión legible que se utilizará en la pantalla de resumen.
            String fechaActual = fechas[i].replace("\n", " ");

            btnFecha.setText(fechas[i]);
            btnFecha.setAllCaps(false);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(160, 200);

            params.setMargins(0, 0, 20, 0);
            btnFecha.setLayoutParams(params);

            // La primera fecha queda marcada; las demás empiezan sin seleccionar.
            if (i == 0) {
                btnFecha.setBackgroundColor(Color.parseColor("#D4AF37"));
                btnFecha.setTextColor(Color.BLACK);
            } else {
                btnFecha.setBackgroundColor(Color.parseColor("#2A2A2A"));
                btnFecha.setTextColor(Color.WHITE);
            }

            btnFecha.setOnClickListener(v -> {
                /*
                 * Se reinician todos los botones para que el usuario
                 * pueda tener solamente una fecha seleccionada.
                 */
                for (int j = 0; j < layoutFechas.getChildCount(); j++) {
                    Button botonHermano =
                            (Button) layoutFechas.getChildAt(j);

                    botonHermano.setBackgroundColor(
                            Color.parseColor("#2A2A2A")
                    );

                    botonHermano.setTextColor(Color.WHITE);
                }

                // Se resalta la fecha elegida.
                btnFecha.setBackgroundColor(Color.parseColor("#D4AF37"));
                btnFecha.setTextColor(Color.BLACK);

                // Se almacena la fecha elegida.
                fechaSeleccionada = fechaActual;
            });

            layoutFechas.addView(btnFecha);
        }
    }

    /*
     * Crea los botones de horarios de forma dinámica.
     * El usuario debe elegir un horario para avanzar.
     */
    private void configurarHorarios() {
        GridLayout layoutHorarios = findViewById(R.id.layoutHorariosDinamico);

        String[] horarios = {
                "08:00",
                "09:00",
                "12:00",
                "13:00",
                "14:00",
                "15:00",
                "16:00",
                "17:00"
        };

        for (int i = 0; i < horarios.length; i++) {
            Button btnHora = new Button(this);

            String horarioActual = horarios[i];

            btnHora.setText(horarioActual);

            GridLayout.LayoutParams paramsGrid =
                    new GridLayout.LayoutParams();

            paramsGrid.width = GridLayout.LayoutParams.WRAP_CONTENT;
            paramsGrid.height = GridLayout.LayoutParams.WRAP_CONTENT;
            paramsGrid.setMargins(10, 10, 10, 10);

            btnHora.setLayoutParams(paramsGrid);

            // Estado inicial de los horarios.
            btnHora.setBackgroundColor(Color.parseColor("#2A2A2A"));
            btnHora.setTextColor(Color.parseColor("#D4AF37"));

            btnHora.setOnClickListener(v -> {
                /*
                 * Se restablecen todos los horarios antes de marcar
                 * el que el usuario acaba de seleccionar.
                 */
                for (int j = 0; j < layoutHorarios.getChildCount(); j++) {
                    Button botonHermano =
                            (Button) layoutHorarios.getChildAt(j);

                    botonHermano.setBackgroundColor(
                            Color.parseColor("#2A2A2A")
                    );

                    botonHermano.setTextColor(
                            Color.parseColor("#D4AF37")
                    );
                }

                // Se destaca el horario seleccionado.
                btnHora.setBackgroundColor(Color.parseColor("#D4AF37"));
                btnHora.setTextColor(Color.BLACK);

                // Se almacena el horario elegido.
                horarioSeleccionado = horarioActual;
            });

            layoutHorarios.addView(btnHora);
        }
    }

    /*
     * Configura las tarjetas de servicios que ya están creadas en el XML.
     * Cada tarjeta se vincula con su nombre y precio correspondientes.
     */
    private void configurarServicios() {
        LinearLayout layoutServicios =
                findViewById(R.id.layoutServiciosDinamico);

        String[] servicios = {
                "Corte",
                "Corte + Barba",
                "Color",
                "Corte + Color",
                "Corte + Barba + Color"
        };

        String[] precios = {
                "$10.000",
                "$14.000",
                "$15.000",
                "$22.000",
                "$25.000"
        };

        for (int i = 0; i < layoutServicios.getChildCount(); i++) {
            View servicioCard = layoutServicios.getChildAt(i);

            /*
             * Se guarda el índice porque cada tarjeta debe recuperar
             * el nombre y precio que coinciden con su posición.
             */
            int indiceServicio = i;

            servicioCard.setOnClickListener(v -> {
                // Se reinician visualmente todas las tarjetas.
                for (int j = 0; j < layoutServicios.getChildCount(); j++) {
                    layoutServicios.getChildAt(j).setBackgroundColor(
                            Color.parseColor("#2A2A2A")
                    );
                }

                // Se resalta la tarjeta elegida.
                v.setBackgroundColor(Color.parseColor("#D4AF37"));

                // Se guardan los datos del servicio seleccionado.
                servicioSeleccionado = servicios[indiceServicio];
                precioSeleccionado = precios[indiceServicio];
            });
        }
    }

    /*
     * Valida que el usuario haya elegido horario y servicio.
     * Luego envía todos los datos de la reserva a ResumenTurnoActivity.
     */
    private void configurarBotonConfirmar() {
        Button btnConfirmar = findViewById(R.id.btnConfirmarTurno);

        btnConfirmar.setOnClickListener(v -> {

            boolean faltaHorario = horarioSeleccionado.isEmpty();
            boolean faltaServicio = servicioSeleccionado.isEmpty();

            if (faltaHorario || faltaServicio) {
                Toast.makeText(
                        this,
                        getString(R.string.mensaje_seleccion_incompleta),
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            /*
             * Intent permite abrir otra Activity.
             * Los extras transportan los datos reales de la reserva.
             */
            Intent intent = new Intent(
                    MainActivity.this,
                    ResumenTurnoActivity.class
            );

            intent.putExtra("SERVICIO", servicioSeleccionado);
            intent.putExtra("PRECIO", precioSeleccionado);
            intent.putExtra("FECHA", fechaSeleccionada);
            intent.putExtra("HORARIO", horarioSeleccionado);

            startActivity(intent);
        });
    }
}
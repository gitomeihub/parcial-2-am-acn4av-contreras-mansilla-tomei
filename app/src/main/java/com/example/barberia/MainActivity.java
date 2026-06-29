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
     * Datos elegidos para la reserva.
     * Se enviarán luego a ResumenTurnoActivity.
     */
    private String fechaSeleccionada = "";
    private String horarioSeleccionado = "";
    private String servicioSeleccionado = "";
    private String precioSeleccionado = "";

    /*
     * Datos del cliente recibidos desde DatosClienteActivity.
     * Acompañan el turno hasta que se guarda en Mis turnos.
     */
    private String nombreCliente = "";
    private String dniCliente = "";
    private String telefonoCliente = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula esta activity con la pantalla activity_main.xml.
        setContentView(R.layout.activity_main);

        /*
         * Recupera los datos enviados desde DatosClienteActivity.
         */
        Intent intentRecibido = getIntent();

        nombreCliente = intentRecibido.getStringExtra("NOMBRE_CLIENTE");
        dniCliente = intentRecibido.getStringExtra("DNI_CLIENTE");
        telefonoCliente = intentRecibido.getStringExtra("TELEFONO_CLIENTE");

        /*
         * Valores de respaldo para evitar errores si la pantalla
         * se abre directamente durante una prueba.
         */
        if (nombreCliente == null) {
            nombreCliente = "";
        }

        if (dniCliente == null) {
            dniCliente = "";
        }

        if (telefonoCliente == null) {
            telefonoCliente = "";
        }

        // Configura la navegación y las opciones de reserva.
        configurarBotonVolverInicio();
        configurarFechas();
        configurarHorarios();
        configurarServicios();
        configurarBotonConfirmar();
    }

    /*
     * Regresa directamente a InicioActivity.
     * Los flags evitan que se acumulen pantallas en el historial.
     */
    private void configurarBotonVolverInicio() {
        Button btnVolverInicio = findViewById(R.id.btnVolverInicio);

        btnVolverInicio.setOnClickListener(v -> {
            Intent intentInicio = new Intent(
                    MainActivity.this,
                    InicioActivity.class
            );

            intentInicio.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intentInicio);
            finish();
        });
    }

    /*
     * Crea las fechas disponibles de forma dinámica.
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

        // La primera fecha inicia marcada visualmente.
        fechaSeleccionada = fechas[0].replace("\n", " ");

        for (int i = 0; i < fechas.length; i++) {
            Button btnFecha = new Button(this);

            // Versión legible que luego se mostrará en el resumen.
            String fechaActual = fechas[i].replace("\n", " ");

            btnFecha.setText(fechas[i]);
            btnFecha.setAllCaps(false);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(160, 200);

            params.setMargins(0, 0, 20, 0);
            btnFecha.setLayoutParams(params);

            // La primera fecha empieza seleccionada.
            if (i == 0) {
                btnFecha.setBackgroundColor(Color.parseColor("#D4AF37"));
                btnFecha.setTextColor(Color.BLACK);
            } else {
                btnFecha.setBackgroundColor(Color.parseColor("#2A2A2A"));
                btnFecha.setTextColor(Color.WHITE);
            }

            btnFecha.setOnClickListener(v -> {
                // Restaura visualmente todas las fechas.
                for (int j = 0; j < layoutFechas.getChildCount(); j++) {
                    Button botonHermano =
                            (Button) layoutFechas.getChildAt(j);

                    botonHermano.setBackgroundColor(
                            Color.parseColor("#2A2A2A")
                    );

                    botonHermano.setTextColor(Color.WHITE);
                }

                // Resalta la fecha elegida.
                btnFecha.setBackgroundColor(Color.parseColor("#D4AF37"));
                btnFecha.setTextColor(Color.BLACK);

                // Guarda la selección.
                fechaSeleccionada = fechaActual;
            });

            layoutFechas.addView(btnFecha);
        }
    }

    /*
     * Crea los horarios disponibles de forma dinámica.
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
                // Restaura todos los horarios antes de marcar uno.
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

                // Resalta y guarda el horario elegido.
                btnHora.setBackgroundColor(Color.parseColor("#D4AF37"));
                btnHora.setTextColor(Color.BLACK);

                horarioSeleccionado = horarioActual;
            });

            layoutHorarios.addView(btnHora);
        }
    }

    /*
     * Configura las tarjetas de servicios ya definidas en el XML.
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

            // Guarda el índice para identificar servicio y precio.
            int indiceServicio = i;

            servicioCard.setOnClickListener(v -> {
                // Restaura todas las tarjetas.
                for (int j = 0; j < layoutServicios.getChildCount(); j++) {
                    layoutServicios.getChildAt(j).setBackgroundColor(
                            Color.parseColor("#2A2A2A")
                    );
                }

                // Destaca la tarjeta seleccionada.
                v.setBackgroundColor(Color.parseColor("#D4AF37"));

                // Guarda servicio y precio elegidos.
                servicioSeleccionado = servicios[indiceServicio];
                precioSeleccionado = precios[indiceServicio];
            });
        }
    }

    /*
     * Valida las elecciones y envía todos los datos al resumen.
     */
    private void configurarBotonConfirmar() {
        Button btnConfirmar = findViewById(R.id.btnConfirmarTurno);

        btnConfirmar.setOnClickListener(v -> {

            boolean faltanDatosCliente =
                    nombreCliente.isEmpty()
                            || dniCliente.isEmpty()
                            || telefonoCliente.isEmpty();

            boolean faltaHorario = horarioSeleccionado.isEmpty();
            boolean faltaServicio = servicioSeleccionado.isEmpty();

            /*
             * El flujo normal obliga a cargar datos antes de reservar.
             * Esta validación evita confirmar un turno sin cliente.
             */
            if (faltanDatosCliente) {
                Toast.makeText(
                        this,
                        getString(R.string.mensaje_error_datos_cliente),
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            if (faltaHorario || faltaServicio) {
                Toast.makeText(
                        this,
                        getString(R.string.mensaje_seleccion_incompleta),
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            /*
             * Abre la pantalla resumen y transporta cliente + turno
             * mediante Intent extras.
             */
            Intent intentResumen = new Intent(
                    MainActivity.this,
                    ResumenTurnoActivity.class
            );

            intentResumen.putExtra("NOMBRE_CLIENTE", nombreCliente);
            intentResumen.putExtra("DNI_CLIENTE", dniCliente);
            intentResumen.putExtra("TELEFONO_CLIENTE", telefonoCliente);

            intentResumen.putExtra("SERVICIO", servicioSeleccionado);
            intentResumen.putExtra("PRECIO", precioSeleccionado);
            intentResumen.putExtra("FECHA", fechaSeleccionada);
            intentResumen.putExtra("HORARIO", horarioSeleccionado);

            startActivity(intentResumen);
        });
    }
}
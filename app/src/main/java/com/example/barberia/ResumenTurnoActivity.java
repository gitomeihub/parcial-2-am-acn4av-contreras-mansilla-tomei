package com.example.barberia;
import android.widget.RadioGroup;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.RadioGroup;
import android.widget.RadioGroup;

public class ResumenTurnoActivity extends AppCompatActivity {

    /*
     * Constantes utilizadas para guardar y recuperar los turnos
     * desde SharedPreferences.
     */
    private static final String PREFS_NAME = "barberia_prefs";
    private static final String KEY_TURNOS = "turnos_guardados";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula esta activity con activity_resumen_turno.xml.
        setContentView(R.layout.activity_resumen_turno);

        // Referencias a los datos del cliente.
        TextView txtNombreClienteResumen =
                findViewById(R.id.txtNombreClienteResumen);

        TextView txtDniClienteResumen =
                findViewById(R.id.txtDniClienteResumen);

        TextView txtTelefonoClienteResumen =
                findViewById(R.id.txtTelefonoClienteResumen);

        // Referencias a los datos de la reserva.
        TextView txtServicioResumen =
                findViewById(R.id.txtServicioResumen);

        TextView txtFechaResumen =
                findViewById(R.id.txtFechaResumen);

        TextView txtHorarioResumen =
                findViewById(R.id.txtHorarioResumen);

        TextView txtTotalResumen =
                findViewById(R.id.txtTotalResumen);

        // Botones de navegación y confirmación.
        Button btnVolverReserva =
                findViewById(R.id.btnVolverReserva);

        Button btnConfirmarReserva =
                findViewById(R.id.btnConfirmarReserva);
        // Grupo que contiene las opciones de método de pago.
        RadioGroup radioGroupMedioPago =
                findViewById(R.id.radioGroupMedioPago);

        /*
         * Recupera todos los datos enviados desde MainActivity:
         * cliente y detalle de la reserva.
         */
        Intent intentRecibido = getIntent();

        String nombreCliente =
                intentRecibido.getStringExtra("NOMBRE_CLIENTE");

        String dniCliente =
                intentRecibido.getStringExtra("DNI_CLIENTE");

        String telefonoCliente =
                intentRecibido.getStringExtra("TELEFONO_CLIENTE");

        String servicio =
                intentRecibido.getStringExtra("SERVICIO");

        String precio =
                intentRecibido.getStringExtra("PRECIO");

        String fecha =
                intentRecibido.getStringExtra("FECHA");

        String horario =
                intentRecibido.getStringExtra("HORARIO");

        /*
         * Valores de respaldo para evitar que la pantalla quede vacía
         * si se abre directamente desde Android Studio durante una prueba.
         */
        if (nombreCliente == null || nombreCliente.isEmpty()) {
            nombreCliente = getString(R.string.valor_cliente_ejemplo);
        }

        if (dniCliente == null || dniCliente.isEmpty()) {
            dniCliente = getString(R.string.valor_dni_ejemplo);
        }

        if (telefonoCliente == null || telefonoCliente.isEmpty()) {
            telefonoCliente = getString(R.string.valor_telefono_ejemplo);
        }

        if (servicio == null || servicio.isEmpty()) {
            servicio = getString(R.string.valor_servicio_ejemplo);
        }

        if (precio == null || precio.isEmpty()) {
            precio = getString(R.string.valor_total_ejemplo);
        }

        if (fecha == null || fecha.isEmpty()) {
            fecha = getString(R.string.valor_fecha_ejemplo);
        }

        if (horario == null || horario.isEmpty()) {
            horario = getString(R.string.valor_horario_ejemplo);
        }

        // Muestra todos los datos en la pantalla.
        txtNombreClienteResumen.setText(nombreCliente);
        txtDniClienteResumen.setText(dniCliente);
        txtTelefonoClienteResumen.setText(telefonoCliente);

        txtServicioResumen.setText(servicio);
        txtFechaResumen.setText(fecha);
        txtHorarioResumen.setText(horario);
        txtTotalResumen.setText(precio);

        /*
         * Copias finales para poder usar los valores dentro
         * del evento Confirmar reserva.
         */
        final String nombreFinal = nombreCliente;
        final String dniFinal = dniCliente;
        final String telefonoFinal = telefonoCliente;

        final String servicioFinal = servicio;
        final String precioFinal = precio;
        final String fechaFinal = fecha;
        final String horarioFinal = horario;

        // Regresa a Reserva para modificar las elecciones.
        btnVolverReserva.setOnClickListener(v -> finish());

        /*
         * Guarda el turno completo y abre Mis turnos.
         */
        btnConfirmarReserva.setOnClickListener(v -> {
            /*
             * Verifica que el usuario haya seleccionado una opción de pago
             * antes de confirmar el turno.
             */
            if (radioGroupMedioPago.getCheckedRadioButtonId() == -1) {
                Toast.makeText(
                        ResumenTurnoActivity.this,
                        getString(R.string.mensaje_error_medio_pago),
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            boolean turnoGuardado = guardarTurno(
                    nombreFinal,
                    dniFinal,
                    telefonoFinal,
                    servicioFinal,
                    precioFinal,
                    fechaFinal,
                    horarioFinal
            );

            if (turnoGuardado) {
                Toast.makeText(
                        ResumenTurnoActivity.this,
                        getString(R.string.mensaje_turno_confirmado),
                        Toast.LENGTH_SHORT
                ).show();

                Intent intentMisTurnos = new Intent(
                        ResumenTurnoActivity.this,
                        MisTurnosActivity.class
                );

                startActivity(intentMisTurnos);

                // Evita volver al resumen después de confirmar.
                finish();
            }
        });
    }

    /*
     * Guarda un turno completo dentro de SharedPreferences.
     * Incluye la información del cliente y de la reserva.
     */
    private boolean guardarTurno(
            String nombreCliente,
            String dniCliente,
            String telefonoCliente,
            String servicio,
            String precio,
            String fecha,
            String horario
    ) {
        SharedPreferences preferencias = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        String turnosJson = preferencias.getString(
                KEY_TURNOS,
                "[]"
        );

        try {
            // Recupera la lista existente de turnos.
            JSONArray listaTurnos = new JSONArray(turnosJson);

            // Crea el nuevo turno.
            JSONObject nuevoTurno = new JSONObject();

            nuevoTurno.put("nombreCliente", nombreCliente);
            nuevoTurno.put("dniCliente", dniCliente);
            nuevoTurno.put("telefonoCliente", telefonoCliente);

            nuevoTurno.put("servicio", servicio);
            nuevoTurno.put("precio", precio);
            nuevoTurno.put("fecha", fecha);
            nuevoTurno.put("horario", horario);

            // Agrega el turno sin borrar los anteriores.
            listaTurnos.put(nuevoTurno);

            // Guarda la lista actualizada localmente.
            preferencias.edit()
                    .putString(KEY_TURNOS, listaTurnos.toString())
                    .apply();

            return true;

        } catch (JSONException e) {
            Toast.makeText(
                    this,
                    "No se pudo guardar el turno. Intentá nuevamente.",
                    Toast.LENGTH_LONG
            ).show();

            return false;
        }
    }
}
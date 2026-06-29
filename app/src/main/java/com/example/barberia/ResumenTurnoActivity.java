package com.example.barberia;

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

public class ResumenTurnoActivity extends AppCompatActivity {

    /*
     * Datos para guardar los turnos localmente en el dispositivo.
     * SharedPreferences funciona como un pequeño almacenamiento interno
     * de la aplicación.
     */
    private static final String PREFS_NAME = "barberia_prefs";
    private static final String KEY_TURNOS = "turnos_guardados";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula esta activity con activity_resumen_turno.xml.
        setContentView(R.layout.activity_resumen_turno);

        // Referencias a los TextView donde se mostrarán los datos recibidos.
        TextView txtServicioResumen = findViewById(R.id.txtServicioResumen);
        TextView txtFechaResumen = findViewById(R.id.txtFechaResumen);
        TextView txtHorarioResumen = findViewById(R.id.txtHorarioResumen);
        TextView txtTotalResumen = findViewById(R.id.txtTotalResumen);

        // Referencias a los botones de la pantalla.
        Button btnVolverReserva = findViewById(R.id.btnVolverReserva);
        Button btnConfirmarReserva = findViewById(R.id.btnConfirmarReserva);

        /*
         * Recibe los datos enviados desde MainActivity mediante Intent extras.
         */
        Intent intentRecibido = getIntent();

        String servicio = intentRecibido.getStringExtra("SERVICIO");
        String precio = intentRecibido.getStringExtra("PRECIO");
        String fecha = intentRecibido.getStringExtra("FECHA");
        String horario = intentRecibido.getStringExtra("HORARIO");

        /*
         * Valores de respaldo.
         * Evitan que la pantalla se vea vacía si se abre directamente
         * durante una prueba.
         */
        if (servicio == null) {
            servicio = getString(R.string.valor_servicio_ejemplo);
        }

        if (precio == null) {
            precio = getString(R.string.valor_total_ejemplo);
        }

        if (fecha == null) {
            fecha = getString(R.string.valor_fecha_ejemplo);
        }

        if (horario == null) {
            horario = getString(R.string.valor_horario_ejemplo);
        }

        // Se muestran los datos reales elegidos por el usuario.
        txtServicioResumen.setText(servicio);
        txtFechaResumen.setText(fecha);
        txtHorarioResumen.setText(horario);
        txtTotalResumen.setText(precio);

        /*
         * Copias finales para poder utilizar los datos dentro
         * del evento del botón Confirmar reserva.
         */
        final String servicioFinal = servicio;
        final String precioFinal = precio;
        final String fechaFinal = fecha;
        final String horarioFinal = horario;

        /*
         * Regresa a la pantalla de reserva para modificar
         * fecha, horario o servicio.
         */
        btnVolverReserva.setOnClickListener(v -> finish());

        /*
         * Guarda el turno confirmado y abre la pantalla Mis turnos.
         */
        btnConfirmarReserva.setOnClickListener(v -> {

            boolean turnoGuardado = guardarTurno(
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

                /*
                 * Evita que, después de confirmar, el usuario vuelva
                 * al resumen con el botón físico de atrás.
                 */
                finish();
            }
        });
    }

    /*
     * Guarda un nuevo turno dentro de SharedPreferences.
     *
     * Se utiliza un JSONArray porque permite conservar más de un turno.
     * Cada turno se guarda como un JSONObject con servicio, precio,
     * fecha y horario.
     */
    private boolean guardarTurno(
            String servicio,
            String precio,
            String fecha,
            String horario
    ) {
        SharedPreferences preferencias = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        String turnosJson = preferencias.getString(KEY_TURNOS, "[]");

        try {
            // Recupera los turnos existentes o crea una lista vacía.
            JSONArray listaTurnos = new JSONArray(turnosJson);

            // Crea el nuevo turno que se agregará a la lista.
            JSONObject nuevoTurno = new JSONObject();
            nuevoTurno.put("servicio", servicio);
            nuevoTurno.put("precio", precio);
            nuevoTurno.put("fecha", fecha);
            nuevoTurno.put("horario", horario);

            // Agrega el turno nuevo sin borrar los anteriores.
            listaTurnos.put(nuevoTurno);

            // Guarda nuevamente toda la lista actualizada.
            preferencias.edit()
                    .putString(KEY_TURNOS, listaTurnos.toString())
                    .apply();

            return true;

        } catch (JSONException e) {
            /*
             * Si hubiera un problema al crear o leer el JSON,
             * se informa y no se abre la pantalla de turnos.
             */
            Toast.makeText(
                    this,
                    "No se pudo guardar el turno. Intentá nuevamente.",
                    Toast.LENGTH_LONG
            ).show();

            return false;
        }
    }
}
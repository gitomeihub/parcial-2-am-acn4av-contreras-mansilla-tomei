package com.example.barberia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ResumenTurnoActivity extends AppCompatActivity {
    private FirebaseFirestore db;
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

        db = FirebaseFirestore.getInstance();

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

        /*
         * Grupo de opciones de pago.
         * Este ID se agregará en activity_resumen_turno.xml.
         */
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
         * Copias finales para usar los valores dentro
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
         * Valida el método de pago, guarda el turno completo
         * y abre la pantalla Mis turnos.
         */
        btnConfirmarReserva.setOnClickListener(v -> {

            /*
             * Verifica que el usuario haya elegido una forma de pago.
             */
            if (radioGroupMedioPago.getCheckedRadioButtonId() == -1) {
                Toast.makeText(
                        ResumenTurnoActivity.this,
                        getString(R.string.mensaje_error_medio_pago),
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            /*
             * Obtiene el texto de la opción elegida:
             * Efectivo, Transferencia o Mercado Pago.
             */
            RadioButton radioPagoSeleccionado = findViewById(
                    radioGroupMedioPago.getCheckedRadioButtonId()
            );

            String medioPago = radioPagoSeleccionado
                    .getText()
                    .toString();

            boolean turnoGuardado = guardarTurno(
                    nombreFinal,
                    dniFinal,
                    telefonoFinal,
                    servicioFinal,
                    precioFinal,
                    fechaFinal,
                    horarioFinal,
                    medioPago
            );
            if (turnoGuardado) {
                // 1. Armamos el paquete de datos para Firebase usando las variables reales
                Map<String, Object> turno = new HashMap<>();
                turno.put("servicio", servicioFinal);
                turno.put("fecha", fechaFinal);
                turno.put("hora", horarioFinal);
                turno.put("metodoPago", medioPago);
                turno.put("estado", "Reservado");
                turno.put("fechaCreacion", FieldValue.serverTimestamp());

                // 2. Lo enviamos a la colección "turnos" en la nube
                db.collection("turnos")
                        .add(turno)
                        .addOnSuccessListener(documentReference -> {
                            // 3. Si Firebase guardó bien, mostramos el mensaje y cambiamos de pantalla (el código de tus compañeras)
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
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            // 4. Si falla la subida, avisamos
                            Toast.makeText(
                                    ResumenTurnoActivity.this,
                                    "No se pudo guardar el turno en la nube",
                                    Toast.LENGTH_SHORT
                            ).show();
                        });
            }
        });
    }

    /*
     * Guarda un turno completo dentro de SharedPreferences.
     * Incluye cliente, reserva y método de pago.
     */
    private boolean guardarTurno(
            String nombreCliente,
            String dniCliente,
            String telefonoCliente,
            String servicio,
            String precio,
            String fecha,
            String horario,
            String medioPago
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

            // Guarda también el método de pago seleccionado.
            nuevoTurno.put("medioPago", medioPago);

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
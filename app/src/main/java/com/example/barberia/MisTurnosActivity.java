package com.example.barberia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MisTurnosActivity extends AppCompatActivity {

    /*
     * Estos nombres deben coincidir con los utilizados
     * en ResumenTurnoActivity para recuperar los turnos guardados.
     */
    private static final String PREFS_NAME = "barberia_prefs";
    private static final String KEY_TURNOS = "turnos_guardados";

    // Elementos que se modifican según haya o no turnos guardados.
    private LinearLayout layoutEstadoVacio;
    private LinearLayout layoutTurnosDinamico;
    private TextView txtTituloListaTurnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula esta activity con el layout activity_mis_turnos.xml.
        setContentView(R.layout.activity_mis_turnos);

        // Referencias a los elementos definidos en el XML.
        Button btnVolverInicio =
                findViewById(R.id.btnVolverInicioMisTurnos);

        layoutEstadoVacio =
                findViewById(R.id.layoutEstadoVacio);

        layoutTurnosDinamico =
                findViewById(R.id.layoutTurnosDinamico);

        txtTituloListaTurnos =
                findViewById(R.id.txtTituloListaTurnos);

        // Carga todos los turnos guardados localmente.
        cargarTurnosGuardados();

        /*
         * Regresa al inicio sin acumular pantallas repetidas.
         */
        btnVolverInicio.setOnClickListener(v -> {
            Intent intentInicio = new Intent(
                    MisTurnosActivity.this,
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
     * Recupera los turnos desde SharedPreferences y crea
     * una tarjeta dinámica para cada reserva encontrada.
     */
    private void cargarTurnosGuardados() {
        SharedPreferences preferencias = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        String turnosJson = preferencias.getString(
                KEY_TURNOS,
                "[]"
        );

        // Evita duplicar tarjetas al actualizar la pantalla.
        layoutTurnosDinamico.removeAllViews();

        try {
            JSONArray listaTurnos = new JSONArray(turnosJson);

            /*
             * Si no hay turnos, se muestra el estado vacío.
             */
            if (listaTurnos.length() == 0) {
                layoutEstadoVacio.setVisibility(View.VISIBLE);
                txtTituloListaTurnos.setVisibility(View.GONE);
                return;
            }

            // Si hay reservas, se muestra el listado.
            layoutEstadoVacio.setVisibility(View.GONE);
            txtTituloListaTurnos.setVisibility(View.VISIBLE);

            for (int i = 0; i < listaTurnos.length(); i++) {
                JSONObject turno = listaTurnos.getJSONObject(i);

                /*
                 * optString permite que también se puedan leer
                 * turnos viejos que se hubieran guardado antes
                 * de agregar datos del cliente o medio de pago.
                 */
                String nombreCliente = turno.optString(
                        "nombreCliente",
                        "Cliente no informado"
                );

                String dniCliente = turno.optString(
                        "dniCliente",
                        "-"
                );

                String telefonoCliente = turno.optString(
                        "telefonoCliente",
                        "-"
                );

                String servicio = turno.optString("servicio", "");
                String precio = turno.optString("precio", "");
                String fecha = turno.optString("fecha", "");
                String horario = turno.optString("horario", "");

                /*
                 * Recupera el medio de pago.
                 * Los turnos creados antes de esta mejora mostrarán
                 * "No informado" en lugar de romper la app.
                 */
                String medioPago = turno.optString(
                        "medioPago",
                        "No informado"
                );

                /*
                 * Se conserva el índice del turno para poder eliminar
                 * exactamente la reserva seleccionada.
                 */
                int indiceTurno = i;

                agregarTarjetaTurno(
                        nombreCliente,
                        dniCliente,
                        telefonoCliente,
                        servicio,
                        precio,
                        fecha,
                        horario,
                        medioPago,
                        indiceTurno
                );
            }

        } catch (JSONException e) {
            layoutEstadoVacio.setVisibility(View.VISIBLE);
            txtTituloListaTurnos.setVisibility(View.GONE);

            Toast.makeText(
                    this,
                    "No se pudieron cargar los turnos guardados.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    /*
     * Crea visualmente una tarjeta para un turno confirmado.
     */
    private void agregarTarjetaTurno(
            String nombreCliente,
            String dniCliente,
            String telefonoCliente,
            String servicio,
            String precio,
            String fecha,
            String horario,
            String medioPago,
            int indiceTurno
    ) {
        // Contenedor principal de la tarjeta.
        LinearLayout tarjetaTurno = new LinearLayout(this);

        tarjetaTurno.setOrientation(LinearLayout.VERTICAL);
        tarjetaTurno.setPadding(dp(20), dp(20), dp(20), dp(20));
        tarjetaTurno.setBackgroundColor(Color.parseColor("#2A2A2A"));

        LinearLayout.LayoutParams parametrosTarjeta =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametrosTarjeta.setMargins(0, 0, 0, dp(18));
        tarjetaTurno.setLayoutParams(parametrosTarjeta);

        // Nombre del servicio elegido.
        TextView txtServicio = new TextView(this);

        txtServicio.setText(servicio);
        txtServicio.setTextColor(Color.WHITE);
        txtServicio.setTextSize(20);
        txtServicio.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        // Datos de la persona a la que pertenece el turno.
        TextView txtCliente = new TextView(this);

        txtCliente.setText(
                getString(
                        R.string.detalle_cliente_turno,
                        nombreCliente,
                        dniCliente,
                        telefonoCliente
                )
        );

        txtCliente.setTextColor(Color.parseColor("#C8C8C8"));
        txtCliente.setTextSize(14);

        LinearLayout.LayoutParams parametrosCliente =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametrosCliente.setMargins(0, dp(12), 0, 0);
        txtCliente.setLayoutParams(parametrosCliente);

        // Fecha y horario del turno.
        TextView txtFechaHorario = new TextView(this);

        txtFechaHorario.setText(
                getString(
                        R.string.detalle_fecha_horario_turno,
                        fecha,
                        horario
                )
        );

        txtFechaHorario.setTextColor(Color.parseColor("#C8C8C8"));
        txtFechaHorario.setTextSize(15);

        LinearLayout.LayoutParams parametrosFecha =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametrosFecha.setMargins(0, dp(12), 0, 0);
        txtFechaHorario.setLayoutParams(parametrosFecha);

        // Precio total del turno.
        TextView txtPrecio = new TextView(this);

        txtPrecio.setText(
                getString(
                        R.string.detalle_total_turno,
                        precio
                )
        );

        txtPrecio.setTextColor(Color.parseColor("#D4AF37"));
        txtPrecio.setTextSize(18);
        txtPrecio.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        LinearLayout.LayoutParams parametrosPrecio =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametrosPrecio.setMargins(0, dp(12), 0, 0);
        txtPrecio.setLayoutParams(parametrosPrecio);

        /*
         * Muestra el método elegido y deja claro que el pago
         * todavía se realiza o confirma al asistir.
         */
        TextView txtMedioPago = new TextView(this);

        txtMedioPago.setText(
                getString(
                        R.string.detalle_pago_turno,
                        medioPago
                )
        );

        txtMedioPago.setTextColor(Color.parseColor("#C8C8C8"));
        txtMedioPago.setTextSize(14);

        LinearLayout.LayoutParams parametrosMedioPago =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametrosMedioPago.setMargins(0, dp(12), 0, 0);
        txtMedioPago.setLayoutParams(parametrosMedioPago);

        // Botón para cancelar solamente este turno.
        Button btnCancelarTurno = new Button(this);

        btnCancelarTurno.setText(R.string.boton_cancelar_turno);
        btnCancelarTurno.setAllCaps(false);
        btnCancelarTurno.setTextColor(Color.WHITE);
        btnCancelarTurno.setBackgroundColor(Color.parseColor("#444444"));

        LinearLayout.LayoutParams parametrosBoton =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametrosBoton.setMargins(0, dp(18), 0, 0);
        btnCancelarTurno.setLayoutParams(parametrosBoton);

        /*
         * Elimina el turno seleccionado del almacenamiento
         * y actualiza la lista de reservas.
         */
        btnCancelarTurno.setOnClickListener(v -> {
            eliminarTurno(indiceTurno);

            Toast.makeText(
                    MisTurnosActivity.this,
                    "Turno cancelado correctamente.",
                    Toast.LENGTH_SHORT
            ).show();

            cargarTurnosGuardados();
        });

        // Agrega los componentes dentro de la tarjeta.
        tarjetaTurno.addView(txtServicio);
        tarjetaTurno.addView(txtCliente);
        tarjetaTurno.addView(txtFechaHorario);
        tarjetaTurno.addView(txtPrecio);
        tarjetaTurno.addView(txtMedioPago);
        tarjetaTurno.addView(btnCancelarTurno);

        // Agrega la tarjeta a la lista dinámica de turnos.
        layoutTurnosDinamico.addView(tarjetaTurno);
    }

    /*
     * Elimina un turno según su posición dentro del JSONArray guardado.
     */
    private void eliminarTurno(int indiceTurno) {
        SharedPreferences preferencias = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        String turnosJson = preferencias.getString(
                KEY_TURNOS,
                "[]"
        );

        try {
            JSONArray listaTurnos = new JSONArray(turnosJson);

            if (indiceTurno >= 0 && indiceTurno < listaTurnos.length()) {
                listaTurnos.remove(indiceTurno);

                preferencias.edit()
                        .putString(
                                KEY_TURNOS,
                                listaTurnos.toString()
                        )
                        .apply();
            }

        } catch (JSONException e) {
            Toast.makeText(
                    this,
                    "No se pudo cancelar el turno.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    /*
     * Convierte dp a píxeles para que márgenes y padding
     * se adapten a la densidad del dispositivo.
     */
    private int dp(int cantidadDp) {
        float densidad = getResources()
                .getDisplayMetrics()
                .density;

        return (int) (cantidadDp * densidad);
    }
}
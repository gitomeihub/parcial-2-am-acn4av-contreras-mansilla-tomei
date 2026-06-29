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
     * Deben coincidir exactamente con los nombres usados
     * en ResumenTurnoActivity para poder recuperar los turnos guardados.
     */
    private static final String PREFS_NAME = "barberia_prefs";
    private static final String KEY_TURNOS = "turnos_guardados";

    // Elementos que cambian según existan o no reservas guardadas.
    private LinearLayout layoutEstadoVacio;
    private LinearLayout layoutTurnosDinamico;
    private TextView txtTituloListaTurnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula esta activity con activity_mis_turnos.xml.
        setContentView(R.layout.activity_mis_turnos);

        // Referencias a los elementos definidos en el XML.
        Button btnVolverInicio = findViewById(R.id.btnVolverInicioMisTurnos);

        layoutEstadoVacio = findViewById(R.id.layoutEstadoVacio);
        layoutTurnosDinamico = findViewById(R.id.layoutTurnosDinamico);
        txtTituloListaTurnos = findViewById(R.id.txtTituloListaTurnos);

        /*
         * Carga los turnos guardados localmente y crea una tarjeta
         * por cada reserva encontrada.
         */
        cargarTurnosGuardados();

        /*
         * Regresa a la pantalla de inicio.
         * Los flags evitan que se acumulen pantallas repetidas.
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
     * Recupera la lista de turnos desde SharedPreferences
     * y actualiza visualmente la pantalla.
     */
    private void cargarTurnosGuardados() {
        SharedPreferences preferencias = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        String turnosJson = preferencias.getString(KEY_TURNOS, "[]");

        // Limpia las tarjetas anteriores antes de volver a cargarlas.
        layoutTurnosDinamico.removeAllViews();

        try {
            JSONArray listaTurnos = new JSONArray(turnosJson);

            /*
             * Si no existen turnos, se muestra el mensaje de estado vacío.
             */
            if (listaTurnos.length() == 0) {
                layoutEstadoVacio.setVisibility(View.VISIBLE);
                txtTituloListaTurnos.setVisibility(View.GONE);
                return;
            }

            /*
             * Si hay turnos, se oculta el mensaje vacío y se muestra
             * el título de la lista.
             */
            layoutEstadoVacio.setVisibility(View.GONE);
            txtTituloListaTurnos.setVisibility(View.VISIBLE);

            /*
             * Se recorre cada turno guardado y se genera su tarjeta.
             */
            for (int i = 0; i < listaTurnos.length(); i++) {
                JSONObject turno = listaTurnos.getJSONObject(i);

                String servicio = turno.getString("servicio");
                String precio = turno.getString("precio");
                String fecha = turno.getString("fecha");
                String horario = turno.getString("horario");

                /*
                 * Se conserva la posición del turno para saber cuál
                 * eliminar cuando se toque el botón Cancelar turno.
                 */
                int indiceTurno = i;

                agregarTarjetaTurno(
                        servicio,
                        precio,
                        fecha,
                        horario,
                        indiceTurno
                );
            }

        } catch (JSONException e) {
            /*
             * Si hubiera un problema con la información guardada,
             * se informa al usuario y se muestra el estado vacío.
             */
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
     * Crea una tarjeta visual para un turno confirmado.
     */
    private void agregarTarjetaTurno(
            String servicio,
            String precio,
            String fecha,
            String horario,
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
        txtServicio.setTypeface(null, android.graphics.Typeface.BOLD);

        // Fecha y horario del turno.
        TextView txtFechaHorario = new TextView(this);
        txtFechaHorario.setText("Fecha: " + fecha + " · " + horario + " hs");
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
        txtPrecio.setText("Total: " + precio);
        txtPrecio.setTextColor(Color.parseColor("#D4AF37"));
        txtPrecio.setTextSize(18);
        txtPrecio.setTypeface(null, android.graphics.Typeface.BOLD);

        LinearLayout.LayoutParams parametrosPrecio =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametrosPrecio.setMargins(0, dp(12), 0, 0);
        txtPrecio.setLayoutParams(parametrosPrecio);

        // Botón para cancelar solamente este turno.
        Button btnCancelarTurno = new Button(this);
        btnCancelarTurno.setText("Cancelar turno");
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
         * Elimina el turno seleccionado del almacenamiento local
         * y vuelve a cargar la lista actualizada.
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

        // Se agregan los elementos dentro de la tarjeta.
        tarjetaTurno.addView(txtServicio);
        tarjetaTurno.addView(txtFechaHorario);
        tarjetaTurno.addView(txtPrecio);
        tarjetaTurno.addView(btnCancelarTurno);

        // La tarjeta se agrega a la lista dinámica.
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

        String turnosJson = preferencias.getString(KEY_TURNOS, "[]");

        try {
            JSONArray listaTurnos = new JSONArray(turnosJson);

            if (indiceTurno >= 0 && indiceTurno < listaTurnos.length()) {
                listaTurnos.remove(indiceTurno);

                preferencias.edit()
                        .putString(KEY_TURNOS, listaTurnos.toString())
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
     * Convierte medidas dp a píxeles para que los márgenes y padding
     * se adapten mejor a la densidad de cada pantalla.
     */
    private int dp(int cantidadDp) {
        float densidad = getResources().getDisplayMetrics().density;
        return (int) (cantidadDp * densidad);
    }
}
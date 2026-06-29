package com.example.barberia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DatosClienteActivity extends AppCompatActivity {

    // Campos donde el cliente ingresa sus datos personales.
    private EditText edtNombreCliente;
    private EditText edtDniCliente;
    private EditText edtTelefonoCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula la activity con el layout activity_datos_cliente.xml.
        setContentView(R.layout.activity_datos_cliente);

        // Referencias a los campos y botones definidos en el XML.
        edtNombreCliente = findViewById(R.id.edtNombreCliente);
        edtDniCliente = findViewById(R.id.edtDniCliente);
        edtTelefonoCliente = findViewById(R.id.edtTelefonoCliente);

        Button btnVolverInicio = findViewById(R.id.btnVolverInicioDatos);
        Button btnContinuarReserva = findViewById(R.id.btnContinuarReserva);

        /*
         * Cierra esta pantalla y vuelve a InicioActivity,
         * que es la activity anterior dentro del flujo.
         */
        btnVolverInicio.setOnClickListener(v -> finish());

        /*
         * Valida los datos del cliente y, si son correctos,
         * los envía a MainActivity para continuar con la reserva.
         */
        btnContinuarReserva.setOnClickListener(v -> {

            String nombre = edtNombreCliente.getText()
                    .toString()
                    .trim();

            String dni = edtDniCliente.getText()
                    .toString()
                    .trim();

            String telefono = edtTelefonoCliente.getText()
                    .toString()
                    .trim();

            /*
             * El teléfono puede contener espacios, guiones o paréntesis.
             * Para validarlo se conservan únicamente los números.
             */
            String telefonoNumerico = telefono.replaceAll("[^0-9]", "");

            boolean nombreValido = nombre.length() >= 3;
            boolean dniValido = dni.matches("\\d{7,8}");
            boolean telefonoValido = telefonoNumerico.length() >= 8;

            if (!nombreValido || !dniValido || !telefonoValido) {
                Toast.makeText(
                        DatosClienteActivity.this,
                        getString(R.string.mensaje_error_datos_cliente),
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            /*
             * Se abre la pantalla de reserva y se envían los datos
             * del cliente mediante Intent extras.
             */
            Intent intentReserva = new Intent(
                    DatosClienteActivity.this,
                    MainActivity.class
            );

            intentReserva.putExtra("NOMBRE_CLIENTE", nombre);
            intentReserva.putExtra("DNI_CLIENTE", dni);
            intentReserva.putExtra("TELEFONO_CLIENTE", telefono);

            startActivity(intentReserva);
        });
    }
}
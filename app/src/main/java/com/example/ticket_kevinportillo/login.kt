package com.example.ticket_kevinportillo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.conexion

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnIniciarSesion = findViewById<Button>(R.id.btnInicioSesion)
        val txtRegistrarse = findViewById<TextView>(R.id.txtRegistrarse)
        val txtUsuario = findViewById<EditText>(R.id.etNombre)
        val txtContrasena = findViewById<EditText>(R.id.etContrasena)

        txtRegistrarse.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnIniciarSesion.setOnClickListener {
            val pantallaTicket = Intent(this, Tickets::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                val objConexion = conexion().cadenaConexion()

                val comprobarUsuario =
                    objConexion?.prepareStatement("SELECT * FROM Usuarios WHERE usuario = ? AND contrasena = ?")!!
                comprobarUsuario.setString(1, txtUsuario.text.toString())
                comprobarUsuario.setString(2, txtContrasena.text.toString())

                val resultado = comprobarUsuario.executeQuery()
                if (resultado.next()) {
                    startActivity(pantallaTicket)
                } else {
                    println("No se encontro un usuario con este registro")
                }
            }
        }
    }
}
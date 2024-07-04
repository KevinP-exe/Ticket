package com.example.ticket_kevinportillo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.conexion
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txInicioSesion = findViewById<TextView>(R.id.txtiniciosesion)
        val btnRegistrar = findViewById<Button>(R.id.btnregistrarse)
        val etUsuario = findViewById<EditText>(R.id.etnombre)
        val etContrasena = findViewById<EditText>(R.id.etcontrasena)

        txInicioSesion.setOnClickListener {
            val intent: Intent = Intent(this, login::class.java)
            startActivity(intent)
        }

        btnRegistrar.setOnClickListener {
            val intent: Intent = Intent(this, Tickets::class.java)
            startActivity(intent)

            GlobalScope.launch(Dispatchers.IO) {
                val objConexion = conexion().cadenaConexion()

                val crearUsuario =
                    objConexion?.prepareStatement("INSERT INTO Usuarios (id_usuario, usuario, contrasena) VALUES (?, ?, ?)")!!
                crearUsuario.setString(1, UUID.randomUUID().toString())
                crearUsuario.setString(2, etUsuario.text.toString())
                crearUsuario.setString(3, etContrasena.text.toString())
                crearUsuario.executeUpdate()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Usuario Creado", Toast.LENGTH_SHORT).show()
                    etUsuario.setText("")
                    etContrasena.setText("")
                }
            }
        }
    }
}
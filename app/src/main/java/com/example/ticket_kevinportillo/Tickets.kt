package com.example.ticket_kevinportillo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.conexion
import java.util.UUID

class Tickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tickets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnagregar = findViewById<Button>(R.id.btnAgregar)
        val btnver = findViewById<Button>(R.id.btnVer)
        val ettitulo = findViewById<EditText>(R.id.etTitulo)
        val etdescripcion = findViewById<EditText>(R.id.etDesc)
        val etnombre = findViewById<EditText>(R.id.etResponsable)
        val etemail = findViewById<EditText>(R.id.etEmail)
        val ettelefono = findViewById<EditText>(R.id.etTelefono)
        val etubicacion = findViewById<EditText>(R.id.etUbicacion)

        btnver.setOnClickListener {
            val intent: Intent = Intent(this, VisualizacionTickets::class.java)
            startActivity(intent)
        }

        btnagregar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = conexion().cadenaConexion()

                val agregarTicket = objConexion?.prepareStatement("INSERT INTO Tickets (numero_ticket, titulo, descripcion, responsable, email, telefono, ubicacion) VALUES (?, ?, ?, ?, ?, ?, ?)")!!
                agregarTicket.setString(1, UUID.randomUUID().toString())
                agregarTicket.setString(2, ettitulo.text.toString())
                agregarTicket.setString(3, etdescripcion.text.toString())
                agregarTicket.setString(4, etnombre.text.toString())
                agregarTicket.setString(5, etemail.text.toString())
                agregarTicket.setString(6, ettelefono.text.toString())
                agregarTicket.setString(7, etubicacion.text.toString())
                agregarTicket.executeUpdate()

                withContext(Dispatchers.Main){
                    Toast.makeText(this@Tickets, "Ticket Agregado", Toast.LENGTH_SHORT).show()
                    ettitulo.setText("")
                    etdescripcion.setText("")
                    etnombre.setText("")
                    etemail.setText("")
                    ettelefono.setText("")
                    etubicacion.setText("")
                }
            }
        }
    }
}
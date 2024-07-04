package com.example.ticket_kevinportillo

import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.conexion
import modelo.tbTickets

class VisualizacionTickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_visualizacion_tickets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rcvTickets = findViewById<RecyclerView>(R.id.rcvTickets)

        rcvTickets.layoutManager = LinearLayoutManager(this)

        fun obtenerTickets(): List<tbTickets>{
            val objConexion = conexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Tickets")!!

            val listaTickets = mutableListOf<tbTickets>()

            while (resultSet.next()){
                val numeroTicket = resultSet.getString("numero_ticket")
                val tituloTicket = resultSet.getString("titulo")
                val descripcionTicket = resultSet.getString("descripcion")
                val nombreRespTicket = resultSet.getString("responsable")
                val email = resultSet.getString("email")
                val telefono = resultSet.getString("telefono")
                val ubicacion = resultSet.getString("ubicacion")

                val valoresJuntos = tbTickets(numeroTicket, tituloTicket, descripcionTicket, nombreRespTicket, email, telefono, ubicacion)

                listaTickets.add(valoresJuntos)
            }
            return listaTickets
        }
        CoroutineScope(Dispatchers.IO).launch {
            val ticketDB = obtenerTickets()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(ticketDB)
                rcvTickets.adapter = adapter
            }
        }

    }
}
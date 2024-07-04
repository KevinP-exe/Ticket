package RecyclerViewHelpers

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.ticket_kevinportillo.InfoTicket
import com.example.ticket_kevinportillo.R
import modelo.conexion
import modelo.tbTickets

class Adaptador(private var Datos: List<tbTickets>): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarRecyclerView(nuevaLista: List<tbTickets>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    fun eleminarRegistro(TituloTicket: String, posicion: Int) {
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = conexion().cadenaConexion()

            val borrarTicket = objConexion?.prepareStatement("DELETE Tickets WHERE titulo = ?")!!
            borrarTicket.setString(1, TituloTicket)
            borrarTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    fun actualizarListadoDespuesDeEditar(NumeroTicket: String, nuevoTitulo: String){
        val identificador = Datos.indexOfFirst{it.NumeroTicket == NumeroTicket}
        Datos[identificador].TituloTicket = nuevoTitulo
        notifyItemChanged(identificador)
    }

    fun editarTicket(TicketModificar: tbTickets){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = conexion().cadenaConexion()

            val updateTicket = objConexion?.prepareStatement("UPDATE Tickets SET titulo =?, descripcion =?, responsable =?, email =?, telefono =?, ubicacion =? WHERE numero_ticket =?")!!
            updateTicket.setString(1, TicketModificar.TituloTicket)
            updateTicket.setString(2, TicketModificar.DescripcionTicket)
            updateTicket.setString(3, TicketModificar.NombreRespTicket)
            updateTicket.setString(4, TicketModificar.Email)
            updateTicket.setString(5, TicketModificar.Telefono)
            updateTicket.setString(6, TicketModificar.Ubicacion)
            updateTicket.setString(7, TicketModificar.NumeroTicket)
            updateTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Datos[position]
        holder.txtNombreCard.text = item.NombreRespTicket

        holder.imgBorrar.setOnClickListener {
            val context = holder.txtNombreCard.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Borrar")
            builder.setMessage("Estas seguro que deseas borrarlo?")

            builder.setPositiveButton("si"){
                    dialog, wich ->
                eleminarRegistro(item.TituloTicket, position)
            }

            builder.setNegativeButton("No"){
                    dialog, wich ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        holder.imgEditar.setOnClickListener {
            val context = holder.txtNombreCard.context

            val viewDialog = LayoutInflater.from(context).inflate(R.layout.activity_editar, null)
            val editTitulo = viewDialog.findViewById<EditText>(R.id.editTitulo)
            val editDescripcion = viewDialog.findViewById<EditText>(R.id.editDescripcion)
            val editNombre = viewDialog.findViewById<EditText>(R.id.editNombre)
            val editEmail = viewDialog.findViewById<EditText>(R.id.editEmail)
            val editTelefono = viewDialog.findViewById<EditText>(R.id.editTelefono)
            val editUbicacion = viewDialog.findViewById<EditText>(R.id.editUbicacion)

            editTitulo.setText(item.TituloTicket)
            editDescripcion.setText(item.DescripcionTicket)
            editNombre.setText(item.NombreRespTicket)
            editEmail.setText(item.Email)
            editTelefono.setText(item.Telefono)
            editUbicacion.setText(item.Ubicacion)

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar Ticket")
            builder.setView(viewDialog)
            builder.setPositiveButton("Guardar"){
                    dialog, _ ->
                val nuevoTitulo = editTitulo.text.toString()
                val nuevoDescripcion = editDescripcion.text.toString()
                val nuevoNombre = editNombre.text.toString()
                val nuevoEmail = editEmail.text.toString()
                val nuevoTelefono = editTelefono.text.toString()
                val nuevoUbicacion = editUbicacion.text.toString()

                val ticketModificado = tbTickets(
                    NumeroTicket = item.NumeroTicket,
                    TituloTicket = nuevoTitulo,
                    DescripcionTicket = nuevoDescripcion,
                    NombreRespTicket = nuevoNombre,
                    Email = nuevoEmail,
                    Telefono = nuevoTelefono,
                    Ubicacion = nuevoUbicacion
                )

                editarTicket(ticketModificado)
                actualizarListadoDespuesDeEditar(ticketModificado.NumeroTicket, ticketModificado.TituloTicket)

            }
            builder.setNegativeButton("Cancelar"){
                    dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

    }

}
package RecyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticket_kevinportillo.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val txtNombreCard = view.findViewById<TextView>(R.id.txtNombreCard)
    val imgBorrar = view.findViewById<ImageView>(R.id.imgBorrar)
    val imgEditar = view.findViewById<ImageView>(R.id.imgActualizar)
    val itemView =  view.findViewById<TextView>(R.id.txtNombreCard)
}
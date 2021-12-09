package com.example.practicalogin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.Serializable

class PlatoAdaptador(private val lista_plato:List<Plato>) : RecyclerView.Adapter<PlatoAdaptador.PlatoViewHolder>() {
    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatoViewHolder {
        val vista_item= LayoutInflater.from(parent.context).inflate(R.layout.item_plato,parent, false)
        //Para poder hacer referencia al contexto de la aplicacion desde otros metodos de la clase
        contexto=parent.context

        return PlatoViewHolder(vista_item)
    }

    override fun onBindViewHolder(holder: PlatoViewHolder, position: Int) {
        val item_actual=lista_plato[position]
        holder.nombre.text=item_actual.nombre
        holder.persona.text= item_actual.personas.toString()
        holder.autor.text=item_actual.autor.toString()
        holder.rating.rating= item_actual.puntuacion!!.toFloat()


        Glide.with(contexto).load(item_actual.url_plato).into(holder.miniatura)

        holder.editar.setOnClickListener {
            val activity= Intent(contexto,editarPlato::class.java)
            activity.putExtra("plato",item_actual as Serializable)

            contexto.startActivity(activity)

        }

        holder.borrar.setOnClickListener {
            val db_reference= FirebaseDatabase.getInstance().getReference()
            db_reference.child("restaurante")
                .child("platos")
                .child(item_actual.id!!)
                .removeValue()

            Toast.makeText(contexto,
                "Plato borrado con exito",
                Toast.LENGTH_SHORT)
                .show()

        }

    }
    override fun getItemCount(): Int = lista_plato.size

    class PlatoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val miniatura: ImageView = itemView.findViewById(R.id.item_miniautura)
        val nombre: TextView = itemView.findViewById(R.id.item_nombre)
        val persona: TextView = itemView.findViewById(R.id.item_personas)
        val autor: TextView = itemView.findViewById(R.id.item_autor)
        val rating : RatingBar =itemView.findViewById(R.id.item_rating)
        val editar: ImageView =itemView.findViewById(R.id.item_editar)
        val borrar: ImageView =itemView.findViewById(R.id.item_borrar)


    }

}

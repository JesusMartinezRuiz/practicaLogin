package com.example.practicalogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class verPlato : AppCompatActivity() {

    lateinit var volver:Button
    lateinit var recycler: RecyclerView
    lateinit var lista:ArrayList<Plato>
    private lateinit var db_ref: DatabaseReference
    private lateinit var sto_ref: StorageReference
    lateinit var asc : ImageButton
    lateinit var desc :ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_plato)

        db_ref= FirebaseDatabase.getInstance().getReference()
        sto_ref= FirebaseStorage.getInstance().getReference()
        volver= findViewById(R.id.btn_ver_volver)
        lista=ArrayList<Plato>()
        asc=findViewById(R.id.ib_ver_asc)
        desc=findViewById(R.id.ib_ver_desc)

        recycler=findViewById(R.id.lista)
        recycler.adapter=PlatoAdaptador(lista)
        recycler.layoutManager= LinearLayoutManager(applicationContext)
        recycler.setHasFixedSize(true)


        asc.setOnClickListener{
            lista.sortedBy { it.personas }

        }
        desc.setOnClickListener{
            lista.sortedBy { it.personas }.reversed()
        }

        volver.setOnClickListener {
            val actividad = Intent(applicationContext,MainActivity2::class.java)
            startActivity (actividad)
        }

        db_ref.child("restaurante")
            .child("platos")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach { hijo->
                        val pojo_plato=hijo?.getValue(Plato::class.java)
                        lista.add(pojo_plato!!)
                    }
                    recycler.adapter?.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })



    }

}
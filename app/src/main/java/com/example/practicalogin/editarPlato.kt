package com.example.practicalogin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CountDownLatch

class editarPlato : AppCompatActivity() {

    lateinit var volver: Button
    lateinit var modificar: Button
    lateinit var nombre: EditText
    lateinit var autor: EditText
    lateinit var personas: EditText
    lateinit var puntuacion: RatingBar
    lateinit var imagen_plato: ImageView
    private lateinit var pojo_plato:Plato

    private var url_plato: Uri?=null

    private lateinit var db_ref: DatabaseReference
    private lateinit var sto_ref: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_plato)

        pojo_plato=intent.getSerializableExtra("plato") as Plato

        nombre=findViewById(R.id.et_nombre_editar)
        autor=findViewById(R.id.et_autor_editar)
        personas=findViewById(R.id.et_personas_editar)
        imagen_plato=findViewById(R.id.iv_editarPlato)
        puntuacion=findViewById(R.id.rb_editar)
        modificar=findViewById(R.id.btn_editarPlato)
        volver=findViewById(R.id.btn_volverEditar)

        db_ref= FirebaseDatabase.getInstance().getReference()
        sto_ref= FirebaseStorage.getInstance().getReference()



        nombre.setText(pojo_plato.nombre)
        autor.setText(pojo_plato.autor)
        personas.setText(pojo_plato.personas.toString())
        puntuacion.rating=(pojo_plato.puntuacion!!)

        Glide.with(applicationContext).load(pojo_plato.url_plato).into(imagen_plato)

        imagen_plato.setOnClickListener {
            obtener_url.launch("image/*")
        }

        volver.setOnClickListener{
            val actividad = Intent(applicationContext,verPlato::class.java)
            startActivity (actividad)
        }

        modificar.setOnClickListener {

            if(nombre.text.toString().trim().equals("") ||
                autor.text.toString().trim().equals("") ) {

                Toast.makeText(applicationContext, "Falta datos del formulario", Toast.LENGTH_SHORT)
                    .show()
            }else if(personas.text.toString().trim().toIntOrNull()==null || puntuacion.rating==null){
                Toast.makeText(applicationContext, "Falta datos del formulario", Toast.LENGTH_SHORT).show()
            }else{

                var url_plato_firebase:String?=pojo_plato.url_plato

                GlobalScope.launch(Dispatchers.IO) {
                    if(!nombre.text.toString().trim().equals(pojo_plato.nombre) && existe_plato(nombre.text.toString().trim())){
                        tostadaCorrutina("El plato ya existe")

                    }else{
                        if(url_plato!=null){
                            url_plato_firebase=editarImagen(pojo_plato.id!!,url_plato!!)
                        }

                        editarPlato(pojo_plato.id!!,
                            nombre.text.toString().trim(),
                            autor.text.toString().trim(),
                            personas.text.toString().trim().toInt(),
                            puntuacion.rating,
                            url_plato_firebase!!
                        )
                        tostadaCorrutina("Plato modificado con éxito")
                        val actividad = Intent(applicationContext, verPlato::class.java)
                        startActivity(actividad)
                    }
                }

        }


    }


}
    private fun existe_plato(nombre:String):Boolean{
        var resultado:Boolean?=false

        val semaforo= CountDownLatch(1)
        db_ref.child("restaurante")
            .child("platos")
            .orderByChild("nombre")
            .equalTo(nombre)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChildren()){
                        resultado=true;
                    }
                    semaforo.countDown()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })

        semaforo.await();

        return resultado!!;
    }

    private suspend fun editarImagen(id:String,imagen:Uri):String{
        var url_plato_firebase:Uri?=null

        url_plato_firebase=sto_ref.child("restaurante").child("platos").child(id)
            .putFile(imagen).await().storage.downloadUrl.await()

        return url_plato_firebase.toString()
    }

    private fun editarPlato(id:String,nombre:String,autor:String,personas:Int,puntuacion:Float,url_firebase:String){
        val nuevo_plato= Plato(
            id,
            nombre,
            autor,
            personas,
            puntuacion,
            url_firebase
        )
        db_ref.child("restaurante").child("platos").child(id).setValue(nuevo_plato)

    }


    val obtener_url= registerForActivityResult(ActivityResultContracts.GetContent()){
            uri:Uri?->
        when (uri){
            null-> Toast.makeText(applicationContext,"No has seleccionado una imagen", Toast.LENGTH_SHORT).show()
            else->{
                url_plato=uri
                imagen_plato.setImageURI(url_plato)
                Toast.makeText(applicationContext,"Has seleccionado una nueva imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun tostadaCorrutina(texto:String){
        runOnUiThread{
            Toast.makeText(
                applicationContext,
                texto,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
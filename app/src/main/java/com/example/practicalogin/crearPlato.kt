package com.example.practicalogin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class crearPlato : AppCompatActivity() {

    lateinit var volver: Button
    lateinit var crear: Button
    lateinit var nombre:EditText
    lateinit var autor:EditText
    lateinit var personas:EditText
    lateinit var puntuacion:RatingBar
    lateinit var imagen_plato:ImageView
    var url_plato: Uri?=null

    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_plato)

        nombre=findViewById(R.id.et_nombre_crear)
        autor=findViewById(R.id.et_autor_crear)
        personas=findViewById(R.id.et_personas_crear)
        imagen_plato=findViewById(R.id.iv_crearPlato)
        puntuacion=findViewById(R.id.rb_crear)
        crear=findViewById(R.id.btn_crearPlato)
        volver=findViewById(R.id.btn_volverCrear)

        db_ref=FirebaseDatabase.getInstance().reference
        sto_ref=FirebaseStorage.getInstance().reference

        volver.setOnClickListener{
            val actividad = Intent(applicationContext,MainActivity2::class.java)
            startActivity (actividad)
        }


        crear.setOnClickListener {


            val identficador=db_ref.child("restaurante")
                .child("platos").push().key


            if(url_plato!=null){

                GlobalScope.launch(Dispatchers.IO){

                    val url_firebase=sto_ref.child("restaurante")
                        .child("platos")
                        .child(identficador!!)
                        .putFile(url_plato!!)
                        .await().storage.downloadUrl.await()


                    val nuevo_plato=Plato(identficador,
                        nombre.text.toString().trim(),
                        autor.text.toString().trim(),
                        personas.text.toString().toIntOrNull(),
                        puntuacion.rating,
                        url_firebase.toString())


                    db_ref.child("restaurante")
                        .child("platos")
                        .child(identficador?:"El identificador de NULL")
                        .setValue(nuevo_plato)

                    tostadaCorrutina("Plato creado con éxito")


                    val actividad = Intent(applicationContext,MainActivity2::class.java)
                    startActivity (actividad)

                }

            }else{
                Toast.makeText(applicationContext,
                    "Tienes que poner una foto del Plato!",
                    Toast.LENGTH_SHORT).show()
            }

        }

        imagen_plato.setOnClickListener {
            obtener_url.launch("image/*")

        }

    }
    val obtener_url= registerForActivityResult(ActivityResultContracts.GetContent())
    {
            uri:Uri?->

        if (uri==null){
            Toast.makeText(applicationContext,"No has seleccionado una imagen",
                Toast.LENGTH_SHORT).show()
        }else{

            url_plato=uri

            imagen_plato.setImageURI(uri)

            Toast.makeText(applicationContext,"Imagen seleccionada correcta",
                Toast.LENGTH_SHORT).show()
        }

    }


    suspend fun existe_plato(nombre:String):Boolean{
        return false
    }


    suspend fun insertarImgPlato(id:String,imagen:Uri):String{
        return ""
    }


    suspend fun insertarPlato(id:String,nombre:String,autor:String,personas:Int,puntuacion:Float,url_firebase:String){


    }

    suspend fun tostadaCorrutina(texto:String){
        runOnUiThread({
            Toast.makeText(
                applicationContext,
                texto,
                Toast.LENGTH_SHORT
            ).show()
        })
    }
}
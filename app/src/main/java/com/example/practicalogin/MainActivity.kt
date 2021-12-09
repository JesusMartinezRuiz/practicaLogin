package com.example.practicalogin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CountDownLatch



class MainActivity : AppCompatActivity() {

    lateinit var nombre:TextInputEditText
    lateinit var pass:TextInputEditText
    lateinit var login:Button
    lateinit var registrar:TextView
    lateinit var db_ref: DatabaseReference
    lateinit var sto_ref: StorageReference
    lateinit var config:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        nombre=findViewById(R.id.main_et_nombre)
        pass=findViewById(R.id.main_et_contraseña)
        login=findViewById(R.id.main_btn_login)
        registrar=findViewById(R.id.main_tv_newAcc)
        config=findViewById(R.id.iv_config)

        db_ref= FirebaseDatabase.getInstance().reference
        sto_ref= FirebaseStorage.getInstance().reference


        checkTheme()

        login.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO){

                if(existe_usuario(nombre.text.toString().trim()) && existe_pass(pass.text.toString().trim())){/*esto tiene que ir dentro de un global Scope*/

                    val actividad = Intent(applicationContext,MainActivity2::class.java)
                    startActivity (actividad)

                }else{
                    tostadaCorrutina("Datos introducidos incorrectos")
                }
            }

        }
        registrar.setOnClickListener {
            val actividad = Intent(applicationContext,Registro::class.java)
            startActivity (actividad)
        }

        config.setOnClickListener {
            val actividad = Intent(applicationContext,Configuracion::class.java)
            startActivity (actividad)
        }

    }
    private suspend fun existe_usuario(nombre:String):Boolean{
        var resultado:Boolean?=false

        val semaforo= CountDownLatch(1)
        db_ref.child("foodies")
            .child("usuarios")
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

    private suspend fun existe_pass(nombre:String):Boolean{
        var resultado:Boolean?=false

        val semaforo= CountDownLatch(1)
        db_ref.child("foodies")
            .child("usuarios")
            .orderByChild("contraseña")
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

    private fun checkTheme() {
        when (Configuracion.MyPreferences(this).darkMode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                delegate.applyDayNight()
            }
        }
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
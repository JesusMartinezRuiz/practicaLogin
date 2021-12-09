package com.example.practicalogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity2 : AppCompatActivity() {

    lateinit var crear:Button
    lateinit var ver:Button
    lateinit var regresar:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        crear=findViewById(R.id.main_btn_crear)
        ver=findViewById(R.id.main_btn_ver)
        regresar=findViewById(R.id.btn_regresar)

        ver.setOnClickListener {
            val actividad = Intent(applicationContext,verPlato::class.java)
            startActivity (actividad)
        }

        crear.setOnClickListener {
            val actividad = Intent(applicationContext,crearPlato::class.java)
            startActivity (actividad)
        }

        regresar.setOnClickListener {
            val actividad = Intent(applicationContext,MainActivity::class.java)
            startActivity (actividad)
        }

    }
}
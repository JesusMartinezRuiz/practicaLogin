package com.example.practicalogin

import java.io.Serializable

data class Plato(var id:String?=null,
                 var nombre: String? = null,
                 var autor:String?=null,
                 var personas:Int?=0,
                 var puntuacion:Float?=0.0f,
                 var url_plato:String?=null):Serializable
package com.example.glide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.glide.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    lateinit var colaPeticiones:RequestQueue
    lateinit var contenedorVistas:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        colaPeticiones=Volley.newRequestQueue(this)

        contenedorVistas= ActivityMainBinding.inflate(layoutInflater)

        setContentView(contenedorVistas.root)

        contenedorVistas.btnBuscar.setOnClickListener {

            var pais=contenedorVistas.etxtPais.text.toString()

            Log.i("result",pais)

            var url="https://restcountries.com/v3.1/name/$pais"
            Log.i("result",url)

            var request=JsonArrayRequest(
                url,
                Response.Listener { resp->
                    Log.i("result","Objetos obtenidos: ${resp.length()}")

                    var obj=JSONObject(resp.get(0).toString())//convierto en JSON el primer elemento del array

                    //Log.i("result",resp.toString())//imprimo el array
                    //Log.i("result","========================================================================================================================================================================")
                    //Log.i("result",obj.get("name").toString())//imprimo el JSON[Propidad name]

                    var nombre=JSONObject(obj.get("name").toString())//PROCESANDO NOMBRE PAIS (OBJETO JSON)
                    Log.i("result",nombre.get("common").toString())
                    contenedorVistas.pNOficial.text=nombre.get("common").toString()

                    var capital=JSONArray(obj.get("capital").toString())//PROCESANDO CAPITAL PAIS (ARRAY)
                    Log.i("result",capital[0].toString())
                    contenedorVistas.pCapital.text=capital[0].toString()

                    var lang=JSONObject(obj.get("languages").toString())//PROCESANDO IDIOMA PAIS (OBJETO JSON)

                    var idioma=JSONObject(lang.toString())
                    Log.i("result",idioma.toString())

                    val indices=idioma.names()
                    var idiomas=""
                    for(item in 0 until indices.length())
                    {
                        var indiceActual=indices.getString(item)
                        idiomas += idioma.getString(indiceActual)+", "
                    }
                    contenedorVistas.pIdiomas.text=idiomas



                    var band=JSONObject(obj.get("flags").toString())//PROCESANDO BANDERA PAIS (OBJETO JSON)
                    //Log.i("result",band.get("png").toString())

                    Glide
                        .with(this)
                        .load(band.get("png").toString())
                        .placeholder(R.drawable.cargando)
                        .error(R.drawable.error)
                        .into(contenedorVistas.countriFlag)


                    var infoMoneda=JSONObject(obj.get("currencies").toString())

                    val indicesMoneda=infoMoneda.names()

                    for(item in 0 until indicesMoneda.length())
                    {
                        var indiceActual=indicesMoneda.getString(item)
                        var money= JSONObject(infoMoneda.getString(indiceActual).toString())

                       contenedorVistas.pMoneda.text=money.get("name").toString()+ ": "+money.get("symbol").toString()
                    }




                    //Log.i("result",JSONObject(JSONObject(obj.get("currencies").toString()).get("COP").toString()).get("name").toString())
                    /*Log.i("result",infoMoneda.get("name").toString())
                    Log.i("result",infoMoneda.get("symbol").toString())*/

                },
                Response.ErrorListener {

                }
            )

            colaPeticiones.add(request)

        }




       /* contenedorVistas.btnLoad.setOnClickListener {

            Glide
                .with(this)
                .load("https://www.todofondos.net/wp-content/uploads/avengers-4k-wallpaper-scaled.jpg")
                .placeholder(R.drawable.cargando)
                .error(R.drawable.error)
                .into(contenedorVistas.image)

        }*/
    }
}
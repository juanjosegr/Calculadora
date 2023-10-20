package com.example.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    // Listas de botones para números y operaciones.
    private lateinit var listaDeNumeros: MutableList<Button>
    private lateinit var listaDeOperaciones: MutableList<Button>

    // Instancia de la clase Calculo para realizar cálculos.
    private val calculo = Calculo()
    // var para comprobar si se realiza la operacion o no.
    private var seRealizoOperacion = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de las listas de botones.
        listaDeNumeros = mutableListOf()
        listaDeOperaciones = mutableListOf()

        // Configuración de las listas de números y operaciones.
        listaNumeros()
        listaOperaciones()

        val pantallaMostrarResultado = findViewById<TextView>(R.id.pantallaMostrarResultado)

        // Configuración de la acción cuando se presionan botones de números.
        for (numero in listaDeNumeros){
            numero.setOnClickListener(View.OnClickListener {
                if (seRealizoOperacion){                                                //Comprobacion de si es operacion nueva o anterior, para no concatenar el numero de la operacion anterior
                                                                                        //y poder introducir numero nuevo.
                    pantallaMostrarResultado.text = (it as Button).text.toString()
                    seRealizoOperacion = false
                }else{
                    val numeroPresionado = (it as Button).text.toString()               // Obtiene el número presionado como texto.
                    val numeroActgual = pantallaMostrarResultado.text.toString()        // Obtiene el número actual en la pantalla.
                    if (numeroActgual == "0"){                                          // Comprueba si el número actual es 0 y lo reemplaza si es el caso.
                        pantallaMostrarResultado.text = numeroPresionado
                    }else{                                                              // Agrega el número presionado al número actual.
                        pantallaMostrarResultado.text = "$numeroActgual$numeroPresionado"
                    }
                }

            })
        }

        // Configuración de la acción cuando se presionan botones de operaciones.
        for (operacion in listaDeOperaciones){
            operacion.setOnClickListener(View.OnClickListener {
                val operacionPresionada = (it as Button).text.toString()
                val numeroActual = pantallaMostrarResultado.text.toString()

                if(numeroActual.isNotEmpty()){                                      // Convierte el número actual a un  double.
                    val numeroActualDouble = numeroActual.toDouble()
                    calculo.ingresarNumero(numeroActualDouble)                      // Ingresa el número actual en el cálculo.

                    when (operacionPresionada){                                     // Realiza la operación correspondiente basada en la operación presionada.
                        "+" -> calculo.realizarOperacion("+")
                        "-" -> calculo.realizarOperacion("-")
                        "*" -> calculo.realizarOperacion("*")
                        "/" -> calculo.realizarOperacion("/")
                    }
                    pantallaMostrarResultado.text = ""                              // Limpia la pantalla.
                }else{
                    // Muestra un mensaje de error si no hay número en la pantalla.
                    Toast.makeText(this, "Accion no posible", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Configuración de la acción cuando se presiona el botón igual.
        val btnIgual = findViewById<Button>(R.id.btn_Igual)
        btnIgual.setOnClickListener {
            val numeroActualText = pantallaMostrarResultado.text.toString()

            if (numeroActualText.isNotEmpty()){
                val numeroActual = numeroActualText.toDouble()

                if (calculo.num1 != 0.0 && calculo.operaciones.isNotEmpty()){
                calculo.ingresarNumero(numeroActual)                            // Ingresa el número actual y calcula el resultado.
                val resultado = calculo.obtenerResultado()
                                                                                // Muestra el resultado, si es acabado en int lo muestra como int y si es float como float
                                                                                // y luego borra la lógica de cálculo.
                    if (resultado == resultado.toInt().toDouble()) {
                        pantallaMostrarResultado.text = resultado.toInt().toString()
                    } else {
                        pantallaMostrarResultado.text = resultado.toString()
                    }
                calculo.borrar()
                seRealizoOperacion = true                                       //Realización de operación para no concatenar el num del resultado.
            }else {
                                                                                // Muestra mensajes de error según diferentes situaciones.
                    if (calculo.num1 == 0.0 && calculo.operaciones.isEmpty()) {
                        Toast.makeText(this, "Ingrese al menos dos número y una operación para obtener un resultado.", Toast.LENGTH_SHORT).show()
                    } else if (calculo.num1 != 0.0 && calculo.operaciones.isEmpty()) {
                        Toast.makeText(this, "Ingrese una operación para obtener un resultado.", Toast.LENGTH_SHORT).show()
                    } else if (calculo.num1 == 0.0 && calculo.operaciones.isNotEmpty()) {
                        Toast.makeText(this, "Ingrese al menos dos número para obtener un resultado.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Configuración de la acción cuando se presiona el botón borrar.
        val btnBorrar = findViewById<Button>(R.id.btn_Borrar)
        btnBorrar.setOnClickListener{
            // Borra la calculadora y establece la pantalla en 0.
            calculo.borrar()
            pantallaMostrarResultado.text = "0"
        }



    }

    /**
     * Configura la lista de botones para números.
     */
    private fun listaNumeros(){
        listaDeNumeros.add(findViewById(R.id.btn_0))
        listaDeNumeros.add(findViewById(R.id.btn_1))
        listaDeNumeros.add(findViewById(R.id.btn_2))
        listaDeNumeros.add(findViewById(R.id.btn_3))
        listaDeNumeros.add(findViewById(R.id.btn_4))
        listaDeNumeros.add(findViewById(R.id.btn_5))
        listaDeNumeros.add(findViewById(R.id.btn_6))
        listaDeNumeros.add(findViewById(R.id.btn_7))
        listaDeNumeros.add(findViewById(R.id.btn_8))
        listaDeNumeros.add(findViewById(R.id.btn_9))
        listaDeNumeros.add(findViewById(R.id.btn_Punto))
    }

    /**
     * Configura la lista de botones para operaciones.
     */
    private fun listaOperaciones(){
        listaDeOperaciones.add(findViewById(R.id.btn_Mas))
        listaDeOperaciones.add(findViewById(R.id.btn_Menos))
        listaDeOperaciones.add(findViewById(R.id.btn_Multiplicar))
        listaDeOperaciones.add(findViewById(R.id.btn_Dividir))
    }
}
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
    //TextView de la pantalla de resultados y de la pantalla de registro.
    private lateinit var pantallaMostrarResultado: TextView
    private lateinit var pantallaRegistro: TextView
    //Inicializacion de la pantalla de registro a vacia
    private var registroOperaciones: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pantallaMostrarResultado = findViewById<TextView>(R.id.pantallaMostrarResultado)
        pantallaRegistro = findViewById<TextView>(R.id.pantallaRegistro)

        // Inicializar las listas de botones.
        funListaDeNumeros()
        funListaDeOperaciones()
        // Inicializar los botones de igual, borrar y borrar uno.
        funBotonIgual()
        funBotonBorrar()
        funBotonBorrarUno()

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

    /**
     * Muestra un mensaje de error en forma de Toast.
     */
    private fun mensajesError(msj:String){
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show()
    }

    /**
     * Configura los botones de números para establecer los eventos de clic y actualizar la pantalla.
     */
    fun funListaDeNumeros(){
        listaDeNumeros = mutableListOf()
        listaNumeros()
        pantallaMostrarResultado = findViewById<TextView>(R.id.pantallaMostrarResultado)

        for (numero in listaDeNumeros){
            numero.setOnClickListener(View.OnClickListener {
                if (seRealizoOperacion){                                                //Comprobacion de si es operacion nueva o anterior, para no concatenar el numero de la operacion anterior
                    //y poder introducir numero nuevo.
                    pantallaMostrarResultado.text = (it as Button).text.toString()
                    seRealizoOperacion = false
                    limpiarPantallaRegistro()
                    agregarNumeroAlRegistro(pantallaMostrarResultado.text.toString())
                }else{
                    val numeroPresionado = (it as Button).text.toString()               // Obtiene el número presionado como texto.
                    val numeroActgual = pantallaMostrarResultado.text.toString()        // Obtiene el número actual en la pantalla.

                    if (numeroPresionado == "." && numeroActgual.contains(".")){
                        mensajesError("No puedes meter otro punto.")
                    }else{
                        if (numeroActgual == "0"){                                          // Comprueba si el número actual es 0 y lo reemplaza si es el caso.
                            pantallaMostrarResultado.text = numeroPresionado
                        }else{                                                              // Agrega el número presionado al número actual.
                            pantallaMostrarResultado.text = "$numeroActgual$numeroPresionado"
                        }
                        agregarNumeroAlRegistro(numeroPresionado)
                    }
                }

            })
        }
    }

    /**
     * Configura los botones de operaciones para establecer eventos de clic y realizar cálculos.
     */
    fun funListaDeOperaciones(){
        listaDeOperaciones = mutableListOf()
        listaOperaciones()
        pantallaMostrarResultado = findViewById<TextView>(R.id.pantallaMostrarResultado)

        // Configuración de la acción cuando se presionan botones de operaciones.
        for (operacion in listaDeOperaciones){
            operacion.setOnClickListener(View.OnClickListener {
                val operacionPresionada = (it as Button).text.toString()
                val numeroActual = pantallaMostrarResultado.text.toString()

                if(numeroActual.isNotEmpty()){                                      // Convierte el número actual a un  double.
                    val numeroActualDouble = numeroActual.toDouble()
                    calculo.ingresarNumero(numeroActualDouble)                      // Ingresa el número actual en el cálculo.

                    when (operacionPresionada){                                     // Realiza la operación correspondiente basada en la operación presionada.
                        "+" -> {
                            calculo.realizarOperacion("+")
                            agregarOperacionAlRegistro("+")
                        }
                        "-" -> {
                            calculo.realizarOperacion("-")
                            agregarOperacionAlRegistro("-")
                        }
                        "*" -> {
                            calculo.realizarOperacion("*")
                            agregarOperacionAlRegistro("*")
                        }
                        "/" -> {
                            if (numeroActualDouble == 0.0) {
                            mensajesError("No puedes dividir entre 0.")
                        } else {
                            calculo.realizarOperacion("/")
                            agregarOperacionAlRegistro("/")
                        }
                        }
                    }
                    pantallaMostrarResultado.text = ""                              // Limpia la pantalla.
                }else{
                    // Muestra un mensaje de error si no hay número en la pantalla.
                    mensajesError("Accion no posible")
                }
            })
        }
    }

    /**
     * Configura el botón igual para realizar cálculos cuando se presiona.
     */
    fun funBotonIgual(){
        // Configuración de la acción cuando se presiona el botón igual.
        val btnIgual = findViewById<Button>(R.id.btn_Igual)
        btnIgual.setOnClickListener {
        val numeroActualText = pantallaMostrarResultado.text.toString()

            if (numeroActualText.isNotEmpty()){
                val numeroActual = numeroActualText.toDouble()

                if (calculo.num1 != 0.0 && calculo.operaciones.isNotEmpty()){
                    calculo.ingresarNumero(numeroActual)                            // Ingresa el número actual y calcula el resultado.
                    if (calculo.operaciones.last() == '/' && numeroActual == 0.0) {
                        mensajesError("No puedes dividir entre 0.")
                    } else {
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
                            limpiarPantallaRegistro()
                            agregarNumeroAlRegistro(resultado.toString())
                    }
                }else {
                    // Muestra mensajes de error según diferentes situaciones.
                    if (calculo.num1 == 0.0 && calculo.operaciones.isEmpty()) {
                        mensajesError("Ingrese al menos dos número y una operación para obtener un resultado.")
                    } else if (calculo.num1 != 0.0 && calculo.operaciones.isEmpty()) {
                        mensajesError("Ingrese una operación para obtener un resultado.")
                    } else if (calculo.num1 == 0.0 && calculo.operaciones.isNotEmpty()) {
                        mensajesError("Ingrese al menos dos número para obtener un resultado.")
                    }
                }
            }
        }
    }

    /**
     * Configura el botón "Borrar" para reiniciar la calculadora y establecer la pantalla en 0.
     */
    fun funBotonBorrar(){
        // Configuración de la acción cuando se presiona el botón borrar.
        val btnBorrar = findViewById<Button>(R.id.btn_Borrar)
        btnBorrar.setOnClickListener{
            // Borra la calculadora y establece la pantalla en 0.
            calculo.borrar()
            pantallaMostrarResultado.text = "0"
            registroOperaciones = ""
            pantallaRegistro.text = registroOperaciones
        }
    }

    /**
     * Configura el botón "Borrar Uno" para eliminar el último carácter de la pantalla y registro.
     */
    fun funBotonBorrarUno() {
        val btnBorrarUno = findViewById<Button>(R.id.btn_BorrarUno)

        btnBorrarUno.setOnClickListener {
                                                                            // Obtiene el texto actual de la pantalla.
            val numeroActualText = pantallaMostrarResultado.text.toString()
            if (numeroActualText.isNotEmpty()) {
                                                                            // Calcula el nuevo texto de la pantalla eliminando el último carácter.
                val nuevoNumeroActualText = if (numeroActualText.length > 1) {
                    numeroActualText.dropLast(1)
                } else {
                    "0"
                }
                pantallaMostrarResultado.text = nuevoNumeroActualText

                // También borra el último carácter en la pantalla de registro.
                val registroActual = pantallaRegistro.text.toString()
                if (registroActual.isNotEmpty()) {
                    val nuevoRegistro = if (registroActual.length > 1) {
                        registroActual.dropLast(1)
                    } else {
                        ""
                    }
                    pantallaRegistro.text = nuevoRegistro
                }
            }
        }
    }

    /**
     * Agrega una operación al registro de operaciones.
     *
     * @param operacion La operación a agregar al registro.
     */
    private fun agregarOperacionAlRegistro(operacion: String) {
        val registroActual = pantallaRegistro.text.toString()
        if (registroActual.isNotEmpty()) {
            // Si ya hay contenido en el registro, agrega un espacio y luego la operación.
            pantallaRegistro.text = "$registroActual $operacion "
        } else {
            // Si el registro está vacío, simplemente agrega la operación.
            pantallaRegistro.text = operacion
        }
        // Actualiza el registro completo de operaciones.
        registroOperaciones = "$registroOperaciones $operacion "
    }

    /**
     * Agrega un número al registro de operaciones.
     *
     * @param numero El número a agregar al registro.
     */
    private fun agregarNumeroAlRegistro(numero: String) {
        val registroActual = pantallaRegistro.text.toString()
        if (registroActual.isNotEmpty()) {
            // Si ya hay contenido en el registro, simplemente agrega el número al final.
            pantallaRegistro.text = "$registroActual$numero"
        } else {
            // Si el registro está vacío, establece el número como el contenido del registro.
            pantallaRegistro.text = numero
        }
        // Actualiza el registro completo de operaciones.
        registroOperaciones = "$registroOperaciones$numero"
    }

    /**
     * Limpia la pantalla de registro.
     */
    private fun limpiarPantallaRegistro() {
        pantallaRegistro.text = ""
    }

}
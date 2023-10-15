package com.example.calculadora

/**
 *
 * @property num1 El primer número en una operación.
 * @property num2 El segundo número en una operación.
 * @property resultado El resultado de la ultima operación realizada.
 * @property operaciones La operación actual a realizar (por ejemplo, "+", "-", "*", "/").
 */
class Calculo {

    var num1 = 0.0
    var num2 = 0.0
    var resultado = 0.0
    var operaciones = ""

    /**
     * Ingresa un número en el cálculo.
     *
     * @param numero El número ingresado en la operación.
     */


    fun ingresarNumero(numero: Double){
        if (operaciones.isEmpty()){
            num1 = numero
        }else{
            num2= numero
        }
    }

    /**
     * Realiza una operación matemática.
     *
     * @param operacion La operación a realizar (por ejemplo, "+", "-", "*", "/").
     */

    fun realizarOperacion(operacion:String){
        operaciones = operacion
    }

    /**
     * Obtiene el resultado de la operación actual.
     *
     * @return El resultado de la operación actual.
     */

    fun obtenerResultado():Double{
        return when (operaciones){
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            "/" -> {
                if (num2 != 0.0) {
                    num1 / num2

                }else {
                    Double.NaN
                }
            }
            else -> num1
            }
        }

    /**
     * Borra los valores de las propiedades y restablece el cálculo.
     */

    fun borrar() {
        num1 = 0.0
        num2 = 0.0
        resultado = 0.0
        operaciones = ""
    }

}
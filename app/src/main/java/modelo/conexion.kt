package modelo

import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager

class conexion {
    fun cadenaConexion(): Connection? {
        try {
            val url = "jdbc:oracle:thin:@192.168.0.13:1521:xe"
            val usuario = "SYSTEM"
            val contrasena = "ITR2024"

            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        } catch (e: Exception) {
            println("error: $e")
            return null
        }
    }
}
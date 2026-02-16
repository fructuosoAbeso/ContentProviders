package es.ua.eps.logincliente.provider

import android.content.ContentResolver
import android.net.Uri

/**
 * Helper para validar credenciales mediante ContentProvider
 */
class UsuariosProviderHelper(private val contentResolver: ContentResolver) {

    companion object {
        // DEBE ser idéntico al 'android:authorities' del Manifest de la App 1
        private const val AUTHORITY = "es.ua.eps.basededatossqlite.provider"
        private const val USUARIOS_TABLE = "usuarios"

        // URI para acceder a los usuarios
        val USUARIOS_URI: Uri = Uri.parse("content://$AUTHORITY/$USUARIOS_TABLE")

        // Nombres de las columnas que definimos en la Entidad de Room del servidor
        object Columns {
            const val ID = "id"
            const val NOMBRE_USUARIO = "nombre_usuario"
            const val PASSWORD = "password"
        }
    }

    /**
     * Valida las credenciales de un usuario mediante la App Servidora
     */
    fun validarCredenciales(username: String, password: String): Boolean {
        // En el servidor, si mandamos 2 argumentos, él ya sabe que es un Login
        // No necesitamos pasarle el String 'selection' completo porque el DAO de Room ya tiene la SQL
        val selectionArgs = arrayOf(username, password)

        try {
            val cursor = contentResolver.query(
                USUARIOS_URI,
                arrayOf(Columns.ID), // Solo pedimos el ID para confirmar que existe
                null,                // El servidor ignorará esto si mandamos 2 args
                selectionArgs,       // Estos son los que usa el loginCursor(user, pass)
                null
            )

            cursor?.use {
                // Si el cursor tiene filas, el usuario y pass coinciden en la BD de Room
                return it.count > 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return false
    }

    /**
     * Verifica si el ContentProvider del servidor es visible
     */
    fun isProviderAvailable(): Boolean {
        return try {
            val cursor = contentResolver.query(
                USUARIOS_URI,
                arrayOf(Columns.ID),
                null,
                null,
                null
            )
            val available = cursor != null
            cursor?.close()
            available
        } catch (e: Exception) {
            false
        }
    }
}
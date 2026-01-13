package es.ua.eps.appclientelogin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class UserLoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            validarUsuario(username, password)
        }
    }

    private fun validarUsuario(username: String, password: String) {
        // URI del ContentProvider de la otra app
        val uri = Uri.parse("content://es.ua.eps.basededatossqlite.provider/usuarios")

        // Consulta usando ContentResolver
        val cursor = contentResolver.query(
            uri,
            arrayOf("id"), // Solo necesitamos saber si existe
            "nombre_usuario=? AND password=?",
            arrayOf(username, password),
            null
        )

        if (cursor != null && cursor.moveToFirst()) {
            // Acceso permitido → abrir UserActivity
            val intent = Intent(this, UserPageActivity::class.java)
            intent.putExtra(UserPageActivity.EXTRA_USERNAME, username)
            startActivity(intent)
            finish() // cerrar login
        } else {
            Toast.makeText(this, "Error usuario/password incorrectos", Toast.LENGTH_SHORT).show()
        }

        cursor?.close()
    }
}

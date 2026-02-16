package es.ua.eps.appclientelogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.logincliente.provider.UsuariosProviderHelper
import java.util.concurrent.Executors

class LoginClientActivity : AppCompatActivity() {

    private lateinit var providerHelper: UsuariosProviderHelper
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_client)

        // Inicializamos el Helper pasándole el contentResolver de la actividad
        providerHelper = UsuariosProviderHelper(contentResolver)

        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val user = etUser.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Realizamos la consulta en un hilo secundario para no bloquear la UI
            // (Aunque los ContentProviders son rápidos, es mejor práctica)
            executor.execute {
                val loginOk = providerHelper.validarCredenciales(user, pass)

                runOnUiThread {
                    if (loginOk) {
                        Toast.makeText(this, "Login remoto exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginClientActivity, UserHomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Si falla, verificamos si es por credenciales o porque el servidor no está
                        verificarError()
                    }
                }
            }
        }
    }

    private fun verificarError() {
        executor.execute {
            val disponible = providerHelper.isProviderAvailable()
            runOnUiThread {
                if (!disponible) {
                    Toast.makeText(this, "Error: App Servidora no instalada o inaccesible", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
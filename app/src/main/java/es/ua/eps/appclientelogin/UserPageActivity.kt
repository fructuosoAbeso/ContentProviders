package es.ua.eps.appclientelogin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserPageActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)

        val tvWelcome: TextView = findViewById(R.id.tvWelcome)

        // Recoger el username pasado desde UserLoginActivity
        val username = intent.getStringExtra(EXTRA_USERNAME)

        tvWelcome.text = "¡Bienvenido, $username!"
    }
}

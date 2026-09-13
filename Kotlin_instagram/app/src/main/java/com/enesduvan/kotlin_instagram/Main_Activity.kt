package com.enesduvan.kotlin_instagram

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.enesduvan.kotlin_instagram.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Main_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        //----------değişken tanımlamalar
        auth = Firebase.auth
        val current_user = auth.currentUser //eğer giriş yapıldıysa bidaha parola isteme
        if (current_user != null){
            val intent = Intent(this@Main_Activity, test_activity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
package com.enesduvan.kotlin_instagram


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.enesduvan.kotlin_instagram.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

//enesduvan@gmail.com
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var password_array :  ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)


        //----------değişken tanımlamalar
        auth = Firebase.auth
        password_array = ArrayList<String>()
        //----------değişken tanımlamalar


        val current_user = auth.currentUser //eğer giriş yapıldıysa bidaha parola isteme
        if (current_user != null){
            val intent = Intent(this@MainActivity, test_activity::class.java)
            startActivity(intent)
            finish()
        }


        //------------kayıt ol fragment
/*
           binding.textView.setOnClickListener {
               //fragment değişimi navigator ile
               val navHostFragment =
                   supportFragmentManager.findFragmentById(R.id.KayitolFragment) as NavHostFragment

               val navController = navHostFragment.navController

               navController.navigate(R.id.blankFragment2) // veya action id

           }*/
        binding.button2.setOnClickListener {
            val intent = Intent(this@MainActivity, Main_Activity::class.java)
            startActivity(intent)

        }
    }

    override fun onStart() {
        super.onStart()
        val current_user = auth.currentUser //eğer giriş yapıldıysa bidaha parola isteme
        if (current_user != null){
            val intent = Intent(this@MainActivity, test_activity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun signin(view: View){

        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        var name = ""
        for (item in email){
            if(item == '@'){
                // burdan sonrası artık @gmail.com
                break
            }
            name += item
        }
        if (email.isNotEmpty())
        {
            if (password.isNotEmpty()){
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        //başarılı giriş
                        Toast.makeText(
                            this@MainActivity,
                            "Hoşgeldin " + name + " !",
                            Toast.LENGTH_SHORT
                        ).show()
                        // kullanıcı başarıyla oluştu
                        val intent = Intent(this@MainActivity, test_activity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Giriş başarısız E-Mail yada şifre hatalı", Toast.LENGTH_SHORT).show() }
                    }
            }else{
                Toast.makeText(this@MainActivity, "Lütfen şifre alanını boş bırakmayın !", Toast.LENGTH_SHORT).show() }
        }else{
            Toast.makeText(this@MainActivity, "Lütfen E-mail alanını boş bırakmayın !", Toast.LENGTH_SHORT).show()
        }
    }
    fun signup(view: View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        var name = ""
        for (item in email){
            if(item == '@'){
                // burdan sonrası artık @gmail.com
                break
            }
            name += item
        }
        if (email.isNotEmpty())
        {
            if (password.isNotEmpty()){
                password_array.add("@")
                password_array.add(".")
                password_array.add(",")
                password_array.add("/")
                password_array.add("+")
                var x = 0
                for (item in password_array){
                    if(item in password){
                        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                            Toast.makeText(this@MainActivity, "Hoşgeldin "+name+" !" ,Toast.LENGTH_SHORT).show()
                            // kullanıcı başarıyla oluştu
                            val intent = Intent(this@MainActivity, test_activity::class.java)
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                            // kullanıcı oluşmadı
                            Toast.makeText(this@MainActivity, "Hata kullanıcı oluşturulamadı !", Toast.LENGTH_SHORT).show()
                        }
                        x = 1
                        break
                    }
                }
                if(x == 0){
                    Toast.makeText(this@MainActivity, "Lütfen şifrenizde ( @ , . , + , / ) karakterleri kullanın !", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this@MainActivity, "Lütfen şifre alanını boş bırakmayın !", Toast.LENGTH_SHORT).show()
            }

        }else{
        Toast.makeText(this@MainActivity, "Lütfen E-mail alanını boş bırakmayın !", Toast.LENGTH_SHORT).show()

        }
    }

}
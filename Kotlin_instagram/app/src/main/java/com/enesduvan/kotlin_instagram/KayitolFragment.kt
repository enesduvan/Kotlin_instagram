package com.enesduvan.kotlin_instagram

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.enesduvan.kotlin_instagram.databinding.ActivityMainBinding
import com.enesduvan.kotlin_instagram.databinding.FragmentKayitolBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class KayitolFragment : Fragment(R.layout.fragment_kayitol) {

    private var _binding: FragmentKayitolBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    private lateinit var password_array :  ArrayList<String>

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                _binding = FragmentKayitolBinding.bind(view)

                //----------değişken tanımlamalar
                auth = Firebase.auth
                password_array = ArrayList<String>()
                //----------değişken tanımlamalar


                val current_user = auth.currentUser //eğer giriş yapıldıysa bidaha parola isteme
                if (current_user != null){
                    val intent = Intent(requireActivity(), test_activity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                binding.singupbutton.setOnClickListener {
                    //kayıt ol button
                    signup()
                }
                binding.backbutton.setOnClickListener {
                    //geri buton
                    back()
                }
            }

            override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
            }
    fun back(){

    }


    fun signup(){
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
                            Toast.makeText(requireActivity(), "Hoşgeldin "+name+" !" ,Toast.LENGTH_SHORT).show()
                            // kullanıcı başarıyla oluştu
                            val intent = Intent(requireActivity(), test_activity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }.addOnFailureListener {
                            // kullanıcı oluşmadı
                            Toast.makeText(requireActivity(), "Hata kullanıcı oluşturulamadı !", Toast.LENGTH_SHORT).show()
                        }
                        x = 1
                        break
                    }
                }
                if(x == 0){
                    Toast.makeText(requireActivity(), "Lütfen şifrenizde ( @ , . , + , / ) karakterleri kullanın !", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(requireActivity(), "Lütfen şifre alanını boş bırakmayın !", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(requireActivity(), "Lütfen E-mail alanını boş bırakmayın !", Toast.LENGTH_SHORT).show()

        }
    }


}

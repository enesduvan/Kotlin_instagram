package com.enesduvan.kotlin_instagram

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.enesduvan.kotlin_instagram.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SigninFragment : Fragment(R.layout.fragment_signin) {
    //class UploadFragment : Fragment(R.layout.fragment_upload)
    //R.layout.Fragment ismi eklenecek

    private var _binding: FragmentSigninBinding? = null
    //Fragment ismi
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var password_array :  ArrayList<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSigninBinding.bind(view)
        //Fragment ismi
        //----------değişken tanımlamalar
        auth = Firebase.auth
        password_array = ArrayList<String>()
        //----------değişken tanımlamalar

        binding.singinbutton.setOnClickListener {
            signin()
        }
        binding.textView.setOnClickListener{
            val action = SigninFragmentDirections.actionSigninFragmentToKayitolFragment()
            Navigation.findNavController(it).navigate(action)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun signin(){

        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        var name =""
        if (name == "") {
            for (item in email) {
                if (item == '@') {
                    // burdan sonrası artık @gmail.com
                    break
                }
                name += item
            }
        }
        if (email.isNotEmpty())
        {
            if (password.isNotEmpty()){
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        //başarılı giriş
                        Toast.makeText(
                            requireActivity(),
                            "Hoşgeldin " + name + " !",
                            Toast.LENGTH_SHORT
                        ).show()
                        // kullanıcı başarıyla oluştu
                        val intent = Intent(requireActivity(), test_activity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireActivity(), "Giriş başarısız E-Mail yada şifre hatalı", Toast.LENGTH_SHORT).show() }
                }
            }else{
                Toast.makeText(requireActivity(), "Lütfen şifre alanını boş bırakmayın !", Toast.LENGTH_SHORT).show() }
        }else{
            Toast.makeText(requireActivity(), "Lütfen E-mail alanını boş bırakmayın !", Toast.LENGTH_SHORT).show()
        }
    }
}



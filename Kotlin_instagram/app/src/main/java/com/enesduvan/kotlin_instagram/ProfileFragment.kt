package com.enesduvan.kotlin_instagram

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enesduvan.kotlin_instagram.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class ProfileFragment : Fragment(R.layout.fragment_profile) {
            private var _binding: FragmentProfileBinding? = null
            //Fragment ismi
            private lateinit var auth: FirebaseAuth
            private val binding get() = _binding!!
            var name_true = ""
            var biograpy_true = ""
            var name = ""

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)

                _binding = FragmentProfileBinding.bind(view)
                //Fragment ismi

                binding.button.setOnClickListener { log_out() }
                binding.duzenleButtonProfile.setOnClickListener { profil_duzenle() }
                binding.duzenleButtonProfile2.setOnClickListener { profil_save() }
                auth = Firebase.auth
                name = AppData_name.kullaniciAdi.toString()
                binding.nameTextProfile.text = name
                binding.biyografiTextProfile.text = "biograpy"
            }

            override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
            }
    fun log_out(){
        auth.signOut()
        val intent = Intent(requireActivity(), Main_Activity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
    fun profil_duzenle(){
        name_true = binding.nameTextProfile.text.toString()
        biograpy_true = binding.biyografiTextProfile.text.toString()
        binding.nameTextProfile.visibility = View.INVISIBLE
        binding.biyografiTextProfile.visibility = View.INVISIBLE
        binding.editTextName.visibility = View.VISIBLE
        binding.editTextBiyografi.visibility = View.VISIBLE
        binding.duzenleButtonProfile2.isClickable = true
        binding.duzenleButtonProfile2.visibility = View.VISIBLE
        binding.editTextName.hint = name_true
        binding.editTextBiyografi.hint = biograpy_true
    }
    fun profil_save(){
        var name = binding.editTextName.text.toString()
        var biograpy = binding.editTextBiyografi.text.toString()
        binding.nameTextProfile.visibility = View.VISIBLE
        binding.biyografiTextProfile.visibility = View.VISIBLE
        binding.editTextName.visibility = View.INVISIBLE
        binding.editTextBiyografi.visibility = View.INVISIBLE
        if (name.isEmpty()){
            name = name_true
        }
        if (biograpy.isEmpty()){
            biograpy = biograpy_true
        }
        binding.nameTextProfile.text = name
        binding.biyografiTextProfile.text = biograpy
        binding.duzenleButtonProfile2.isClickable = false
        binding.duzenleButtonProfile2.visibility = View.INVISIBLE
    }
}
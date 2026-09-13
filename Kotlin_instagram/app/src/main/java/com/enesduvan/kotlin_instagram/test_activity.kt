package com.enesduvan.kotlin_instagram

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.enesduvan.kotlin_instagram.databinding.ActivityTestBinding

class test_activity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTestBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        replace_fragment(HomeFragment() )
        binding.bottomNavigationView2.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replace_fragment(HomeFragment())
                R.id.upload -> replace_fragment(UploadFragment())
                R.id.profile -> replace_fragment(ProfileFragment())

                else -> {

                }

            }
            true
        }
    }
    fun replace_fragment(fragment: Fragment){
        val fragment_manager = supportFragmentManager
        val fragment_transiction = fragment_manager.beginTransaction()
        fragment_transiction.replace(R.id.frameLayout,fragment)
        fragment_transiction.commit()
    }

}
package com.enesduvan.kotlin_instagram

import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.enesduvan.kotlin_instagram.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import java.sql.Timestamp

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var post_array : ArrayList<Post>
    private lateinit var adapter: recycler_adapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)
        db = Firebase.firestore
        auth = Firebase.auth
        post_array = ArrayList<Post>()
        getdata()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        adapter = recycler_adapter(post_array)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getdata(){
        //verileri tarih sırasıyla sıralayarak çektik
        db.collection("posts").orderBy("date_time", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error != null){
                Toast.makeText(requireActivity(),error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if (value != null){
                    if(!value.isEmpty){
                        val documents = value.documents
                        post_array.clear()
                        for (document in documents){
                            //casting
                            val download_url =  document.get("download_url") as String
                            val comment =  document.get("comment") as String
                            val name =  document.get("name") as String
                            val post = Post(name,comment,download_url)
                            post_array.add(post)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}

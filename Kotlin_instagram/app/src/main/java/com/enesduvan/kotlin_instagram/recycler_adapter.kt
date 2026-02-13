package com.enesduvan.kotlin_instagram

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enesduvan.kotlin_instagram.databinding.RecyclerRowBinding
import com.squareup.picasso.Picasso

class recycler_adapter (val recycler_list : ArrayList<Post>): RecyclerView.Adapter<recycler_adapter.Recycler_holder> (){


        class Recycler_holder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {
            //ilk oluşturduğunda ne olacak
        }
        override fun onCreateViewHolder(//recyler_row burada bağlanır (olşturulan layout)
            parent: ViewGroup,
            viewType: Int
        ): Recycler_holder {
            val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return Recycler_holder(binding)
        }

        override fun onBindViewHolder(
            //bağlandıktan sonra ne olacak
            holder: Recycler_holder,
            position: Int
        ) {
            holder.binding.nameText.text = recycler_list.get(position).name

            holder.binding.commentText.text = recycler_list.get(position).comment
            //liste içindeki isimleri al recyler içine yaz
            Picasso.get().load(recycler_list.get(position).download_url).into(holder.binding.imageView)

        }

        override fun getItemCount(): Int {
            //kaç kere kullanılacak
            return recycler_list.size
        }
    }


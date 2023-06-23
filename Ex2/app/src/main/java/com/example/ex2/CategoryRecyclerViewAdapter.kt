package com.example.ex2

import android.app.Activity
import android.view.*
import android.widget.AdapterView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ex2.databinding.ActivityMainBinding
import com.example.ex2.databinding.CategoryListBinding

//메인 액티비티
class CategoryRecyclerViewAdapter(val dataList: MutableList<Category>, val activity: Activity,val onClick : (Int) -> Unit) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryRecyclerViewHolder>(){


    inner class CategoryRecyclerViewHolder(private val binding: CategoryListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.categoryTextView.text = dataList[position].title

            binding.frame.setOnCreateContextMenuListener { contextMenu, view, contextMenuInfo ->
                val p = activity as positionInteface
                p.getPosition(position)
                activity.menuInflater.inflate(R.menu.category_menu_select, contextMenu)
            }

            itemView.setOnClickListener {
                onClick(position)
            }
        }

    }
    override fun onBindViewHolder(holder: CategoryRecyclerViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryRecyclerViewHolder {
        return CategoryRecyclerViewHolder(CategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = dataList.size

}
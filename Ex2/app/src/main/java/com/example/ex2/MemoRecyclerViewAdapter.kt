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
import com.example.ex2.databinding.MemoListBinding

class MemoRecyclerViewAdapter(val dataList: MutableList<Memo>,val title : String? ,val activity: Activity,val  onClick :(Int) -> Unit ) : RecyclerView.Adapter<MemoRecyclerViewAdapter.MemoRecyclerViewHolder>(){


    inner class MemoRecyclerViewHolder(private val binding: MemoListBinding) : RecyclerView.ViewHolder(binding.root){


        fun bind(position: Int){
            binding.categoryTextView.text = title
            binding.memoTextView.text = dataList[position].title

            binding.frame.setOnCreateContextMenuListener { contextMenu, view, contextMenuInfo ->
                val p = activity as menuInterface
                p.getPosition(position)
                activity.menuInflater.inflate(R.menu.memo_menu_select, contextMenu)
            }

            itemView.setOnClickListener {
                onClick(position)
            }
        }

    }

    override fun onBindViewHolder(holder: MemoRecyclerViewHolder, position: Int) {
        holder.bind(position)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoRecyclerViewHolder {
        return MemoRecyclerViewHolder(MemoListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = dataList.size

}
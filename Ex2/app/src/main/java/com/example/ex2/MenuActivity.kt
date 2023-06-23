package com.example.ex2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ex2.databinding.ActivityMenuBinding

class MenuActivity() : AppCompatActivity(), menuInterface{


    @SuppressLint("NotifyDataSetChanged")
    private val requestLauncher  = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode == Activity.RESULT_OK){
            val memo = it.data?.getStringExtra("memo")
            memo?.let { memo ->
                category?.memo?.add(memo)
            }
            binding.memoRecycler.adapter?.notifyDataSetChanged()
        }

        if(it.resultCode == Activity.RESULT_OK + 1){
            val memo = it.data?.getStringExtra("rename")
            val position = it.data?.getIntExtra("position", -1)


            position?.let {
                if (position != -1){
                    memo?.let {memo ->
                        category?.memo?.set(it, memo)
                    }
                }
            }

            binding.memoRecycler.adapter?.notifyDataSetChanged()

        }
    }
    var position : Int? = null
    private lateinit var binding : ActivityMenuBinding
    var category : Category? = null
    var categoryPosition : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("categoryToMemo", Category::class.java)
        } else {
            intent.getParcelableExtra<Category>("categoryToMemo")
        }

        categoryPosition = intent.getIntExtra("categoryToMemoPosition", -1)

        val copyCategory = category as Category
        //Log.d("memoActivity", "${copyCategory.memo.size}")
        //Log.d("memoActivity", "$category, $categoryPosition")

        binding.memoRecycler.apply {
            adapter = category?.memo?.let {memo -> MemoRecyclerViewAdapter(memo, category?.title, this@MenuActivity) }
            layoutManager = LinearLayoutManager(this@MenuActivity)
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Toast.makeText(this@MenuActivity, "메모 액티비티 종료", Toast.LENGTH_SHORT).show()
                intent.putExtra("memoToCategory", category)
                intent.putExtra("position", categoryPosition)
                setResult(900, intent)
                finish()
            }
        })

    }


    override fun onDestroy() {

        super.onDestroy()
    }


    //예전 버전 백스택 사용할 때
    override fun onBackPressed() {
        //Log.d("onBackPress", "$category, $categoryPosition")
        category?.let {
            //Toast.makeText(this, "메모 액티비티 종료", Toast.LENGTH_SHORT).show()
            intent.putExtra("memoToCategory", Category(it.title, it.memo))
            intent.putExtra("position", categoryPosition)
            setResult(900, intent)
            finish()
        }

        return super.onBackPressed()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.memo_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.registerMemo ->{
                val intent = Intent(this, addMemo::class.java)
                requestLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_select_list1 -> {
                category?.let {category ->
                    position?.let { pos ->
                        val intent = Intent(this, renameMemo::class.java)
                        intent.putExtra("title", category.title)
                        intent.putExtra("renameMemo", category.memo[pos])
                        intent.putExtra("position", pos)
                        requestLauncher.launch(intent)
                    }

                }
                true
            }
            R.id.menu_select_list2 -> {
                position?.let {
                    category?.memo?.removeAt(it)
                }
                binding.memoRecycler.adapter?.notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }

    }



    override fun getPosition(position: Int) {
        this.position = position
    }

}

interface menuInterface{
    fun getPosition(position : Int)
}
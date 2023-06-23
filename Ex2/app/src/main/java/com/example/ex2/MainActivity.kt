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
import com.example.ex2.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity(), positionInteface {
    private lateinit var binding : ActivityMainBinding
    private var dataList  = mutableListOf<Category>()
    var position : Int? = null


    @SuppressLint("NotifyDataSetChanged")
    private val requestLauncher  = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        //add Category
        if(it.resultCode == Activity.RESULT_OK){
            val resultData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                it.data?.getParcelableExtra("category", Category::class.java)
            }else{
                it.data?.getParcelableExtra<Category>("category")
            }
            //Category?

            //Toast.makeText(this, "받아온 데이터 $resultData", Toast.LENGTH_SHORT).show()
            resultData?.let { Category ->
                dataList.add(Category)
            }

            //Toast.makeText(this, "데이터 사이즈 ${dataList.size}", Toast.LENGTH_SHORT).show()
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }

        if(it.resultCode == Activity.RESULT_OK + 1){
            val resultData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                it.data?.getParcelableExtra("rename", Category::class.java)
            }else{
                it.data?.getParcelableExtra<Category>("rename")
            }

            val position = it.data?.getIntExtra("position", -1)
            //Log.d("renameCategory", "category : ${resultData?.toString()}, position : ${position}")

            position?.let { p ->
                resultData?.let { category ->
                    if(p != -1){
                        dataList[p] = category
                    }
                }
            }
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }

        //메모 액티비티 실행 결과 반환값
        if(it.resultCode == 900){

            //Toast.makeText(this, "메모 액티비티 종료로 여기가 실행된다", Toast.LENGTH_SHORT).show()
            val resultData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                it.data?.getParcelableExtra("memoToCategory", Category::class.java)
            }else{
                it.data?.getParcelableExtra<Category>("memoToCategory")
            }

            //val category = resultData as Category
            val pos = it.data?.getIntExtra("position", -1)
            resultData?.let { category ->
                //Log.d("memoToCategory", "${category.memo.size}")
                pos?.let {p ->
                    dataList.set(p ,category)
                }
            }


        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()

        //Log.d("MainActivity", "${dataList.size}")

        binding.recyclerView.apply {
            adapter = CategoryRecyclerViewAdapter(dataList, this@MainActivity){
                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                intent.putExtra("categoryToMemo", dataList[it])
                intent.putExtra("categoryToMemoPosition", it)
                requestLauncher.launch(intent)
            }
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                saveData()
                finish()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.category_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.toCategory -> {
                val intent = Intent(this, addCategory::class.java)
                //startActivity(intent)
                requestLauncher.launch(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }



    @SuppressLint("NotifyDataSetChanged")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.category_select_list1 ->{
                //Toast.makeText(this, "$position 카테고리 누름", Toast.LENGTH_SHORT).show()

                position?.let {

                    val intent2 = Intent(this, renameCategory::class.java)
                    intent2.putExtra("renameCategory", dataList[it])
                    intent2.putExtra("position", position)

                    requestLauncher.launch(intent2)
                }
                true
            }
            R.id.category_select_list2 ->{
                //Toast.makeText(this, "${position} 카테고리 삭제 누름", Toast.LENGTH_SHORT).show()
                position?.let {
                    dataList.removeAt(it)
                }
                binding.recyclerView.adapter?.notifyDataSetChanged()
                true
            }
            else ->{super.onContextItemSelected(item)}
        }
    }

    override fun getPosition(position: Int) {
        this.position = position
    }


    //데이터 저장
    fun saveData(){
        val shared = getSharedPreferences("category", MODE_PRIVATE)
        val token = object  : TypeToken<MutableList<Category>>(){}
        val gson = Gson().toJson(dataList, token.type)

        shared.edit().apply {
            putString("categoryList", gson)
            apply()
        }
    }

    //데이터 읽기
    fun getData(){
        val shared = getSharedPreferences("category", MODE_PRIVATE)
        val jsonData = shared.getString("categoryList", "")

        if(jsonData != ""){
            val token = object  : TypeToken<MutableList<Category>>(){}
            val gson = Gson()
            val list : MutableList<Category> = gson.fromJson(jsonData, token.type)
            list.let {
                dataList = it
            }
        }
    }

    override fun onDestroy() {
        saveData()
        super.onDestroy()
    }
}

interface positionInteface{
    fun getPosition(position : Int)
}

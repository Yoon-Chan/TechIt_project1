package com.example.ex2

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.ex2.databinding.ActivityRenameCategoryBinding
import kotlin.concurrent.thread

class renameCategory : AppCompatActivity() {
    private lateinit var binding : ActivityRenameCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRenameCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("renameCategory", Category::class.java)
        }else{
            intent.getParcelableExtra("renameCategory")
        }

        val position = intent.getIntExtra("position", -1)


        //변경 전 이름
        binding.titleTextView.text = category?.title

        val imn = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //에디트 텍스트에 포커스 두기
        requestFocus(imn)


        //텍스트 완료 눌렀을 때
        editTextButton(imn, position)

        //버튼 확인 눌렀을 때
        selectButton(imn, position)

        //버튼 취소 눌렀을
        cancelButton(imn)
    }

    private fun cancelButton(imn: InputMethodManager) {
        binding.cancelButton.setOnClickListener {
            hideFocus(imn)
            finish()
        }
    }

    private fun selectButton(imn: InputMethodManager, position: Int) {
        binding.sendButton.setOnClickListener {
            if(binding.titleEditTextView.text.toString().isEmpty()){
                Toast.makeText(this, "빈 값은 입력할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }else{
                val rename = binding.titleEditTextView.text.toString()
                intent.putExtra("rename", Category(rename))
                intent.putExtra("position", position)
                hideFocus(imn)
                setResult(RESULT_OK+1, intent)
                finish()
            }
        }
    }

    private fun editTextButton(imn: InputMethodManager, position : Int) {
        binding.titleEditTextView.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_DONE){
                if(binding.titleEditTextView.text.toString().isEmpty()){
                    Toast.makeText(this, "빈 값은 입력할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    false
                }else{
                    val rename = binding.titleEditTextView.text.toString()
                    intent.putExtra("rename", Category(rename))
                    intent.putExtra("position", position)
                    hideFocus(imn)

                    setResult(RESULT_OK+1, intent)
                    finish()
                    true
                }
            }else{
                false
            }
        }
    }

    fun requestFocus(imn : InputMethodManager){
        binding.titleEditTextView.requestFocus()
        thread {
            SystemClock.sleep(500)
            imn.showSoftInput(currentFocus, 0)
        }
    }

    fun hideFocus(imn: InputMethodManager){
        imn.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        binding.titleEditTextView.clearFocus()
    }
}
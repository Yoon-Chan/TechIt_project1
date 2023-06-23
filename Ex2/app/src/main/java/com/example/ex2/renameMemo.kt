package com.example.ex2

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.example.ex2.databinding.ActivityRenameMemoBinding
import kotlin.concurrent.thread

class renameMemo : AppCompatActivity() {
    private lateinit var binding : ActivityRenameMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRenameMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val memo = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra("memo", Memo::class.java)
        }else{
            intent.getParcelableExtra<Memo>("memo")
        }
        val position = intent.getIntExtra("position", -1)

        val title = intent.getStringExtra("title")

        //변경 전 이름
        binding.categoryTitleTextView.text = title
        binding.titleEditTextView.setText(memo?.title)
        binding.contentEditTextView.setText(memo?.content)

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
                val renameMemo = Memo(binding.titleEditTextView.text.toString(), binding.contentEditTextView.text.toString())
                intent.putExtra("rename", renameMemo)
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
                    val renameMemo = Memo(binding.titleEditTextView.text.toString(), binding.contentEditTextView.text.toString())
                    intent.putExtra("rename", renameMemo)
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
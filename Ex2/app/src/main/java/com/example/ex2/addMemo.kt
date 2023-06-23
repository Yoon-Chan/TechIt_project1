package com.example.ex2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.ex2.databinding.ActivityAddMemoBinding
import kotlin.concurrent.thread

class addMemo : AppCompatActivity() {

    private lateinit var binding : ActivityAddMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imn : InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //자동으로 에디트 텍스트에 포커스 설정하기
        requestFocus(imn)

        //확인 버튼
        clickSendButton(imn)

        //취소 버튼
        clickCancelButton(imn)

        //에디트 텍스트 확인 버튼 클릭 시
        clickEditTextButton(imn)
    }

    //포커스 잡기
    fun requestFocus(imn : InputMethodManager){
        binding.titleEditTextView.requestFocus()
        thread {
            SystemClock.sleep(500)
            imn.showSoftInput(currentFocus,0)
        }
    }

    fun hideFocus(imn: InputMethodManager){
        imn.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        binding.titleEditTextView.clearFocus()
    }

    fun clickSendButton(imn: InputMethodManager){
        binding.sendButton.setOnClickListener {
            if(binding.titleEditTextView.text.isEmpty()){
                Toast.makeText(this, "빈 값을 입력하면 안됩니다.", Toast.LENGTH_SHORT).show()
            }else{
                val memo = binding.titleEditTextView.text.toString()
                intent.putExtra("memo", memo)
                Toast.makeText(this, "저장 완료!!", Toast.LENGTH_SHORT).show()
                hideFocus(imn)

                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    fun clickCancelButton(imn: InputMethodManager){
        binding.cancelButton.setOnClickListener {
            hideFocus(imn)
            finish()
        }
    }

    fun clickEditTextButton(imn: InputMethodManager){
        binding.titleEditTextView.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_DONE){

                if(binding.titleEditTextView.text.isEmpty()){
                    Toast.makeText(this, "빈 값을 입력하면 안됩니다.", Toast.LENGTH_SHORT).show()
                    false
                }else{
                    val memo = binding.titleEditTextView.text.toString()
                    intent.putExtra("memo", memo)
                    Toast.makeText(this, "저장 완료!!", Toast.LENGTH_SHORT).show()
                    hideFocus(imn)

                    setResult(RESULT_OK, intent)
                    finish()
                    true
                }

                //true
            }else{
                false
            }
        }
    }
}
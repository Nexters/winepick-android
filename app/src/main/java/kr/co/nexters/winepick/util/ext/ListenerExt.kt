package kr.co.nexters.winepick.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.onTextChangeListener(onTextChanged: (s: CharSequence?) -> Unit={}){
    addTextChangedListener(object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s)
        }
    })
}
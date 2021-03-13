package kr.co.nexters.winepick.util

import android.content.Context
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.util.ui.OnSingleClickListener

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.setRound(curveRadius: Float) {
    outlineProvider = object : ViewOutlineProvider() {

        override fun getOutline(view: View?, outline: Outline?) {
            outline?.setRoundRect(1.dpToPx(), 1.dpToPx(), view!!.width, view.height, curveRadius)
        }
    }
    clipToOutline = true
}

@BindingAdapter("app:isInvisible")
fun View.setInvisible(isInvisible: Boolean) {
    this.isInvisible = isInvisible
}

/** 1초의 인터벌을 가지는 클릭 리스너를 등록한다. */
@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(onClickAction: (() -> Unit)?) {
    onClickAction?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

/** ListAdapter 사용 시 사용(아이템 비교 및 대체) */
@Suppress("UNCHECKED_CAST")
@BindingAdapter("submitList")
fun RecyclerView.submitList(items: List<Any>?) {
    (adapter as? ListAdapter<Any, *>)?.submitList(items)
}

/** 일반 뷰에서 키보드 숨기기 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

@BindingAdapter(
    value = ["beforeTextChanged", "onTextChanged", "onAfterTextChanged"],
    requireAll = false
)
fun AppCompatEditText.setTextWatcher(
    beforeTextChanged: TextViewBindingAdapter.BeforeTextChanged?,
    onTextChanged: TextViewBindingAdapter.OnTextChanged?,
    afterTextChanged: TextViewBindingAdapter.AfterTextChanged?
) {
    addTextChangedListener(
        beforeTextChanged = { charSequence: CharSequence?, start: Int, count: Int, after: Int ->
            beforeTextChanged?.beforeTextChanged(charSequence, start, count, after)
        }, onTextChanged = { charSequence: CharSequence?, start: Int, before: Int, count: Int ->
            onTextChanged?.onTextChanged(charSequence, start, before, count)
        }, afterTextChanged = { text ->
            afterTextChanged?.afterTextChanged(text)
        })
}

@BindingAdapter("isSelected")
fun View.setSelected(newIsSelected: Boolean) {
    this.isSelected = newIsSelected
}

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}


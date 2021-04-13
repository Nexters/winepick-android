package kr.co.nexters.winepick.util

import android.content.Context
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.ui.survey.SurveyFragment
import kr.co.nexters.winepick.util.ui.OnSingleClickListener
import org.jetbrains.annotations.NotNull


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

/** 일반 뷰에서 키보드 보이기 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
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

@BindingAdapter("imageDrawableResource")
fun setImageDrawableResource(imageView: ImageView, resource: Int) {
    val drawable = ResourcesCompat.getDrawable(imageView.context.resources, resource, null)
    imageView.setImageDrawable(drawable)
}

/**
 * 설문 항목 아이템 클릭 시 동작하는 애니메이션 로직
 * 코드적으로도, 애니메이션적으로도 조잡해보이기도 하여 내부 논의 후 반영 예정
 */
fun startSurveyAnswerClickAnimation(
    button: @NotNull Button,
    surveyFragment: SurveyFragment
) {
    surveyFragment.uiScope.launch {
        button.setBackgroundResource(R.drawable.set_radius_black_button)
        button.setTextColor(ContextCompat.getColor(button.context, R.color.colorWhite))
        delay(150)
        button.setBackgroundResource(R.drawable.set_radius_gray_button)
        button.setTextColor(ContextCompat.getColor(button.context, R.color.colorGray82))
        delay(150)
        button.setBackgroundResource(R.drawable.set_radius_black_button)
        button.setTextColor(ContextCompat.getColor(button.context, R.color.colorWhite))
        delay(150)
        button.setBackgroundResource(R.drawable.set_radius_gray_button)
        button.setTextColor(ContextCompat.getColor(button.context, R.color.colorGray82))
        delay(150)
        button.setBackgroundResource(R.drawable.set_radius_black_button)
        button.setTextColor(ContextCompat.getColor(button.context, R.color.colorWhite))
    }
}


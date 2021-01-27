package kr.co.nexters.winepick.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider

/**
 * BaseActivity
 *
 * @param layoutResId 레이아웃 Resource Id
 *
 * @property binding 바인딩 객체
 * @property viewModel ViewModel 객체
 * @property viewModelFactory ViewModel 을 사용하기 위해 반드시 필요한 ViewModelFactory
 *
 * @since v1.0.0 / 2021.01.17
 */
abstract class BaseActivity<B : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : AppCompatActivity() {
    protected lateinit var binding: B

    abstract val viewModel: BaseViewModel?
    val viewModelFactory: ViewModelProvider.AndroidViewModelFactory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
    }
}

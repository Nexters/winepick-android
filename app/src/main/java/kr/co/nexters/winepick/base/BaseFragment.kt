package kr.co.nexters.winepick.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider

/**
 * BaseFragment
 *
 * @param layoutResId 레이아웃 Resource Id
 *
 * @property binding 바인딩 객체
 *
 * @since v1.0.0 / 2020.01.28
 */
abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes val layoutResId: Int
) : Fragment() {
    lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
    }
}

/**
 * 특정 프레그먼트를 띄워준다.
 *
 * @param fm FragmentManager
 * @param id 프레그먼트가 들어갈 레이아웃 id
 */
fun BaseFragment<*>.navigate(fm: FragmentManager, @IdRes id: Int): Int {
    return fm.run {
        beginTransaction()
            .replace(id, this@navigate, this@navigate::class.simpleName)
            .commit()
    }
}

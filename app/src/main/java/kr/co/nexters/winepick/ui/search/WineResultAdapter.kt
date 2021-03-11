package kr.co.nexters.winepick.ui.search

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.databinding.ItemWineResultBinding
import kr.co.nexters.winepick.ui.base.WineResultViewModel
import kr.co.nexters.winepick.util.setOnSingleClickListener

/**
 * 검색 결과 아이템 recyclerview adapter
 *
 * @since v1.0.0 / 2021.02.08
 */
class WineResultAdapter(val vm: WineResultViewModel) :
    ListAdapter<WineResult, WineResultViewHolder>(SearchResultDiffUtilCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WineResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemWineResultBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_wine_result, parent, false)

        return WineResultViewHolder(binding).apply {
            binding.root.setOnSingleClickListener {
                vm.wineItemViewClick(binding.wineResult!!)
            }

            binding.btnSearchResultHeart.setOnSingleClickListener {
                // 미연의 클릭 방지를 위해 강제로 toggle 처리한다.
                binding.wineResult = binding.wineResult!!.apply { likeYn = !this.likeYn!! }
                vm.wineHeartClick(binding.wineResult!!)
            }
        }
    }

    override fun onBindViewHolder(holder: WineResultViewHolder, position: Int) {
        holder.bind(getItem(position), vm)
    }
}

class WineResultViewHolder(private val binding: ItemWineResultBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(wineResult: WineResult, vm: WineResultViewModel) {
        binding.wineResult = wineResult
        binding.vm = vm
    }
}

object SearchResultDiffUtilCallBack : DiffUtil.ItemCallback<WineResult>() {
    override fun areItemsTheSame(
        oldItem: WineResult,
        newItem: WineResult
    ): Boolean {
        return oldItem.nmKor == newItem.nmKor
    }

    override fun areContentsTheSame(
        oldItem: WineResult,
        newItem: WineResult
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}

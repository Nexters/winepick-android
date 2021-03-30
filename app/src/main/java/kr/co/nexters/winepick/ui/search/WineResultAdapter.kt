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
        when(wineResult.category) {
            "레드" -> {
                wineResult.wineImg = R.drawable.img_red
            }
            "화이트" -> {
                wineResult.wineImg = R.drawable.img_white
            }
            "스파클링" -> {
                wineResult.wineImg = R.drawable.img_sparkling
            }
        }

        when(wineResult.country) {
            "미국" -> {wineResult.wineCountryImg = R.drawable.us_icon}
            "이탈리아" -> {wineResult.wineCountryImg = R.drawable.italy_icon}
            "남아프리카공화국" -> {wineResult.wineCountryImg = R.drawable.south_africa_icon}
            "호주" -> { wineResult.wineCountryImg = R.drawable.australia_icon }
            "이탈리아" -> {wineResult.wineCountryImg = R.drawable.italy_icon}
            "칠레" -> {wineResult.wineCountryImg = R.drawable.chile_icon}
            "스페인" -> {wineResult.wineCountryImg = R.drawable.spain_icon}
            "뉴질랜드" -> {wineResult.wineCountryImg = R.drawable.new_zealand_icon}
            "캐나다" -> {wineResult.wineCountryImg = R.drawable.canada_icon}
            "프랑스" -> {wineResult.wineCountryImg = R.drawable.france_icon}
            "오스트리아" -> {wineResult.wineCountryImg = R.drawable.australia_icon}
            "독일" -> {wineResult.wineCountryImg = R.drawable.germany_icon}
            "포루투갈" -> {wineResult.wineCountryImg = R.drawable.portugal_icon}
            "아르헨티나" -> {wineResult.wineCountryImg= R.drawable.argentina_icon}

        }
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

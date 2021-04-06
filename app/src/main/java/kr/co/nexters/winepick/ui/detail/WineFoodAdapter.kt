package kr.co.nexters.winepick.ui.detail

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
import kr.co.nexters.winepick.data.model.WineFood
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.databinding.ItemWineFoodBinding
import kr.co.nexters.winepick.databinding.ItemWineFoodBindingImpl
import kr.co.nexters.winepick.databinding.ItemWineResultBinding
import kr.co.nexters.winepick.ui.base.WineResultViewModel
import kr.co.nexters.winepick.util.setOnSingleClickListener


class WineFoodAdapter(val vm: WineDetailViewModel) :
    ListAdapter<WineFood, WindFoodViewHolder>(WineFoodDiffUtilCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WindFoodViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemWineFoodBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_wine_food, parent, false)

        return WindFoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WindFoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class WindFoodViewHolder(private val binding: ItemWineFoodBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(wineFood: WineFood) {
        binding.wineFood = wineFood
        when(wineFood.title) {
            "레드" -> {
                wineFood.img = R.drawable.img_red_small
                wineFood.title = "Red Wine"
            }
            "화이트" -> {
                wineFood.img = R.drawable.img_white_small
                wineFood.title = "White Wine"
            }
            "스파클링" -> {
                wineFood.img = R.drawable.img_sparkling_small
                wineFood.title = " Sparkling Wine"
            }
            "테이블 와인" -> {
                wineFood.img = R.drawable.img_table
                wineFood.title = "Table"

            }
            "디저트 와인" -> {
                wineFood.img = R.drawable.img_dessert
                wineFood.title = "Dessert"

            }
            "에피타이저" -> {
                wineFood.img = R.drawable.img_dessert
                wineFood.title = "Appetizer"
            }
            else -> {
                wineFood.img = wineFood.img ?: R.drawable.bread_icon
            }
        }
    }
}

object WineFoodDiffUtilCallBack : DiffUtil.ItemCallback<WineFood>() {
    override fun areItemsTheSame(
        oldItem: WineFood,
        newItem: WineFood
    ): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(
        oldItem: WineFood,
        newItem: WineFood
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}

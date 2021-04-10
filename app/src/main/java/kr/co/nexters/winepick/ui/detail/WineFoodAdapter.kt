package kr.co.nexters.winepick.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.model.WineFood
import kr.co.nexters.winepick.databinding.ItemWineFeatureBinding
import kr.co.nexters.winepick.databinding.ItemWineFoodBinding


class WineFoodAdapter(val type: WineListType, val vm: WineDetailViewModel) :
    ListAdapter<WineFood, RecyclerView.ViewHolder>(WineFoodDiffUtilCallBack) {
    companion object {
        const val TYPE_FEATURE = 0
        const val TYPE_FOOD = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (type == WineListType.FEATURE) TYPE_FEATURE else TYPE_FOOD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_FEATURE -> {
                val binding: ItemWineFeatureBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.item_wine_feature, parent, false
                )

                WineFeatureViewHolder(binding)
            }
            else -> {
                val binding: ItemWineFoodBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.item_wine_food, parent, false
                )

                WineFoodViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_FEATURE -> {
                (holder as WineFeatureViewHolder).bind(getItem(position))
            }
            else -> {
                (holder as WineFoodViewHolder).bind(getItem(position))
            }
        }
    }
}

class WineFoodViewHolder(private val binding: ItemWineFoodBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(wineFood: WineFood) {
        binding.wineFood = wineFood
        wineFood.img = wineFood.img ?: R.drawable.food_other
    }
}

class WineFeatureViewHolder(private val binding: ItemWineFeatureBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(wineFood: WineFood) {
        binding.wineFood = wineFood
        when (wineFood.title) {
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
                wineFood.img = wineFood.img ?: R.drawable.img_table
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

enum class WineListType { FEATURE, FOOD }

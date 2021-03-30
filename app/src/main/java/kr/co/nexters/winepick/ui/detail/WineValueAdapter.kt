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
import kr.co.nexters.winepick.data.model.remote.wine.WineValue
import kr.co.nexters.winepick.databinding.ItemWineResultBinding
import kr.co.nexters.winepick.databinding.ItemWineValueBindingImpl
import kr.co.nexters.winepick.ui.base.WineResultViewModel
import kr.co.nexters.winepick.ui.search.WineResultViewHolder
import kr.co.nexters.winepick.util.setOnSingleClickListener


class WineValueAdapter(val vm: WineDetailViewModel) :
    ListAdapter<WineValue, WineValueViewHolder>(WineValueDiffUtilCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WineValueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemWineValueBindingImpl =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_wine_value, parent, false)

        return WineValueViewHolder(binding).apply {


        }
    }

    override fun onBindViewHolder(holder: WineValueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class WineValueViewHolder(private val binding: ItemWineValueBindingImpl) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(wineValue: WineValue) {
        binding.wineValue = wineValue

    }
}

object WineValueDiffUtilCallBack : DiffUtil.ItemCallback<WineValue>() {
    override fun areItemsTheSame(
        oldItem: WineValue,
        newItem: WineValue
    ): Boolean {
        return oldItem.wineTitle == newItem.wineTitle
    }

    override fun areContentsTheSame(
        oldItem: WineValue,
        newItem: WineValue
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}

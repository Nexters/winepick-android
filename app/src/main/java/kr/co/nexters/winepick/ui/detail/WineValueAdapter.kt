package kr.co.nexters.winepick.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.model.remote.wine.WineValue
import kr.co.nexters.winepick.databinding.ItemWineValueBinding

/**
 * WineValueAdapter
 *
 * @since v1.0.0 / 2021.03.30
 */
class WineValueAdapter(val vm: WineDetailViewModel) :
    ListAdapter<WineValue, WineValueViewHolder>(WineValueDiffUtilCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WineValueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemWineValueBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_wine_value, parent, false)

        return WineValueViewHolder(binding).apply {
            binding.imgWineDetailIcon.setOnClickListener {
                binding.wineValue = binding.wineValue!!.apply { hintVisible = !this.hintVisible!! }
            }
        }
    }

    override fun onBindViewHolder(holder: WineValueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class WineValueViewHolder(private val binding: ItemWineValueBinding) :
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

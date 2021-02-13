package kr.co.nexters.winepick.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ItemSearchRelativeBinding
import kr.co.nexters.winepick.util.ext.htmlUnStyling
import kr.co.nexters.winepick.util.setOnSingleClickListener

/**
 * 검색 자동완성 아이템 recyclerview adapter
 *
 * @since v1.0.0 / 2021.02.08
 */
class SearchRelativeAdapter(val vm: SearchViewModel) :
    ListAdapter<String, SearchRelativeViewHolder>(SearchRelativeDiffUtilCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRelativeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSearchRelativeBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_search_relative, parent, false)

        return SearchRelativeViewHolder(binding).apply {
            itemView.setOnSingleClickListener {
                vm.querySearchClick(binding.searchRelative!!.htmlUnStyling())
            }
        }
    }

    override fun onBindViewHolder(holder: SearchRelativeViewHolder, position: Int) {
        holder.bind(getItem(position), vm)
    }
}

class SearchRelativeViewHolder(private val binding: ItemSearchRelativeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(searchRelative: String, vm: SearchViewModel) {
        binding.searchRelative = searchRelative
        binding.vm = vm
    }
}

object SearchRelativeDiffUtilCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}

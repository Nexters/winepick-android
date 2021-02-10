package kr.co.nexters.winepick.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.model.local.SearchCurrent
import kr.co.nexters.winepick.databinding.ItemSearchCurrentBinding
import kr.co.nexters.winepick.databinding.ItemSearchResultBinding
import kr.co.nexters.winepick.util.ext.htmlUnStyling
import kr.co.nexters.winepick.util.setOnSingleClickListener

/**
 * 검색 결과 아이템 recyclerview adapter
 *
 * @since v1.0.0 / 2021.02.08
 */
class SearchCurrentAdapter(val vm: SearchViewModel) :
    ListAdapter<SearchCurrent, SearchCurrentViewHolder>(SearchCurrentDiffUtilCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCurrentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSearchCurrentBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_search_current, parent, false)

        return SearchCurrentViewHolder(binding).apply {
            itemView.setOnSingleClickListener {
                vm.querySearchClick(binding.searchCurrent!!.value.htmlUnStyling())
            }
        }
    }

    override fun onBindViewHolder(holder: SearchCurrentViewHolder, position: Int) {
        holder.bind(getItem(position), vm)
    }
}

class SearchCurrentViewHolder(private val binding: ItemSearchCurrentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(searchCurrent: SearchCurrent, vm: SearchViewModel) {
        binding.searchCurrent = searchCurrent
        binding.vm = vm
    }
}

object SearchCurrentDiffUtilCallBack : DiffUtil.ItemCallback<SearchCurrent>() {
    override fun areItemsTheSame(
        oldItem: SearchCurrent,
        newItem: SearchCurrent
    ): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(
        oldItem: SearchCurrent,
        newItem: SearchCurrent
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}

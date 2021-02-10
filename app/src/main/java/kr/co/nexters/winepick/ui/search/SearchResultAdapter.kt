package kr.co.nexters.winepick.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.model.wine.WineResult
import kr.co.nexters.winepick.databinding.ItemSearchResultBinding

/**
 * 검색 결과 아이템 recyclerview adapter
 *
 * @since v1.0.0 / 2021.02.08
 */
class SearchResultAdapter(val vm: SearchViewModel) :
    ListAdapter<WineResult, SearchResultViewHolder>(SearchResultDiffUtilCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSearchResultBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_search_result, parent, false)

        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position), vm)
    }
}

class SearchResultViewHolder(private val binding: ItemSearchResultBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(searchResult: WineResult, vm: SearchViewModel) {
        binding.searchResult = searchResult
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

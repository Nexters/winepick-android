package kr.co.nexters.winepick.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.databinding.ItemSearchResultBinding
import kr.co.nexters.winepick.util.setOnSingleClickListener

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

        return SearchResultViewHolder(binding).apply {
            binding.btnSearchResultHeart.setOnSingleClickListener {
                vm.addLike(binding.searchResult!!.id!!)
                binding.btnSearchResultHeart.setBackgroundResource(R.drawable.like_success)

            }
        }
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

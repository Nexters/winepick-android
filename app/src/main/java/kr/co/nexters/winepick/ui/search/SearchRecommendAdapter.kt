package kr.co.nexters.winepick.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ItemSearchRecommendBinding
import kr.co.nexters.winepick.util.setOnSingleClickListener

/**
 * 검색 추천 아이템 recyclerview adapter
 *
 * @since v1.0.0 / 2021.02.08
 */
class SearchRecommendAdapter(val vm: SearchViewModel) :
    ListAdapter<String, SearchRecommendViewHolder>(SearchRecommendDiffUtilCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecommendViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSearchRecommendBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_search_recommend, parent, false)

        return SearchRecommendViewHolder(binding).apply {
            itemView.setOnSingleClickListener {
                Toast.makeText(parent.context, "${binding.searchRecommend} 클릭", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onBindViewHolder(holder: SearchRecommendViewHolder, position: Int) {
        holder.bind(getItem(position), vm)
    }
}

class SearchRecommendViewHolder(private val binding: ItemSearchRecommendBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(searchRecommand: String, vm: SearchViewModel) {
        binding.searchRecommend = searchRecommand
        binding.vm = vm
    }
}

object SearchRecommendDiffUtilCallBack : DiffUtil.ItemCallback<String>() {
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

package kr.co.nexters.winepick.type

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.util.inflate

class RecentSearchAdapter() : RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {

    private var data : List<WineResult> = emptyList()

    fun initData(data: List<WineResult>){
        this.data = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, data: WineResult, pos : Int)
    }
    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(parent.inflate(R.layout.item_recent_search))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position],listener)
        holder.setIsRecyclable(false)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val txt = view.findViewById<TextView>(R.id.tv_rv_recent_title)
        fun bind(item: WineResult, listener: OnItemClickListener?){
            txt.text = item.nmKor
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                    notifyDataSetChanged()
                }
            }
        }
    }
}
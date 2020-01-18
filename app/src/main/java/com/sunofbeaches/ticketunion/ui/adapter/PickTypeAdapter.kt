package com.sunofbeaches.ticketunion.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.model.domain.PickTypeItem

class PickTypeAdapter : RecyclerView.Adapter<PickTypeAdapter.InnerHolder>() {

    private val types: ArrayList<PickTypeItem.Data> = ArrayList()

    private var currentSelectedTypeId: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_picker_type, parent, false)
        return InnerHolder(itemView)
    }

    override fun getItemCount(): Int {
        return types.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {

        if (currentSelectedTypeId != null && currentSelectedTypeId == types[position].favorites_id) {
            holder.itemView.setBackgroundColor(Color.parseColor("#EFEEEE"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        val text = holder.itemView.findViewById<TextView>(R.id.type_item_text)
        text.text = types[position].favorites_title
        holder.itemView.setOnClickListener {
            listener?.onTypeItemClick(types[position])
        }
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onTypeItemClick(item: PickTypeItem.Data)
    }

    fun setData(pickTypeItem: PickTypeItem) {
        types.clear()
        types.addAll(pickTypeItem.data)
        currentSelectedTypeId = types[0].favorites_id
        notifyDataSetChanged()
    }

    fun updateSelectedId(favoritesId: Int) {
        this.currentSelectedTypeId = favoritesId
        notifyDataSetChanged()
    }

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
package com.hilton.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryKeyListAdapter(private val keys: MutableList<String>) :
    RecyclerView.Adapter<HistoryKeyListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keyTv = itemView.findViewById<TextView>(R.id.key_txt)
    }

    fun getData(): MutableList<String> {
        return keys
    }

    fun addKey(key: String) {
        if (keys.contains(key)){
            keys.remove(key)
        }
        if (keys.size >= 5) {
            keys.removeLast()
        }
        keys.add(0, key)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.key_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (keys.size > 5) {
            5
        } else {
            keys.size
        }
    }

    var onItemClicked: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = keys[position]
        holder.keyTv.text = key

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(key)
        }
    }
}

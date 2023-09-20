package com.hilton.demo.searchresult

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hilton.demo.R
import com.hilton.rocketreserver.JobListQuery

class JobListAdapter(private val jobs: List<JobListQuery.Job>) :
    RecyclerView.Adapter<JobListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.findViewById<TextView>(R.id.title_txt)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    var onItemClicked: ((JobListQuery.Job) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobs[position]
        holder.titleTv.text = job.title

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(job)
        }
    }
}

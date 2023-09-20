package com.hilton.demo.searchresult

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hilton.demo.JobDetailActivity
import com.hilton.demo.R
import com.hilton.rocketreserver.JobListQuery
import java.lang.reflect.Type

class SearchResultListActivity : AppCompatActivity() {

    companion object {
        const val ARG_PARAM_KEY = "json"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result_list)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val json = intent.getStringExtra(ARG_PARAM_KEY)
        val type: Type = object : TypeToken<List<JobListQuery.Job>>() {}.type
        val list = Gson().fromJson<List<JobListQuery.Job>>(json, type)

        findViewById<TextView>(R.id.jobs_count_tv).text = getString(R.string.total_jobs_count, list.size.toString())

        val jobsRv = findViewById<RecyclerView>(R.id.jobs_rv)
        val listAdapter = JobListAdapter(list)
        listAdapter.onItemClicked = {
            val intent = Intent(this@SearchResultListActivity, JobDetailActivity::class.java)
            intent.putExtra(JobDetailActivity.ARG_PARAM_KEY, Gson().toJson(it))
            startActivity(intent)
        }
        jobsRv.adapter = listAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
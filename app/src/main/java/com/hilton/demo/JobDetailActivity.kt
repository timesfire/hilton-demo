package com.hilton.demo

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.hilton.rocketreserver.JobListQuery


class JobDetailActivity : AppCompatActivity() {
    companion object {
        const val ARG_PARAM_KEY = "job"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val job = Gson().fromJson(intent.getStringExtra(ARG_PARAM_KEY),JobListQuery.Job::class.java)
        findViewById<TextView>(R.id.job_title).text = getString(R.string.job_title, job.title)
        findViewById<TextView>(R.id.job_desc).text = getString(R.string.job_description, job.description)
        findViewById<TextView>(R.id.company_name).text = getString(R.string.company_name, job.company?.name)


        val imageView = findViewById<ImageView>(R.id.company_logo)
        job?.company?.logoUrl?.let {
            Glide.with(this).load(it).into(imageView)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
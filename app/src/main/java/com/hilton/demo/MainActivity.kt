package com.hilton.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hilton.demo.searchresult.SearchResultListActivity
import com.hilton.rocketreserver.JobListQuery
import com.hilton.rocketreserver.JobListQuery.Data
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.util.UUID

class MainActivity : AppCompatActivity() {

    lateinit var keyListAdapter:HistoryKeyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.input_txt)
        val searchBtn = findViewById<Button>(R.id.search_btn)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        editText.addTextChangedListener {
            searchBtn.isEnabled = it?.isNotEmpty() == true
        }

        val historyRv = findViewById<RecyclerView>(R.id.history_rv)
        keyListAdapter = HistoryKeyListAdapter(getHistoryKeyFromSp())
        keyListAdapter.onItemClicked = {
            editText.setText(it)
            searchBtn.performClick()
        }
        historyRv.adapter = keyListAdapter

        searchBtn.setOnClickListener {
            val searchKey = editText.text.toString().trim()

            keyListAdapter.addKey(searchKey)
            keyListAdapter.notifyDataSetChanged()

            lifecycleScope.launch {
                progressBar.visibility = View.VISIBLE
                val searchQuery = JobListQuery(key = searchKey)


                val response = try {
                    // real fetch
                    // apolloClient(this@MainActivity).query(searchQuery).execute()
                    // test
                    // apolloClient(this@MainActivity).query(HelloQuery()).execute()

                    // mock data
                    val mockJob1 = JobListQuery.Job(title = "A company look for developer", company = JobListQuery.Company(name = "AAA", logoUrl = "https://img2.baidu.com/it/u=4088573071,191442804&fm=253&fmt=auto&app=138&f=JPEG?w=537&h=500"), description = "very good")
                    val mockJob2 = JobListQuery.Job(title = "B company look for developer", company = JobListQuery.Company(name = "BBB", logoUrl = "https://img2.baidu.com/it/u=4088573071,191442804&fm=253&fmt=auto&app=138&f=JPEG?w=537&h=500"), description = "very good")
                    val mockJob3 = JobListQuery.Job(title = "C company look for developer", company = JobListQuery.Company(name = "CCC", logoUrl = "https://img2.baidu.com/it/u=4088573071,191442804&fm=253&fmt=auto&app=138&f=JPEG?w=537&h=500"), description = "very good")
                    val jobs = arrayListOf(mockJob1, mockJob2, mockJob3)
                    ApolloResponse.Builder<Data>(data = Data(jobs = jobs), requestUuid = UUID.randomUUID(), operation = searchQuery)
                        .build()

                } catch (e: ApolloException) {
                    Log.d("MainActivity", "Failure", e)
                    progressBar.visibility = View.INVISIBLE
                    return@launch
                }
                Log.d("MainActivity", "success:${response.data?.toString()}")
                progressBar.visibility = View.INVISIBLE
                response.data?.jobs?.let {
                    val intent = Intent(this@MainActivity, SearchResultListActivity::class.java)
                    val json = Gson().toJson(it)
                    intent.putExtra(SearchResultListActivity.ARG_PARAM_KEY, json)
                    startActivity(intent)
                }
            }

        }
    }

    private val SP_FILE_NAME = "main_sp_file"
    private val HISTORY_SEARCH_KEYS = "history_search_keys"
    private fun getHistoryKeyFromSp(): MutableList<String> {
        val jsstring = getSharedPreferences(SP_FILE_NAME, MODE_PRIVATE).getString(HISTORY_SEARCH_KEYS, "[]")
        val type: Type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(jsstring, type)
    }

    private fun setHistoryKeyToSp() {
        getSharedPreferences(SP_FILE_NAME, MODE_PRIVATE).edit()
            .putString(HISTORY_SEARCH_KEYS, Gson().toJson(keyListAdapter.getData())).apply()
    }

    override fun onStop() {
        super.onStop()
        setHistoryKeyToSp()
    }

}

package com.hilton.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo3.exception.ApolloException
import com.google.gson.Gson
import com.hilton.demo.pokemondetail.PokemonDetailActivity
import com.hilton.rocketreserver.Pokemon_v2_pokemonspeciesQuery
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var speciesListAdapter: PokemonSpeciesListAdapter
    lateinit var loadingView: ConstraintLayout
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var noDataTipTv: TextView
    var searchKey = ""
    val pageSize = 8


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.input_txt)
        val searchBtn = findViewById<Button>(R.id.search_btn)
        loadingView = findViewById(R.id.loading)
        refreshLayout = findViewById(R.id.refreshLayout)
        noDataTipTv = findViewById(R.id.no_data_tip)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        speciesListAdapter = PokemonSpeciesListAdapter()
        recyclerView.adapter = speciesListAdapter

        editText.addTextChangedListener {
            searchBtn.isEnabled = it?.isNotEmpty() == true
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchBtn.performClick()
                true
            } else false
        }


        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            fetchData(searchKey)
        }
        speciesListAdapter.onItemClicked = {
            val intent = Intent(this@MainActivity, PokemonDetailActivity::class.java)
            intent.putExtra(PokemonDetailActivity.ARG_PARAM_KEY, Gson().toJson(it))
            startActivity(intent)
        }

        searchBtn.setOnClickListener {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            noDataTipTv.visibility = View.GONE
            searchKey = editText.text.toString().trim()
            speciesListAdapter.clearAndRefresh()
            refreshLayout.setEnableLoadMore(true)
            refreshLayout.resetNoMoreData()
            fetchData(searchKey)
        }
    }

    private fun fetchData(searchKey: String) {
        lifecycleScope.launch {
            loadingView.visibility = View.VISIBLE

            val offset = speciesListAdapter.getRealSpeciesCount()
            val searchQuery = Pokemon_v2_pokemonspeciesQuery("%$searchKey%", offset, pageSize)

            val response = try {
                apolloClient().query(searchQuery).execute()
            } catch (e: ApolloException) {
                Log.d("MainActivity", "Failure", e)
                if (offset == 0) {
                    noDataTipTv.text = e.message
                    noDataTipTv.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                }
                null
            }
            loadingView.visibility = View.INVISIBLE
            if (offset > 0) {
                refreshLayout.finishLoadMore()
            }
            response?.data?.pokemon_v2_pokemonspecies?.let {
                if (it.isEmpty()) {
                    if (offset == 0) {
                        noDataTipTv.text = getString(R.string.no_data_fond)
                        noDataTipTv.visibility = View.VISIBLE
                        refreshLayout.setEnableLoadMore(false)
                    }
                    refreshLayout.setNoMoreData(true)

                } else {
                    speciesListAdapter.addPokemonspecies(it)
                    if (it.size < 8) {
                        refreshLayout.setNoMoreData(true)
                    }
                }
                Log.d("MainActivity", "获取到数据：${it.size}")
            }
        }
    }


}

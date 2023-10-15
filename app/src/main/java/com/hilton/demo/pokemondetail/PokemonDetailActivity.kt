package com.hilton.demo.pokemondetail

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hilton.demo.R
import com.hilton.rocketreserver.Pokemon_v2_pokemonspeciesQuery


class PokemonDetailActivity : AppCompatActivity() {
    companion object {
        const val ARG_PARAM_KEY = "pokemon"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.abilities_rv)


        val pokemon =
            Gson().fromJson(intent.getStringExtra(ARG_PARAM_KEY), Pokemon_v2_pokemonspeciesQuery.Pokemon_v2_pokemon::class.java)
        findViewById<TextView>(R.id.pokemon_name).text = getString(R.string.pokemon_name, pokemon.name)

        recyclerView.adapter = AbilitiesListAdapter(pokemon.pokemon_v2_pokemonabilities)

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
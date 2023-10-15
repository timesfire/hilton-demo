package com.hilton.demo.pokemondetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hilton.demo.R
import com.hilton.rocketreserver.Pokemon_v2_pokemonspeciesQuery


class AbilitiesListAdapter(private val abilities: List<Pokemon_v2_pokemonspeciesQuery.Pokemon_v2_pokemonability>) :
    RecyclerView.Adapter<AbilitiesListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val abilitiesNameTv: TextView = itemView.findViewById(R.id.abilities_name)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pokemon_detail_list_item_abilities, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return abilities.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = abilities[position]
        holder.abilitiesNameTv.text = item.pokemon_v2_ability?.name
    }
}

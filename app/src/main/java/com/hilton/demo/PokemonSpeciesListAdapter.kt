package com.hilton.demo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hilton.rocketreserver.Pokemon_v2_pokemonspeciesQuery

data class PokemonItem(
    val pokemon_v2_pokemoncolor: Pokemon_v2_pokemonspeciesQuery.Pokemon_v2_pokemoncolor?,
    val pokemon_v2_pokemon: Pokemon_v2_pokemonspeciesQuery.Pokemon_v2_pokemon
)

class PokemonSpeciesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var realSpeciesCount = 0
    fun getRealSpeciesCount(): Int {
        return realSpeciesCount
    }


    companion object {
        const val VIEW_TYPE_POKEMONSPECY = 1
        const val VIEW_TYPE_POKEMON = 2
        var sColorNameMap: HashMap<String, Long> = HashMap()

        init {
            sColorNameMap["brown"] = 0xffa52a2a
            sColorNameMap["pink"] = 0xffffc0cb
        }
    }

    private val dataList = mutableListOf<Any>()
    fun addPokemonspecies(speciesList: List<Pokemon_v2_pokemonspeciesQuery.Pokemon_v2_pokemonspecy>) {
        realSpeciesCount += speciesList.size
        val position = dataList.size
        speciesList.forEach { specie ->
            dataList.add(specie)
            specie.pokemon_v2_pokemons.forEach {
                dataList.add(PokemonItem(specie.pokemon_v2_pokemoncolor, it))
            }
        }
        notifyItemRangeInserted(position, dataList.size - position)
    }


    override fun getItemViewType(position: Int): Int {
        when (dataList[position]) {
            is Pokemon_v2_pokemonspeciesQuery.Pokemon_v2_pokemonspecy -> return VIEW_TYPE_POKEMONSPECY
            is PokemonItem -> return VIEW_TYPE_POKEMON
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_POKEMONSPECY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.pokemon_species_list_item_specie, parent, false)
                return SpeciesViewHolder(view)
            }

            VIEW_TYPE_POKEMON -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.pokemon_species_list_item_pokemon, parent, false)
                return PokemonViewHolder(view)
            }

        }
        throw Exception("viewType error")
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    var onItemClicked: ((Pokemon_v2_pokemonspeciesQuery.Pokemon_v2_pokemon) -> Unit)? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = dataList[position]
        when (holder) {
            is SpeciesViewHolder -> {
                holder.speciesNameTv.text = (data as Pokemon_v2_pokemonspeciesQuery.Pokemon_v2_pokemonspecy).name
                holder.captureRateTv.text = data.capture_rate?.toString()
                holder.itemView.setBackgroundColor(parseColor(data.pokemon_v2_pokemoncolor?.name))
            }

            is PokemonViewHolder -> {
                holder.pokemonNameTv.text = (data as PokemonItem).pokemon_v2_pokemon.name
                holder.itemView.setOnClickListener { onItemClicked?.invoke(data.pokemon_v2_pokemon) }
                holder.itemView.setBackgroundColor(parseColor(data.pokemon_v2_pokemoncolor?.name))
            }
        }
    }

    fun parseColor(colorName: String?): Int {
        var color = Color.GRAY
        if (colorName?.isNotEmpty() == true) {
            kotlin.runCatching {
                color = Color.parseColor(colorName)
            }.onFailure {
                sColorNameMap[colorName]?.let {
                    color = it.toInt()
                }
            }
        }
        return color
    }

    fun clearAndRefresh() {
        realSpeciesCount = 0
        val itemCount = dataList.size
        dataList.clear()
        notifyItemRangeRemoved(0, itemCount)
    }


    class SpeciesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val speciesNameTv: TextView = itemView.findViewById(R.id.species_name)
        val captureRateTv: TextView = itemView.findViewById(R.id.capture_rate)
    }

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonNameTv: TextView = itemView.findViewById(R.id.pokemon_name)
    }
}

package com.handibagofholding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter (private val characterList: ArrayList<CharacterMetaData>): RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_card_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val CharacterViewModel = characterList[position]

        holder.tv_characterName.text = CharacterViewModel.name

    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {
        val tv_characterName = itemView.findViewById<TextView>(R.id.tv_characterName)

    }
}
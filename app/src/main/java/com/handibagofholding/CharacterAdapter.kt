package com.handibagofholding

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter (private val characterList: ArrayList<CharacterMetaData>, private val activityContext: Context): RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
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

        holder.myItemView.setOnClickListener {
            val intent = Intent(activityContext, CharacterActivity::class.java)
            intent.putExtra("id", CharacterViewModel.id)
            activityContext.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    class ViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
        val tv_characterName = itemView.findViewById<TextView>(R.id.tv_characterName)
        val myItemView = itemView
    }
}
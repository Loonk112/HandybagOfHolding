package com.handybagofholding

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter (private val characterList: ArrayList<CharacterMetaData>, private val activityContext: Context): RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_character, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val characterViewModel = characterList[position]

        holder.tvCharacterName.text = characterViewModel.name

        holder.myItemView.setOnClickListener {
            val intent = Intent(activityContext, CharacterActivity::class.java)
            ViewModel.character = characterViewModel.id
            activityContext.startActivity(intent)
        }

        holder.myItemView.setOnLongClickListener {
            AlertDialog(activityContext).show("You are about to delete ${characterViewModel.name}.\nIt is irreversible.\nAre you sure?"){
                if (it == AlertDialog.ResponseType.YES) {

                    ViewModel.db.runTransaction { transaction ->

                        transaction.delete(ViewModel.db.collection("characters").document(characterViewModel.id))
                        transaction.delete(ViewModel.db.collection("character_notes").document(characterViewModel.id))

                    }.addOnSuccessListener {
                        Log.d("Firebase", "Item was deleted")
                        Toast.makeText(activityContext, "${characterViewModel.name} was deleted.", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { e ->
                        Log.w("Firebase", "Error deleting document", e)
                        Toast.makeText(activityContext, "${characterViewModel.name} was NOT deleted!", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            true
        }

    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvCharacterName: TextView = itemView.findViewById(R.id.tv_characterName)
        val myItemView = itemView
    }
}
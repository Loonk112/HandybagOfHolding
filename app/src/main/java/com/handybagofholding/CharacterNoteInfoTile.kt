package com.handybagofholding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView

class CharacterNoteInfoTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val tvNote: TextView

    init {
        inflate(context, R.layout.tile_note_info, this)

        tvNote = findViewById(R.id.tv_note)

        getItemInfo()

        this.setOnLongClickListener {
            Log.d("CharacterNoteInfoTile", "Long click detected")
            findNavController().navigate(R.id.action_characterDetailsFragment_to_characterNoteEditFragment)
            true
        }

    }

    private fun getItemInfo() {
        ViewModel.db.collection("character_notes").document(ViewModel.character).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("CharacterNoteInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("CharacterNoteInfoTile", "$snapshot")
            if (snapshot != null && snapshot.exists())
            {
                (snapshot.get("note") as String).let {
                    tvNote.text = it
                }
            }
        }
    }
}
package com.handibagofholding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView

class NoteInfoTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val tvNote: TextView

    init {
        inflate(context, R.layout.note_info_tile, this)

        tvNote = findViewById(R.id.tv_note)

        getItemInfo()

        this.setOnLongClickListener {
            Log.d("NoteInfoTile", "Long click detected")
            findNavController().navigate(R.id.action_itemFragment_to_noteEditFragment)
            true
        }

    }

    private fun getItemInfo() {
        val iId = ViewModel.item

        ViewModel.db.collection("notes").document(iId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("NoteInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("NoteInfoTile", "$snapshot")
            if (snapshot != null && snapshot.exists())
            {
                (snapshot.get("note") as String).let {
                    tvNote.text = it
                }
            }
        }
    }
}
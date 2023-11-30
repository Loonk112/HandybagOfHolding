package com.handybagofholding.tile

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import com.handybagofholding.R
import com.handybagofholding.ViewModel

class ItemNoteInfoTile @JvmOverloads constructor(
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
            Log.d("ItemNoteInfoTile", "Long click detected")
            findNavController().navigate(R.id.action_itemFragment_to_itemNoteEditFragment)
            true
        }

    }

    private fun getItemInfo() {
        val iId = ViewModel.item

        ViewModel.db.collection("item_notes").document(iId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("ItemNoteInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("ItemNoteInfoTile", "$snapshot")
            if (snapshot != null && snapshot.exists())
            {
                (snapshot.get("note") as String).let {
                    tvNote.text = it
                }
            }
        }
    }
}
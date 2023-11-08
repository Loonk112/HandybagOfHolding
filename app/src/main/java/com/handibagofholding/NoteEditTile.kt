package com.handibagofholding

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.card.MaterialCardView

class NoteEditTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : LinearLayout(context, attributeSet, defaultStyleAttribute) {


    private val et_itemNotes: EditText

    init {
        inflate(context, R.layout.note_edit_tile, this)

        et_itemNotes = findViewById(R.id.et_itemNotes)
    }

    public fun setValue(value: String) {
        et_itemNotes.setText(value)
    }

    public fun getValue(): HashMap<*, String>? {

        if (et_itemNotes.text.isBlank()) return null


        val stringOut = et_itemNotes.text.toString().trim().trimIndent()

        return hashMapOf(
            "note" to stringOut
        )
    }

}
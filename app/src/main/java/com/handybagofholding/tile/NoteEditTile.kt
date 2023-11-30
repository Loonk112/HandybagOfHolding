package com.handybagofholding.tile

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import com.handybagofholding.R

class NoteEditTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : LinearLayout(context, attributeSet, defaultStyleAttribute) {


    private val etItemNotes: EditText

    init {
        inflate(context, R.layout.tile_note_edit, this)

        etItemNotes = findViewById(R.id.et_itemNotes)
    }

    fun setValue(value: String) {
        etItemNotes.setText(value)
    }

    fun getValue(): HashMap<*, String>? {

        if (etItemNotes.text.isBlank()) return null


        val stringOut = etItemNotes.text.toString().trim().trimIndent()

        return hashMapOf(
            "note" to stringOut
        )
    }

}
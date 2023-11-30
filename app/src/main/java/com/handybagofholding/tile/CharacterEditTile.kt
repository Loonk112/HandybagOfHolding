package com.handybagofholding.tile

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import com.handybagofholding.R
import com.handybagofholding.ViewModel
import com.handybagofholding.data.CharacterMetaData

class CharacterEditTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : LinearLayout(context, attributeSet, defaultStyleAttribute) {


    private val etCharacterName: EditText

    init {
        inflate(context, R.layout.tile_character_edit, this)

        etCharacterName = findViewById(R.id.et_characterName)
    }

    fun setValue(value: CharacterMetaData) {
        etCharacterName.setText(value.name)
    }

    fun getValue(): CharacterMetaData? {

        if (etCharacterName.text.isBlank()) return null


        val cName = etCharacterName.text.toString().trim()

        return CharacterMetaData(ViewModel.character, cName, ViewModel.account)
    }

}
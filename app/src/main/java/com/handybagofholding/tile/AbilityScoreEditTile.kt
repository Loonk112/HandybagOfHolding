package com.handybagofholding.tile

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.handybagofholding.R
import com.handybagofholding.data.AbilityScoreData

class AbilityScoreEditTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : LinearLayout(context, attributeSet, defaultStyleAttribute) {

    private val strengthEdit: VerticalValueCounterTile
    private val constitutionEdit: VerticalValueCounterTile
    private val dexterityEdit: VerticalValueCounterTile
    private val intelligenceEdit: VerticalValueCounterTile
    private val wisdomEdit: VerticalValueCounterTile
    private val charismaEdit: VerticalValueCounterTile


    init {
        inflate(context, R.layout.tile_ability_score_edit, this)

        strengthEdit = findViewById(R.id.strengthEdit)
        constitutionEdit = findViewById(R.id.constitutionEdit)
        dexterityEdit = findViewById(R.id.dexterityEdit)
        intelligenceEdit = findViewById(R.id.intelligenceEdit)
        wisdomEdit = findViewById(R.id.wisdomEdit)
        charismaEdit = findViewById(R.id.charismaEdit)

    }

    fun setValues(values: AbilityScoreData) {
        strengthEdit.setValue(values.strength)
        constitutionEdit.setValue(values.constitution)
        dexterityEdit.setValue(values.dexterity)
        intelligenceEdit.setValue(values.intelligence)
        wisdomEdit.setValue(values.wisdom)
        charismaEdit.setValue(values.charisma)
    }

    fun getValues(): AbilityScoreData {

        return AbilityScoreData(
            strengthEdit.getValue(),
            constitutionEdit.getValue(),
            dexterityEdit.getValue(),
            intelligenceEdit.getValue(),
            wisdomEdit.getValue(),
            charismaEdit.getValue()
        )
    }

}
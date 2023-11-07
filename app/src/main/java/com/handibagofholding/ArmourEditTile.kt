package com.handibagofholding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView

class ArmourEditTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val et_ac: TextView
    private val spnr_proficiency: Spinner
    private val spnr_slot: Spinner

    init {
        inflate(context, R.layout.armour_edit_tile, this)

        et_ac = findViewById(R.id.et_ac)
        spnr_proficiency = findViewById(R.id.spnr_proficiency)
        spnr_slot = findViewById(R.id.spnr_slot)

        if (spnr_proficiency != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                context,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_armourProficiencies)
            )
            spnr_proficiency.adapter = adapter
        }
        else Log.e("ArmourEditTile", "Can't find view!")

        if (spnr_slot != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                context,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_armourSlot)
            )
            spnr_slot.adapter = adapter
        }
        else Log.e("ArmourEditTile", "Can't find view!")
    }

    public fun setValues(values: ArmourData) {
        et_ac.text = values.ac.toString()
        val proficiencyArray = resources.getStringArray(R.array.sa_armourProficiencies)
        spnr_proficiency.setSelection(proficiencyArray.indexOf(values.proficiency))
        val slotArray = resources.getStringArray(R.array.sa_armourSlot)
        spnr_slot.setSelection(slotArray.indexOf(values.slot))
    }

    public fun getValues(): ArmourData {
        val iId = ViewModel.item
        var ac = 0
        if (et_ac.text.isNotBlank()) {
            ac = et_ac.text.toString().toInt()
        }
        else {
            Toast.makeText(context, "AC defaulted to 0", Toast.LENGTH_SHORT).show()
        }

        val out = ArmourData(iId, spnr_proficiency.selectedItem.toString(), spnr_slot.selectedItem.toString(), ac)

        Log.d("ArmourEditTile", "$out")

        return out
    }

}
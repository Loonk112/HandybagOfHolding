package com.handybagofholding.tile

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.handybagofholding.R
import com.handybagofholding.ViewModel
import com.handybagofholding.data.ArmourData

@Suppress("KotlinConstantConditions")
class ArmourEditTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : LinearLayout(context, attributeSet, defaultStyleAttribute) {


    private val etAc: TextView
    private val spnrProficiency: Spinner
    private val spnrSlot: Spinner

    init {
        inflate(context, R.layout.tile_armour_edit, this)

        etAc = findViewById(R.id.et_ac)
        spnrProficiency = findViewById(R.id.spnr_proficiency)
        spnrSlot = findViewById(R.id.spnr_slot)

        if (spnrProficiency != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                context,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_armourProficiencies)
            )
            spnrProficiency.adapter = adapter
        }
        else Log.e("ArmourEditTile", "Can't find view!")

        if (spnrSlot != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                context,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_armourSlot)
            )
            spnrSlot.adapter = adapter
        }
        else Log.e("ArmourEditTile", "Can't find view!")
    }

    fun setValues(values: ArmourData) {
        etAc.text = values.ac.toString()
        val proficiencyArray = resources.getStringArray(R.array.sa_armourProficiencies)
        spnrProficiency.setSelection(proficiencyArray.indexOf(values.proficiency))
        val slotArray = resources.getStringArray(R.array.sa_armourSlot)
        spnrSlot.setSelection(slotArray.indexOf(values.slot))
    }

    fun getValues(): ArmourData {
        val iId = ViewModel.item
        var ac = 0
        if (etAc.text.isNotBlank()) {
            ac = etAc.text.toString().toInt()
        }
        else {
            Toast.makeText(context, "AC defaulted to 0", Toast.LENGTH_SHORT).show()
        }

        val out = ArmourData(iId, spnrProficiency.selectedItem.toString(), spnrSlot.selectedItem.toString(), ac)

        Log.d("ArmourEditTile", "$out")

        return out
    }

}
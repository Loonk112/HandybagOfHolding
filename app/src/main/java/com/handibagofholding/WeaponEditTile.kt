package com.handibagofholding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class WeaponEditTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : LinearLayout(context, attributeSet, defaultStyleAttribute) {

    private var weaponRange = "Melee"
    private var weaponProficiency = "Simple"

    private var rg_weapons_proficiencyGroup: RadioGroup
    private var rg_weapons_range: RadioGroup
    private var spnr_weapons_proficiency: Spinner
    private var rv_weapons_damageDisplay: RecyclerView
    private var spnr_weapons_damageType: Spinner
    private var spnr_weapons_damageDice: Spinner
    private var et_weapons_damageDiceCount: EditText
    private var b_weapons_addDamage: Button

    private var damageArrayList: ArrayList<ItemDamageData>
    private var damageAdapter: ItemDamageAdapter


    init {
        inflate(context, R.layout.weapon_edit_tile, this)

        rg_weapons_proficiencyGroup = findViewById<RadioGroup>(R.id.rg_weapons_proficiencyGroup)
        rg_weapons_range = findViewById<RadioGroup>(R.id.rg_weapons_range)
        spnr_weapons_proficiency = findViewById<Spinner>(R.id.spnr_weapons_proficiency)
        spnr_weapons_damageType = findViewById<Spinner>(R.id.spnr_weapons_damageType)
        spnr_weapons_damageDice = findViewById<Spinner>(R.id.spnr_weapons_damageDice)
        et_weapons_damageDiceCount = findViewById<EditText>(R.id.et_weapons_damageCount)
        rv_weapons_damageDisplay = findViewById<RecyclerView>(R.id.rv_weapons_damageDisplay)
        b_weapons_addDamage = findViewById<Button>(R.id.b_weapons_addDamage)

        if (spnr_weapons_proficiency != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                context,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponSimpleProficiencyMelee)
            )
            spnr_weapons_proficiency.adapter = adapter
        }
        else Log.e("WeaponEditTile", "Can't find view!")

        if (spnr_weapons_damageType != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                context,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_damageTypes)
            )
            spnr_weapons_damageType.adapter = adapter
        }
        else Log.e("WeaponEditTile", "Can't find view!")

        if (spnr_weapons_damageDice != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                context,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_damageDice)
            )
            spnr_weapons_damageDice.adapter = adapter
        }
        else Log.e("WeaponEditTile", "Can't find view!")

        damageArrayList = ArrayList<ItemDamageData>()
        damageAdapter = ItemDamageAdapter(damageArrayList, context)
        rv_weapons_damageDisplay.adapter = damageAdapter

        rv_weapons_damageDisplay.layoutManager = LinearLayoutManager(context)

        rg_weapons_proficiencyGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                when (radio.text.toString())
                {
                    "Simple" -> {
                        weaponProficiency = radio.text.toString()
                        updateWeaponsProficiency(weaponProficiency, weaponRange)
                    }
                    "Martial" -> {
                        weaponProficiency = radio.text.toString()
                        updateWeaponsProficiency(weaponProficiency, weaponRange)
                    }
                }
            })

        rg_weapons_range.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                when (radio.text)
                {
                    "Melee" -> {
                        weaponRange = radio.text.toString()
                        updateWeaponsProficiency(weaponProficiency, weaponRange)
                    }
                    "Ranged" -> {
                        weaponRange = radio.text.toString()
                        updateWeaponsProficiency(weaponProficiency, weaponRange)
                    }
                }
            })

        b_weapons_addDamage.setOnClickListener {
            if (et_weapons_damageDiceCount.text.isBlank())
            {
                Toast.makeText(context, "Dice count is required to add damage.", Toast.LENGTH_SHORT).show()
            }
            else {
                damageArrayList.add(ItemDamageData(spnr_weapons_damageType.selectedItem.toString(), et_weapons_damageDiceCount.text.toString().toInt(), spnr_weapons_damageDice.selectedItem.toString()))
                rv_weapons_damageDisplay.adapter?.notifyItemInserted(damageArrayList.size)
                Log.d("WeaponEditTile", "$damageArrayList")
            }
        }
    }

    public fun setValue(value: WeaponData) {


        when (value.range) {
            "Melee" -> {
                findViewById<RadioButton>(R.id.rb_weapons_melee).setChecked(true)
            }
            "Ranged" -> {
                findViewById<RadioButton>(R.id.rb_weapons_ranged).setChecked(true)
            }
        }
        when (value.group) {
            "Simple" -> {
                findViewById<RadioButton>(R.id.rb_weapons_simple).setChecked(true)
            }
            "Martial" -> {
                findViewById<RadioButton>(R.id.rb_weapons_martial).setChecked(true)
            }
        }

        var proficiencyArray = arrayOf<String>()
        if (weaponProficiency == "Simple")
        {
            if (weaponRange == "Melee")
            {// Simple Melee
                proficiencyArray = resources.getStringArray(R.array.sa_weaponSimpleProficiencyMelee)
            }
            else
            {// Simple Ranged
                proficiencyArray = resources.getStringArray(R.array.sa_weaponSimpleProficiencyRanged)
            }
        }
        else
        {
            if (weaponRange == "Melee")
            {// Martial Melee
                proficiencyArray = resources.getStringArray(R.array.sa_weaponMartialProficiencyMelee)
            }
            else
            {// Martial Ranged
                proficiencyArray = resources.getStringArray(R.array.sa_weaponMartialProficiencyRanged)
            }
        }

        spnr_weapons_proficiency.setSelection(proficiencyArray.indexOf(value.proficiency))
        value.damage?.let {
            damageArrayList.clear()
            damageArrayList.addAll(it.toMutableList())
        }

        damageAdapter.notifyDataSetChanged()
    }

    public fun getValue(): WeaponData {
        val iId = ViewModel.item
        return WeaponData(iId, weaponProficiency, weaponRange, spnr_weapons_proficiency.selectedItem.toString(), damageArrayList)
    }

    private fun updateWeaponsProficiency(proficiency: String, range: String)
    {
        spnr_weapons_proficiency.let {
            if (proficiency == "Simple")
            {
                if (range == "Melee")
                {// Simple Melee
                    val adapter = ArrayAdapter( //TODO: Spinner item
                        context,
                        android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponSimpleProficiencyMelee)
                    )
                    it!!.adapter = adapter
                }
                else
                {// Simple Ranged
                    val adapter = ArrayAdapter( //TODO: Spinner item
                        context,
                        android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponSimpleProficiencyRanged)
                    )
                    it!!.adapter = adapter
                }
            }
            else
            {
                if (range == "Melee")
                {// Martial Melee
                    val adapter = ArrayAdapter( //TODO: Spinner item
                        context,
                        android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponMartialProficiencyMelee)
                    )
                    it!!.adapter = adapter
                }
                else
                {// Martial Ranged
                    val adapter = ArrayAdapter( //TODO: Spinner item
                        context,
                        android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponMartialProficiencyRanged)
                    )
                    it!!.adapter = adapter
                }
            }
        }
    }
}
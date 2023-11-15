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

class WeaponEditTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : LinearLayout(context, attributeSet, defaultStyleAttribute) {

    private var weaponRange = "Melee"
    private var weaponProficiency = "Simple"

    private var rgWeaponsProficiencyGroup: RadioGroup
    private var rgWeaponsRange: RadioGroup
    private var spnrWeaponsProficiency: Spinner
    private var rvWeaponsDamageDisplay: RecyclerView
    private var spnrWeaponsDamageType: Spinner
    private var spnrWeaponsDamageDice: Spinner
    private var etWeaponsDamageDiceCount: EditText
    private var bWeaponsAddDamage: Button

    private var damageArrayList: ArrayList<ItemDamageData>
    private var damageAdapter: ItemDamageAdapter


    init {
        inflate(context, R.layout.weapon_edit_tile, this)

        rgWeaponsProficiencyGroup = findViewById(R.id.rg_weapons_proficiencyGroup)
        rgWeaponsRange = findViewById(R.id.rg_weapons_range)
        spnrWeaponsProficiency = findViewById(R.id.spnr_weapons_proficiency)
        spnrWeaponsDamageType = findViewById(R.id.spnr_weapons_damageType)
        spnrWeaponsDamageDice = findViewById(R.id.spnr_weapons_damageDice)
        etWeaponsDamageDiceCount = findViewById(R.id.et_weapons_damageCount)
        rvWeaponsDamageDisplay = findViewById(R.id.rv_weapons_damageDisplay)
        bWeaponsAddDamage = findViewById(R.id.b_weapons_addDamage)

        spnrWeaponsProficiency.let {
            val adapter = ArrayAdapter( //TODO: Spinner item
                context,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponSimpleProficiencyMelee)
            )
            spnrWeaponsProficiency.adapter = adapter
        }

        spnrWeaponsDamageType.let {
            val adapter = ArrayAdapter( //TODO: Spinner item
                context,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_damageTypes)
            )
            spnrWeaponsDamageType.adapter = adapter
        }

        spnrWeaponsDamageDice.let {
            val adapter = ArrayAdapter( //TODO: Spinner item
                context,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_damageDice)
            )
            spnrWeaponsDamageDice.adapter = adapter
        }

        damageArrayList = ArrayList()
        damageAdapter = ItemDamageAdapter(damageArrayList, context)
        rvWeaponsDamageDisplay.adapter = damageAdapter

        rvWeaponsDamageDisplay.layoutManager = LinearLayoutManager(context)

        rgWeaponsProficiencyGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (radio.text.toString()) {
                "Simple" -> {
                    weaponProficiency = radio.text.toString()
                    updateWeaponsProficiency(weaponProficiency, weaponRange)
                }

                "Martial" -> {
                    weaponProficiency = radio.text.toString()
                    updateWeaponsProficiency(weaponProficiency, weaponRange)
                }
            }
        }

        rgWeaponsRange.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (radio.text) {
                "Melee" -> {
                    weaponRange = radio.text.toString()
                    updateWeaponsProficiency(weaponProficiency, weaponRange)
                }

                "Ranged" -> {
                    weaponRange = radio.text.toString()
                    updateWeaponsProficiency(weaponProficiency, weaponRange)
                }
            }
        }

        bWeaponsAddDamage.setOnClickListener {
            if (etWeaponsDamageDiceCount.text.isBlank())
            {
                Toast.makeText(context, "Dice count is required to add damage.", Toast.LENGTH_SHORT).show()
            }
            else {
                damageArrayList.add(ItemDamageData(spnrWeaponsDamageType.selectedItem.toString(), etWeaponsDamageDiceCount.text.toString().toInt(), spnrWeaponsDamageDice.selectedItem.toString()))
                rvWeaponsDamageDisplay.adapter?.notifyItemInserted(damageArrayList.size)
                Log.d("WeaponEditTile", "$damageArrayList")
            }
        }
    }

    fun setValue(value: WeaponData) {


        when (value.range) {
            "Melee" -> {
                findViewById<RadioButton>(R.id.rb_weapons_melee).isChecked = true
            }
            "Ranged" -> {
                findViewById<RadioButton>(R.id.rb_weapons_ranged).isChecked = true
            }
        }
        when (value.group) {
            "Simple" -> {
                findViewById<RadioButton>(R.id.rb_weapons_simple).isChecked = true
            }
            "Martial" -> {
                findViewById<RadioButton>(R.id.rb_weapons_martial).isChecked = true
            }
        }

        val proficiencyArray = if (weaponProficiency == "Simple") {
            if (weaponRange == "Melee") {// Simple Melee
                resources.getStringArray(R.array.sa_weaponSimpleProficiencyMelee)
            } else {// Simple Ranged
                resources.getStringArray(R.array.sa_weaponSimpleProficiencyRanged)
            }
        } else {
            if (weaponRange == "Melee") {// Martial Melee
                resources.getStringArray(R.array.sa_weaponMartialProficiencyMelee)
            } else {// Martial Ranged
                resources.getStringArray(R.array.sa_weaponMartialProficiencyRanged)
            }
        }

        spnrWeaponsProficiency.setSelection(proficiencyArray.indexOf(value.proficiency))
        value.damage?.let {
            damageArrayList.clear()
            damageArrayList.addAll(it.toMutableList())
        }

        damageAdapter.notifyDataSetChanged()
    }

    fun getValue(): WeaponData {
        val iId = ViewModel.item
        return WeaponData(iId, weaponProficiency, weaponRange, spnrWeaponsProficiency.selectedItem.toString(), damageArrayList)
    }

    private fun updateWeaponsProficiency(proficiency: String, range: String)
    {
        spnrWeaponsProficiency.let {
            if (proficiency == "Simple")
            {
                if (range == "Melee")
                {// Simple Melee
                    val adapter = ArrayAdapter( //TODO: Spinner item
                        context,
                        android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponSimpleProficiencyMelee)
                    )
                    it.adapter = adapter
                }
                else
                {// Simple Ranged
                    val adapter = ArrayAdapter( //TODO: Spinner item
                        context,
                        android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponSimpleProficiencyRanged)
                    )
                    it.adapter = adapter
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
                    it.adapter = adapter
                }
                else
                {// Martial Ranged
                    val adapter = ArrayAdapter( //TODO: Spinner item
                        context,
                        android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponMartialProficiencyRanged)
                    )
                    it.adapter = adapter
                }
            }
        }
    }
}
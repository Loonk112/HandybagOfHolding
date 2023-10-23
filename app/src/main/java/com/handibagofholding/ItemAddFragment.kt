package com.handibagofholding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemAddFragment : Fragment() {

    var cat: Int? = null
    var weaponRange = "Melee"
    var weaponProficiency = "Simple"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_item_add, container, false)

        view.findViewById<Button>(R.id.b_newItemCancel).setOnClickListener {
            view.findNavController().navigateUp()
        }

        //Basic
        val et_itemName = view.findViewById<EditText>(R.id.et_itemName)
        val spnr_category = view.findViewById<Spinner>(R.id.spnr_category)

        if (spnr_category != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                requireContext(),
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_itemCategories)
            )
            spnr_category.adapter = adapter
        }
        spnr_category.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                               view: View, position: Int, id: Long) {
                Log.d("ItemAddFragment", "<Category Spinner>\tItem Selected: $position")
                filterDisplay(position)
                cat = position
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d("ItemAddFragment", "<Category Spinner>\tNo item selected")
            }
        }

        //Weapons
        val rg_weapons_proficiencyGroup = view.findViewById<RadioGroup>(R.id.rg_weapons_proficiencyGroup)
        val rg_weapons_range = view.findViewById<RadioGroup>(R.id.rg_weapons_range)
        val spnr_weapons_proficiency = view.findViewById<Spinner>(R.id.spnr_weapons_proficiency)

        if (spnr_weapons_proficiency != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                requireContext(),
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponSimpleProficiencyMelee)
            )
            spnr_category.adapter = adapter
        }

        rg_weapons_proficiencyGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = view.findViewById(checkedId)
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
                val radio: RadioButton = view.findViewById(checkedId)
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



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateWeaponsProficiency(weaponProficiency, weaponRange)
    }


    private fun filterDisplay(optionPosition: Int)
    {
        Log.d("ItemAddFragment", "<filterDisplay>\tFiltering starting...")
        Log.d("ItemAddFragment", "<filterDisplay>\tHiding all...")
        //Hiding all elements


        cat?.let { hide(it) }


        if (cat == null)
        {
            hide(0)
            hide(1)
            hide(2)
            hide(3)
        }

        show(optionPosition)

        cat = optionPosition
        Log.d("ItemAddFragment", "<filterDisplay>\tFiltering finishing...")
    }

    private fun hide(optionPosition: Int)
    {
        val myView = view
        when(optionPosition)
        {
            0 -> { //Weapons
                Log.d("ItemAddFragment", "<hide>\tHiding weapons...")
                myView?.findViewById<TextView>(R.id.tv_weapons_proficiencyGroupTitle)!!.visibility = View.GONE
                myView?.findViewById<RadioGroup>(R.id.rg_weapons_proficiencyGroup)!!.visibility = View.GONE
                myView?.findViewById<TextView>(R.id.tv_weapons_rangeTitle)!!.visibility = View.GONE
                myView?.findViewById<RadioGroup>(R.id.rg_weapons_range)!!.visibility = View.GONE
                myView?.findViewById<TextView>(R.id.tv_weapons_proficiencyTitle)!!.visibility = View.GONE
                myView?.findViewById<Spinner>(R.id.spnr_weapons_proficiency)!!.visibility = View.GONE
                myView?.findViewById<TextView>(R.id.tv_weapons_damageTitle)!!.visibility = View.GONE
                myView?.findViewById<RecyclerView>(R.id.rv_weapons_damageDisplay)!!.visibility = View.GONE
                myView?.findViewById<LinearLayout>(R.id.ll_weapons_damageAddLayout)!!.visibility = View.GONE
                myView?.findViewById<Button>(R.id.b_weapons_addDamage)!!.visibility = View.GONE

            }
            1 -> { //Armour
                Log.d("ItemAddFragment", "<hide>\tHiding armour...")
                myView?.findViewById<EditText>(R.id.armour_et_ac)!!.visibility = View.GONE
                myView?.findViewById<Spinner>(R.id.armour_spnr_armourProf)!!.visibility = View.GONE
                myView?.findViewById<TextView>(R.id.armour_tv_atmourProfTitle)!!.visibility = View.GONE

            }
            2 -> { //Consumable
                Log.d("ItemAddFragment", "<hide>\tHiding consumable...")

            }
            3 -> { //Other
                Log.d("ItemAddFragment", "<hide>\tHiding other...")


            }
        }
    }

    private fun show(optionPosition: Int)
    {
        val myView = view
        when(optionPosition)
        {
            0 -> { //Weapons
                Log.d("ItemAddFragment", "<hide>\tHiding weapons...")
                myView?.findViewById<TextView>(R.id.tv_weapons_proficiencyGroupTitle)!!.visibility = View.VISIBLE
                myView?.findViewById<RadioGroup>(R.id.rg_weapons_proficiencyGroup)!!.visibility = View.VISIBLE
                myView?.findViewById<TextView>(R.id.tv_weapons_rangeTitle)!!.visibility = View.VISIBLE
                myView?.findViewById<RadioGroup>(R.id.rg_weapons_range)!!.visibility = View.VISIBLE
                myView?.findViewById<TextView>(R.id.tv_weapons_proficiencyTitle)!!.visibility = View.VISIBLE
                myView?.findViewById<Spinner>(R.id.spnr_weapons_proficiency)!!.visibility = View.VISIBLE
                myView?.findViewById<TextView>(R.id.tv_weapons_damageTitle)!!.visibility = View.VISIBLE
                myView?.findViewById<RecyclerView>(R.id.rv_weapons_damageDisplay)!!.visibility = View.VISIBLE
                myView?.findViewById<LinearLayout>(R.id.ll_weapons_damageAddLayout)!!.visibility = View.VISIBLE
                myView?.findViewById<Button>(R.id.b_weapons_addDamage)!!.visibility = View.VISIBLE
            }
            1 -> { //Armour
                Log.d("ItemAddFragment", "<hide>\tHiding armour...")
                myView?.findViewById<EditText>(R.id.armour_et_ac)!!.visibility = View.VISIBLE
                myView?.findViewById<Spinner>(R.id.armour_spnr_armourProf)!!.visibility = View.VISIBLE
                myView?.findViewById<TextView>(R.id.armour_tv_atmourProfTitle)!!.visibility = View.VISIBLE

            }
            2 -> { //Consumable
                Log.d("ItemAddFragment", "<hide>\tHiding consumable...")

            }
            3 -> { //Other
                Log.d("ItemAddFragment", "<hide>\tHiding other...")


            }
        }
    }

    private fun updateWeaponsProficiency(proficiency: String, range: String)
    {
        val spinner = view?.findViewById<Spinner>(R.id.spnr_weapons_proficiency)

        spinner.let {
            if (proficiency == "Simple")
            {
                if (range == "Melee")
                {// Simple Melee
                    val adapter = ArrayAdapter( //TODO: Spinner item
                        requireContext(),
                        android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponSimpleProficiencyMelee)
                    )
                    it!!.adapter = adapter
                }
                else
                {// Simple Ranged
                    val adapter = ArrayAdapter( //TODO: Spinner item
                        requireContext(),
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
                        requireContext(),
                        android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponMartialProficiencyMelee)
                    )
                    it!!.adapter = adapter
                }
                else
                {// Martial Ranged
                    val adapter = ArrayAdapter( //TODO: Spinner item
                        requireContext(),
                        android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponMartialProficiencyRanged)
                    )
                    it!!.adapter = adapter
                }
            }
        }
    }
}
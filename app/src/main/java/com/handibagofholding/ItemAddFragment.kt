package com.handibagofholding

import android.os.Bundle
import android.text.TextUtils
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
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemAddFragment : Fragment() {

    var cat: Int? = null
    var weaponRange = "Melee"
    var weaponProficiency = "Simple"

    lateinit var b_newItemConfirm: Button
    lateinit var et_itemName: EditText
    lateinit var spnr_category: Spinner
    lateinit var rg_weapons_proficiencyGroup: RadioGroup
    lateinit var rg_weapons_range: RadioGroup
    lateinit var spnr_weapons_proficiency: Spinner
    lateinit var rv_weapons_damageDisplay: RecyclerView
    lateinit var spnr_weapons_damageType: Spinner
    lateinit var spnr_weapons_damageDice: Spinner
    lateinit var et_weapons_damageDiceCount: EditText
    lateinit var b_weapons_addDamage: Button

    lateinit var damageArrayList: ArrayList<ItemDamageData>
    lateinit var damageAdapter: ItemDamageAdapter

    lateinit var et_armour_ac: EditText
    lateinit var spnr_armour_proficiency: Spinner

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
        et_itemName = view.findViewById<EditText>(R.id.et_itemName)
        spnr_category = view.findViewById<Spinner>(R.id.spnr_category)
        b_newItemConfirm = view.findViewById<Button>(R.id.b_newItemConfirm)

        if (spnr_category != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                requireContext(),
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_itemCategories)
            )
            spnr_category.adapter = adapter
        }
        else Log.e("ItemAddFragment", "Can't find view!")

        //Weapons
        rg_weapons_proficiencyGroup = view.findViewById<RadioGroup>(R.id.rg_weapons_proficiencyGroup)
        rg_weapons_range = view.findViewById<RadioGroup>(R.id.rg_weapons_range)
        spnr_weapons_proficiency = view.findViewById<Spinner>(R.id.spnr_weapons_proficiency)
        spnr_weapons_damageType = view.findViewById<Spinner>(R.id.spnr_weapons_damageType)
        spnr_weapons_damageDice = view.findViewById<Spinner>(R.id.spnr_weapons_damageDice)
        et_weapons_damageDiceCount = view.findViewById<EditText>(R.id.et_weapons_damageCount)
        rv_weapons_damageDisplay = view.findViewById<RecyclerView>(R.id.rv_weapons_damageDisplay)
        b_weapons_addDamage = view.findViewById<Button>(R.id.b_weapons_addDamage)

        if (spnr_weapons_proficiency != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                requireContext(),
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_weaponSimpleProficiencyMelee)
            )
            spnr_weapons_proficiency.adapter = adapter
        }
        else Log.e("ItemAddFragment", "Can't find view!")

        if (spnr_weapons_damageType != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                requireContext(),
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_damageTypes)
            )
            spnr_weapons_damageType.adapter = adapter
        }
        else Log.e("ItemAddFragment", "Can't find view!")

        if (spnr_weapons_damageDice != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                requireContext(),
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_damageDice)
            )
            spnr_weapons_damageDice.adapter = adapter
        }
        else Log.e("ItemAddFragment", "Can't find view!")

        damageArrayList = ArrayList<ItemDamageData>()
        damageAdapter = ItemDamageAdapter(damageArrayList, requireActivity())
        rv_weapons_damageDisplay.adapter = damageAdapter

        //Armour
        et_armour_ac = view.findViewById<EditText>(R.id.et_armour_ac)
        spnr_armour_proficiency = view.findViewById<Spinner>(R.id.spnr_armour_proficiency)

        if (spnr_armour_proficiency != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                requireContext(),
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_armourProficiencies)
            )
            spnr_armour_proficiency.adapter = adapter
        }
        else Log.e("ItemAddFragment", "Can't find view!")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_weapons_damageDisplay.layoutManager = LinearLayoutManager(requireActivity())

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

        b_weapons_addDamage.setOnClickListener {
            if (et_weapons_damageDiceCount.text.isBlank())
            {
                Toast.makeText(context, "Dice count is requireds to add damage.", Toast.LENGTH_SHORT).show()
            }
            else {
                damageArrayList.add(ItemDamageData(spnr_weapons_damageType.selectedItem.toString(), et_weapons_damageDiceCount.text.toString().toInt(), spnr_weapons_damageDice.selectedItem.toString()))
                rv_weapons_damageDisplay.adapter?.notifyItemInserted(damageArrayList.size)
                Log.d("ItemAddFragment", "$damageArrayList")
            }
        }

        b_newItemConfirm.setOnClickListener {
            if (et_itemName.text.isBlank()) {
                Toast.makeText(requireActivity(), "Item name can not be empty.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                when (cat)
                {
                    0 -> {
                        Log.d("ItemAddFragment", "Selected prof: ${spnr_weapons_proficiency.selectedItem.toString()}")

                        val iId = ViewModel.db.collection("items").document().id

                        if (iId != null) {
                            val db = ViewModel.db

                            val itemName = et_itemName.text.toString().trim()
                            val oId = ViewModel.character
                            val category = spnr_category.selectedItem.toString().lowercase()

                            val metaData = hashMapOf(
                                "id" to "$iId",
                                "name" to "$itemName",
                                "owner" to "$oId",
                                "category" to "$category"
                            )


                            val weapon_metaData = hashMapOf(
                                "id" to "$iId",
                                "group" to "$weaponProficiency",
                                "range" to "$weaponRange",
                                "proficiency" to "${spnr_weapons_proficiency.selectedItem.toString()}",
                                "damage" to damageArrayList

                            )

                            db.runTransaction { transaction ->
                                transaction.set(db.collection("items").document(iId), metaData)

                                transaction.set(db.collection("weapons").document(iId), weapon_metaData)


                            }.addOnSuccessListener {
                                view.findNavController().navigateUp()
                                Toast.makeText(context, "Item Has been added to character.", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {e ->
                                Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
                            }
                        }


                    }
                    1 -> {
//TODO
                    }
                    2 -> {
//TODO
                    }
                    3 -> {
//TODO
                    }
                }
            }
        }

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
                rg_weapons_proficiencyGroup.visibility = View.GONE
                myView?.findViewById<TextView>(R.id.tv_weapons_rangeTitle)!!.visibility = View.GONE
                rg_weapons_range.visibility = View.GONE
                myView?.findViewById<TextView>(R.id.tv_weapons_proficiencyTitle)!!.visibility = View.GONE
                spnr_weapons_proficiency.visibility = View.GONE
                myView?.findViewById<TextView>(R.id.tv_weapons_damageTitle)!!.visibility = View.GONE
                rv_weapons_damageDisplay.visibility = View.GONE
                myView?.findViewById<LinearLayout>(R.id.ll_weapons_damageAddLayout)!!.visibility = View.GONE
                myView?.findViewById<Button>(R.id.b_weapons_addDamage)!!.visibility = View.GONE

            }
            1 -> { //Armour
                Log.d("ItemAddFragment", "<hide>\tHiding armour...")
                et_armour_ac.visibility = View.GONE
                spnr_armour_proficiency.visibility = View.GONE
                myView?.findViewById<TextView>(R.id.tv_armour_proficiencyTitle)!!.visibility = View.GONE

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
                rg_weapons_proficiencyGroup.visibility = View.VISIBLE
                myView?.findViewById<TextView>(R.id.tv_weapons_rangeTitle)!!.visibility = View.VISIBLE
                rg_weapons_range.visibility = View.VISIBLE
                myView?.findViewById<TextView>(R.id.tv_weapons_proficiencyTitle)!!.visibility = View.VISIBLE
                spnr_weapons_proficiency.visibility = View.VISIBLE
                myView?.findViewById<TextView>(R.id.tv_weapons_damageTitle)!!.visibility = View.VISIBLE
                rv_weapons_damageDisplay.visibility = View.VISIBLE
                myView?.findViewById<LinearLayout>(R.id.ll_weapons_damageAddLayout)!!.visibility = View.VISIBLE
                myView?.findViewById<Button>(R.id.b_weapons_addDamage)!!.visibility = View.VISIBLE
            }
            1 -> { //Armour
                Log.d("ItemAddFragment", "<hide>\tHiding armour...")
                et_armour_ac.visibility = View.VISIBLE
                spnr_armour_proficiency.visibility = View.VISIBLE
                myView?.findViewById<TextView>(R.id.tv_armour_proficiencyTitle)!!.visibility = View.VISIBLE

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
        spnr_weapons_proficiency.let {
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
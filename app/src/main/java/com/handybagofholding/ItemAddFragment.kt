package com.handybagofholding

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
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.findNavController

class ItemAddFragment : Fragment() {

    private var cat: Int = 0

    private lateinit var bNewItemConfirm: Button
    private lateinit var etItemName: EditText
    private lateinit var spnrCategory: Spinner

    private lateinit var weaponEditTile: WeaponEditTile

    private lateinit var armourEditTile: ArmourEditTile

    private lateinit var etConsumablesCount: EditText

    private lateinit var noteEditTile: NoteEditTile


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
        etItemName = view.findViewById(R.id.et_itemName)
        spnrCategory = view.findViewById(R.id.spnr_category)
        bNewItemConfirm = view.findViewById(R.id.b_newItemConfirm)

        if (spnrCategory != null) {
            val adapter = ArrayAdapter( //TODO: Spinner item
                requireContext(),
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sa_itemCategories)
            )
            spnrCategory.adapter = adapter
        }

        //Weapons

        weaponEditTile = view.findViewById(R.id.weaponEditTile)

        //Armour

        armourEditTile = view.findViewById(R.id.armourEditTile)

        //Consumables

        etConsumablesCount = view.findViewById(R.id.et_consumables_count)

        //Notes

        noteEditTile = view.findViewById(R.id.noteEditTile)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spnrCategory.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View?, position: Int, id: Long) {
                if (view != null)
                {
                    Log.d("ItemAddFragment", "<Category Spinner>\tItem Selected: $position")
                    filterDisplay(position)
                } else filterDisplay(cat)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d("ItemAddFragment", "<Category Spinner>\tNo item selected")
            }
        }


        bNewItemConfirm.setOnClickListener {
            if (etItemName.text.isBlank()) {
                Toast.makeText(requireActivity(), "Item name can not be empty.", Toast.LENGTH_SHORT).show()
            }
            else
            {

                Log.d("ItemAddFragment", "Selected cat: ${spnrCategory.selectedItem}")

                val iId = ViewModel.db.collection("items").document().id

                ViewModel.item = iId

                if (iId != null) {
                    val db = ViewModel.db

                    val itemName = etItemName.text.toString().trim()
                    val oId = ViewModel.character
                    val category = spnrCategory.selectedItem.toString().lowercase()

                    val metaData = hashMapOf(
                        "id" to iId,
                        "name" to itemName,
                        "owner" to oId,
                        "category" to category
                    )

                    var extendedMetaData: Any? = null

                    when (cat)
                    {
                        0 -> {
                            extendedMetaData = weaponEditTile.getValue()
                        }
                        1 -> {
                            extendedMetaData = armourEditTile.getValues()
                        }
                        2 -> {
                            var count = 1
                            if (etConsumablesCount.text.isNotBlank()) {
                                count = etConsumablesCount.text.toString().toInt()
                            }
                            else {
                                Toast.makeText(activity, "Count defaulted to 1", Toast.LENGTH_SHORT).show()
                            }

                            extendedMetaData = hashMapOf(
                                "id" to iId,
                                "count" to count)
                        }
                    }
                    db.runTransaction { transaction ->
                        transaction.set(db.collection("items").document(iId), metaData)


                        if (extendedMetaData != null) {
                            transaction.set(db.collection(category).document(iId), extendedMetaData!!
                            )
                        }

                        if (noteEditTile.getValue() != null) {
                            val note = noteEditTile.getValue()!!
                            transaction.set(db.collection("item_notes").document(iId), note)
                        }


                    }.addOnSuccessListener {
                        Log.d("ItemAddFragment", "Toast checker:")
                        Log.d("ItemAddFragment", context.toString())
                        context?.let {
                            Toast.makeText(it, "Item Has been added to character.", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {e ->
                        context?.let {
                            Toast.makeText(it, "$e", Toast.LENGTH_SHORT).show()
                        }
                    }
                    view.findNavController().navigateUp()
                }
                ViewModel.clearItemData()
            }
        }
        //Hiding All
        hide(0)
        hide(1)
        hide(2)
        hide(3)
    }

    private fun filterDisplay(optionPosition: Int)
    {
        Log.d("ItemAddFragment", "<filterDisplay>\tFiltering starting...")
        Log.d("ItemAddFragment", "<filterDisplay>\tHiding all...")
        //Hiding all elements


        hide(cat)


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
                weaponEditTile.visibility = View.GONE

            }
            1 -> { //Armour
                Log.d("ItemAddFragment", "<hide>\tHiding armour...")
                armourEditTile.visibility = View.GONE

            }
            2 -> { //Consumable
                Log.d("ItemAddFragment", "<hide>\tHiding consumable...")
                myView?.findViewById<EditText>(R.id.et_consumables_count)!!.visibility = View.GONE
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
                Log.d("ItemAddFragment", "<show>\tShowing weapons...")
                weaponEditTile.visibility = View.VISIBLE
            }
            1 -> { //Armour
                Log.d("ItemAddFragment", "<show>\tShowing armour...")
                armourEditTile.visibility = View.VISIBLE

            }
            2 -> { //Consumable
                Log.d("ItemAddFragment", "<show>\tShowing consumable...")
                myView?.findViewById<EditText>(R.id.et_consumables_count)!!.visibility = View.VISIBLE

            }
            3 -> { //Other
                Log.d("ItemAddFragment", "<show>\tShowing other...")


            }
        }
    }
}
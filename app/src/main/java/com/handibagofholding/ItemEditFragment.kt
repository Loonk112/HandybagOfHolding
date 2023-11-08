package com.handibagofholding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.firestore.ktx.toObject


class ItemEditFragment : Fragment() {

    lateinit var et_itemName: EditText
    lateinit var b_editCancel: Button
    lateinit var b_editConfirm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_item_edit, container, false)

        et_itemName = view.findViewById(R.id.et_itemName)
        b_editCancel = view.findViewById<Button>(R.id.b_editCancel)
        b_editConfirm = view.findViewById<Button>(R.id.b_editConfirm)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModel.db.collection("items").document(ViewModel.item).get().addOnSuccessListener {snapshot ->
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<ItemMetaData>()?.let {
                    et_itemName.setText(it.name)
                }
            }
            Log.d("ItemEditFragment", "Success in getting data")
        }.addOnFailureListener{
            Log.e("ItemEditFragment", "Error getting data", it)
        }

        b_editCancel.setOnClickListener {
            view.findNavController().navigateUp()
        }

        b_editConfirm.setOnClickListener {

            if (et_itemName.text.isBlank()) return@setOnClickListener

            val name = et_itemName.text.toString().trim()

            ViewModel.db.collection("items").document(ViewModel.item).update("name",name).addOnSuccessListener {
                context?.let {
                    Toast.makeText(it, "Item edited.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                context?.let {
                    Toast.makeText(
                        it,
                        "There was a problem with updating data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            view.findNavController().navigateUp()

        }
    }
}
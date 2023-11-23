package com.handybagofholding

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
import com.google.firebase.firestore.toObject


class ItemEditFragment : Fragment() {

    private lateinit var etItemName: EditText
    private lateinit var bEditCancel: Button
    private lateinit var bEditConfirm: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_item_edit, container, false)

        etItemName = view.findViewById(R.id.et_itemName)
        bEditCancel = view.findViewById(R.id.b_editCancel)
        bEditConfirm = view.findViewById(R.id.b_editConfirm)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModel.db.collection("items").document(ViewModel.item).get().addOnSuccessListener {snapshot ->
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<ItemMetaData>()?.let {
                    etItemName.setText(it.name)
                }
            }
            Log.d("ItemEditFragment", "Success in getting data")
        }.addOnFailureListener{
            Log.e("ItemEditFragment", "Error getting data", it)
        }

        bEditCancel.setOnClickListener {
            view.findNavController().navigateUp()
        }

        bEditConfirm.setOnClickListener {

            if (etItemName.text.isBlank()) return@setOnClickListener

            val name = etItemName.text.toString().trim()

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
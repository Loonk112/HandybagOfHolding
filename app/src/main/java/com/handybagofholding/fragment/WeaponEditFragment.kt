package com.handybagofholding.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.firestore.toObject
import com.handybagofholding.R
import com.handybagofholding.ViewModel
import com.handybagofholding.tile.WeaponEditTile
import com.handybagofholding.data.WeaponData

class WeaponEditFragment : Fragment() {

    private lateinit var editTile: WeaponEditTile
    private lateinit var bEditCancel: Button
    private lateinit var bEditConfirm: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weapon_edit, container, false)

        editTile = view.findViewById(R.id.editTile)
        bEditCancel = view.findViewById(R.id.b_editCancel)
        bEditConfirm = view.findViewById(R.id.b_editConfirm)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModel.db.collection("weapons").document(ViewModel.item).get().addOnSuccessListener { snapshot ->
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<WeaponData>()?.let {
                    editTile.setValue(it)
                }
            }
            Log.d("WeaponEditFragment", "Success in getting data")
        }.addOnFailureListener{
            Log.e("WeaponEditFragment", "Error getting data", it)
        }

        bEditCancel.setOnClickListener {
            view.findNavController().navigateUp()
        }

        bEditConfirm.setOnClickListener {

            ViewModel.db.collection("weapons").document(ViewModel.item).set(editTile.getValue()).addOnSuccessListener {
                context?.let {
                    Toast.makeText(it, "Item edited.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Log.e("WeaponEditFragment", "Error passing data", it)
                context?.let {
                    Toast.makeText(
                        requireContext(),
                        "There was a problem with updating data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            view.findNavController().navigateUp()

        }

    }

}
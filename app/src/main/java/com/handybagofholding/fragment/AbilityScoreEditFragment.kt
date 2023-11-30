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
import com.handybagofholding.data.AbilityScoreData
import com.handybagofholding.tile.AbilityScoreEditTile

class AbilityScoreEditFragment : Fragment() {

    private lateinit var editTile: AbilityScoreEditTile
    private lateinit var bEditCancel: Button
    private lateinit var bEditConfirm: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ability_score_edit, container, false)

        editTile = view.findViewById(R.id.editTile)
        bEditCancel = view.findViewById(R.id.b_editCancel)
        bEditConfirm = view.findViewById(R.id.b_editConfirm)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModel.db.collection("ability_scores").document(ViewModel.character).get().addOnSuccessListener { snapshot ->
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<AbilityScoreData>()?.let {
                    editTile.setValues(it)
                }
            }
            Log.d("AbilityScoreEditFragment", "Success in getting data")
        }.addOnFailureListener{
            Log.e("AbilityScoreEditFragment", "Error getting data", it)
        }

        bEditCancel.setOnClickListener {
            view.findNavController().navigateUp()
        }

        bEditConfirm.setOnClickListener {

            ViewModel.db.collection("ability_scores").document(ViewModel.character).set(editTile.getValues()).addOnSuccessListener {
                context?.let {
                    Toast.makeText(it, "Character edited.", Toast.LENGTH_SHORT).show()
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
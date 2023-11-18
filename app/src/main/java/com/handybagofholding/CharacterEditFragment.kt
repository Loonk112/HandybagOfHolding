package com.handybagofholding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.firestore.ktx.toObject


class CharacterEditFragment : Fragment() {

    private lateinit var editTile: CharacterEditTile
    private lateinit var bEditCancel: Button
    private lateinit var bEditConfirm: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_character_edit, container, false)

        editTile = view.findViewById(R.id.editTile)
        bEditCancel = view.findViewById(R.id.b_editCancel)
        bEditConfirm = view.findViewById(R.id.b_editConfirm)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModel.db.collection("characters").document(ViewModel.character).get().addOnSuccessListener {snapshot ->
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<CharacterMetaData>()?.let {
                    editTile.setValue(it)
                }
            }
            Log.d("CharacterEditFragment", "Success in getting data")
        }.addOnFailureListener{
            Log.e("CharacterEditFragment", "Error getting data", it)
        }

        bEditCancel.setOnClickListener {
            view.findNavController().navigateUp()
        }

        bEditConfirm.setOnClickListener {

            editTile.getValue()?.let {
                ViewModel.db.collection("characters").document(ViewModel.character).set(it).addOnSuccessListener {
                    context?.let {context ->
                        Toast.makeText(context, "Character edited.", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    context?.let {context ->
                        Toast.makeText(
                            context,
                            "There was a problem with updating data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                view.findNavController().navigateUp()
            } ?: context?.let {context ->
                Toast.makeText(
                    context,
                    "All fields must be filled.",
                    Toast.LENGTH_SHORT
                ).show()
            }




        }
    }
}
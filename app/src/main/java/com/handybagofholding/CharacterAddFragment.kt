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


class CharacterAddFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_character_add, container, false)

        val editTile = view.findViewById<CharacterEditTile>(R.id.editTile)

        view.findViewById<Button>(R.id.b_newCharacterCancel).setOnClickListener {
            view.findNavController().navigateUp()
        }
        view.findViewById<Button>(R.id.b_newCharacterConfirm).setOnClickListener {

            editTile.getValue()?.let {
                val charName = it.name

                val cId = ViewModel.db.collection("characters").document().id

                val uId = ViewModel.account
                val data = hashMapOf(
                    "id" to cId,
                    "name" to charName,
                    "player" to uId
                )

                ViewModel.db.collection("characters").document(cId).set(data)
                    .addOnSuccessListener {
                        Log.d("FirebaseCode", "New Character Success")
                        view.findNavController().navigateUp()
                    }.addOnFailureListener { e ->
                        Log.w("FirebaseCode", "Error writing new character", e)
                    }
            }?: Toast.makeText(requireActivity(), "All fields must be filled.", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}
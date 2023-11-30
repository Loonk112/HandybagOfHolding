package com.handybagofholding.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.handybagofholding.tile.CharacterEditTile
import com.handybagofholding.R
import com.handybagofholding.ViewModel
import com.handybagofholding.tile.AbilityScoreEditTile
import com.handybagofholding.tile.NoteEditTile


class CharacterAddFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_character_add, container, false)

        val characterEditTile = view.findViewById<CharacterEditTile>(R.id.characterEditTile)
        val abilityScoreEditTile = view.findViewById<AbilityScoreEditTile>(R.id.abilityScoreEditTile)
        val noteEditTile = view.findViewById<NoteEditTile>(R.id.noteEditTile)

        view.findViewById<Button>(R.id.b_newCharacterCancel).setOnClickListener {
            view.findNavController().navigateUp()
        }
        view.findViewById<Button>(R.id.b_newCharacterConfirm).setOnClickListener {

            characterEditTile.getValue()?.let {
                val charName = it.name

                val cId = ViewModel.db.collection("characters").document().id

                val uId = ViewModel.account
                ViewModel.character = cId
                val data = hashMapOf(
                    "id" to cId,
                    "name" to charName,
                    "player" to uId
                )
                val db = ViewModel.db

                db.runTransaction { transaction ->
                    transaction.set(db.collection("characters").document(cId), data)

                    transaction.set(db.collection("ability_scores").document(cId), abilityScoreEditTile.getValues())

                    if (noteEditTile.getValue() != null) {
                        val note = noteEditTile.getValue()!!
                        transaction.set(db.collection("character_notes").document(cId), note)
                    }

                }.addOnSuccessListener {
                    Log.d("FirebaseCode", "New Character Success")
                    view.findNavController().navigateUp()
                }.addOnFailureListener { e ->
                    Log.w("FirebaseCode", "Error writing new character", e)
                }

                ViewModel.clearCharacterData()

            }?: Toast.makeText(requireActivity(), "Character name must be filled.", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}
package com.handibagofholding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController

class NoteEditFragment : Fragment() {

    private lateinit var editTile: NoteEditTile
    private lateinit var bEditCancel: Button
    private lateinit var bEditConfirm: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note_edit, container, false)

        editTile = view.findViewById(R.id.editTile)
        bEditCancel = view.findViewById(R.id.b_editCancel)
        bEditConfirm = view.findViewById(R.id.b_editConfirm)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModel.db.collection("notes").document(ViewModel.item).get().addOnSuccessListener {snapshot ->
            if (snapshot != null && snapshot.exists())
            {
                snapshot.getString("note")?.let {
                    editTile.setValue(it)
                }
            }
            Log.d("NoteEditFragment", "Success in getting data")
        }.addOnFailureListener{
            Log.e("NoteEditFragment", "Error getting data", it)
        }

        bEditCancel.setOnClickListener {
            view.findNavController().navigateUp()
        }

        bEditConfirm.setOnClickListener {

            if (editTile.getValue() != null) {

                ViewModel.db.collection("notes").document(ViewModel.item).set(editTile.getValue()!!)
                    .addOnSuccessListener {
                        context?.let {
                            Toast.makeText(it, "Item edited.", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {er ->
                    Log.e("NoteEditFragment", "Error passing data", er)
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

}
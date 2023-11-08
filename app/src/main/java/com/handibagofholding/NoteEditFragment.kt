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
import com.google.firebase.firestore.ktx.toObject

class NoteEditFragment : Fragment() {

    lateinit var editTile: NoteEditTile
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
        val view = inflater.inflate(R.layout.fragment_note_edit, container, false)

        editTile = view.findViewById<NoteEditTile>(R.id.editTile)
        b_editCancel = view.findViewById<Button>(R.id.b_editCancel)
        b_editConfirm = view.findViewById<Button>(R.id.b_editConfirm)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModel.db.collection("notes").document(ViewModel.item).get().addOnSuccessListener {snapshot ->
            if (snapshot != null && snapshot.exists())
            {
                snapshot.getString("note")?.let {it ->
                    editTile.setValue(it)
                }
            }
            Log.d("NoteEditFragment", "Success in getting data")
        }.addOnFailureListener{
            Log.e("NoteEditFragment", "Error getting data", it)
        }

        b_editCancel.setOnClickListener {
            view.findNavController().navigateUp()
        }

        b_editConfirm.setOnClickListener {

            ViewModel.db.collection("notes").document(ViewModel.item).set(editTile.getValue()).addOnSuccessListener {
                context?.let {
                    Toast.makeText(it, "Item edited.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Log.e("NoteEditFragment", "Error passing data", it)
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
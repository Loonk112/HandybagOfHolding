package com.handibagofholding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class CharacterDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_character_details, container, false)

        val name = view.findViewById<TextView>(R.id.tv_name)

        ViewModel.db.collection("characters").document("${ViewModel.character}")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("FirestoreResults", "Listen failed.", e)
                    return@addSnapshotListener
                }
                Log.d("FirestoreResults", "${snapshot?.data}")
                if (snapshot != null && snapshot.exists()) {
                    name.text = snapshot.get("name").toString()
                } else {
                    Log.d("FirestoreResults", "Current data: null")
                }
            }


        view.findViewById<ImageButton>(R.id.ib_return).setOnClickListener {
            requireActivity().finish()
        }

        return view

    }

}
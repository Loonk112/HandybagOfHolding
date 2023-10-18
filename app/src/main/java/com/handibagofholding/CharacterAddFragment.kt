package com.handibagofholding

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CharacterAddFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_character_add, container, false)

        val et_characterName = view.findViewById<EditText>(R.id.et_characterName)

        view.findViewById<Button>(R.id.b_newCharacterCancel).setOnClickListener {
            view.findNavController().navigateUp()
        }
        view.findViewById<Button>(R.id.b_newCharacterConfirm).setOnClickListener {
            if (TextUtils.isEmpty(et_characterName.text.toString().trim {it <= ' '})) {
                Toast.makeText(requireActivity(), "Character name can not be empty.", Toast.LENGTH_SHORT).show()
            }
            else {
                val db = Firebase.firestore

                val charName = et_characterName.text.toString().trim {it <= ' '}

                val uId = ViewModel.account
                val data = hashMapOf(
                    "userId" to "$uId"
                )

                db.collection("characters").add(data)
                    .addOnSuccessListener {
                        val charData = hashMapOf(
                            "id" to "${it.id}",
                            "name" to "$charName"
                        )
                        db.collection("user_characters").document("$uId").update("characters", FieldValue.arrayUnion(charData)).addOnSuccessListener {
                            view.findNavController().navigateUp()
                        }.addOnFailureListener { e ->
                            Log.w("FirebaseCode", "Error writing new character", e)
                        }
                    }.addOnFailureListener { e ->
                        Log.w("FirebaseCode", "Error writing new character", e)
                    }
            }
        }

        return view
    }

}
package com.handibagofholding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CharactersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var characterArrayList: ArrayList<CharacterMetaData>
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_characters, container, false)

        recyclerView = view.findViewById(R.id.rv_characters)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        view.findViewById<ImageButton>(R.id.ib_newCharacter).setOnClickListener {
            view.findNavController().navigate(R.id.action_charactersFragment_to_characterAddFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterArrayList = ArrayList<CharacterMetaData>()


        characterAdapter = CharacterAdapter(characterArrayList, requireActivity())
        recyclerView.adapter = characterAdapter

        val uId = ViewModel.account

        Log.d("FirebaseAuth","$uId")

        db = FirebaseFirestore.getInstance()
        db.collection("user_characters").document("$uId")
            .addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("FirestoreResults", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.w("FirestoreResults", "${snapshot?.data}")
            if (snapshot != null && snapshot.exists()) {
                val result = snapshot.get("characters") as List<Map<String, Object>>
                characterArrayList.clear()
                for (item in result)
                {
                    characterArrayList.add(CharacterMetaData("${item["id"]}", "${item["name"]}"))
                }
                Log.d("FirestoreResults","${characterArrayList}")
                characterAdapter.notifyDataSetChanged()
            } else {
                Log.d("FirestoreResults", "Current data: null")
            }
        }
    }
}
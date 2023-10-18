package com.handibagofholding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ItemsFragment() : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemArrayList: ArrayList<ItemMetaData>
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_items, container, false)

        recyclerView = view.findViewById(R.id.rv_items)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        view.findViewById<ImageButton>(R.id.ib_newItem).setOnClickListener {
            // TODO: ADD!
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemArrayList = ArrayList<ItemMetaData>()


        itemAdapter = ItemAdapter(itemArrayList, requireActivity())
        recyclerView.adapter = itemAdapter

        db = FirebaseFirestore.getInstance()
        db.collection("character_items").document("${ViewModel.character}")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("FirestoreResults", "Listen failed.", e)
                    return@addSnapshotListener
                }
                Log.w("FirestoreResults", "${snapshot?.data}")
                if (snapshot != null && snapshot.exists()) {
                    val result = snapshot.get("items") as List<Map<String, Object>>
                    itemArrayList.clear()
                    for (item in result)
                    {
                        itemArrayList.add(ItemMetaData("${item["id"]}", "${item["name"]}","${item["category"]}"))
                    }
                    Log.d("FirestoreResults","${itemArrayList}")
                    itemAdapter.notifyDataSetChanged()
                } else {
                    Log.d("FirestoreResults", "Current data: null")
                }
            }
    }

}
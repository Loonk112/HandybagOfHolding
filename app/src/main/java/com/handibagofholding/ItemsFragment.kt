package com.handibagofholding

import android.content.ClipData.Item
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ItemsFragment() : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var itemArrayList: ArrayList<ItemMetaData> = ArrayList<ItemMetaData>()
    private var displayArrayList: ArrayList<ItemMetaData> = ArrayList<ItemMetaData>()
    private lateinit var itemAdapter: ItemAdapter
    private var filter: String? = null

    private var tabId: Int = 0
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

        val tabLayout = view.findViewById<TabLayout>(R.id.tl_itemTabLayout)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                if (tab != null) {

                    tabId = tab.id

                    if (tab.text == "all") {
                        filter = null
                    }
                    else filter = tab.text as String?
                }

                updateRV()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        view.findViewById<ImageButton>(R.id.ib_newItem).setOnClickListener {
            view.findNavController().navigate(R.id.action_itemsFragment_to_itemAddFragment)
        }

        view.findViewById<ImageButton>(R.id.ib_return).setOnClickListener {
            requireActivity().finish()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemArrayList = ArrayList<ItemMetaData>()
        displayArrayList = itemArrayList.clone() as ArrayList<ItemMetaData>

        itemAdapter = ItemAdapter(displayArrayList, requireActivity())
        recyclerView.adapter = itemAdapter

        val cId = ViewModel.character

        Log.d("ItemsFragment","$cId")

        filter = null

        ViewModel.db.collection("items").whereEqualTo("owner","$cId").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("ItemsFragment", "Listen failed.", e)
                return@addSnapshotListener
            }
            itemArrayList.clear()
            Log.d("ItemsFragment", "${snapshot?.documents}")
            if (snapshot != null && snapshot.documents.size > 0)
            {
                snapshot.documents.forEach {
                    it.toObject<ItemMetaData>()?.let { it1 ->
                        itemArrayList.add(it1)
                    }
                }
            }
            updateRV()
        }

    }

    private fun updateRV() {
        Log.d("updateRV", "Filter: $filter")
        displayArrayList.clear()
        if (filter != null) {
            Log.d(
                "updateRV",
                "Filtered list: ${itemArrayList.filter { it -> it.category == filter }}"
            )
            displayArrayList.addAll(itemArrayList.filter { it -> it.category == filter } as ArrayList<ItemMetaData>)
        }
        else {
            displayArrayList.addAll(itemArrayList.clone() as ArrayList<ItemMetaData>)
        }
        itemAdapter.notifyDataSetChanged()
    }

}
package com.handybagofholding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import com.google.firebase.firestore.toObject

class ItemFragment : Fragment() {


    private lateinit var dataList: LinearLayout
    private lateinit var ibReturn: ImageButton
    private lateinit var itemInfo: ItemInfoTile
    private lateinit var noteInfo: ItemNoteInfoTile

    private var weaponInfoTile: WeaponInfoTile? = null
    private var armourInfo: ArmourInfoTile? = null
    private var consumableInfo: ConsumableInfoTile? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_item, container, false)

        dataList = view.findViewById(R.id.ll_itemDataList)
        itemInfo = view.findViewById(R.id.itemInfo)
        ibReturn = view.findViewById(R.id.ib_return)

        ViewModel.db.collection("items").document(ViewModel.item).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("ItemFragment", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("ItemFragment", "$snapshot")
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<ItemMetaData>()?.let {
                    when (it.category) {
                        "weapons" -> {
                            weaponInfoTile = WeaponInfoTile(requireContext())
                            dataList.addView(weaponInfoTile)
                        }
                        "armour" -> {
                            armourInfo = ArmourInfoTile(requireContext())
                            dataList.addView(armourInfo)
                        }
                        "consumables" -> {
                            consumableInfo = ConsumableInfoTile(requireContext())
                            dataList.addView(consumableInfo)
                        }
                    }
                }
            }
        }

        noteInfo = ItemNoteInfoTile(requireContext())
        dataList.addView(noteInfo)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ibReturn.setOnClickListener {
            requireActivity().finish()
        }

    }

}
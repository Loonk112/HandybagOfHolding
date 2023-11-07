package com.handibagofholding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.navigation.findNavController
import com.google.firebase.firestore.ktx.toObject

class ItemFragment : Fragment() {


    lateinit var dataList: LinearLayout
    lateinit var ib_return: ImageButton
    lateinit var itemInfo: ItemInfoTile
    lateinit var noteInfo: NoteInfoTile

    var weaponInfoTile: WeaponInfoTile? = null
    var armourInfo: ArmourInfoTile? = null
    var consumableInfo: ConsumableInfoTile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_item, container, false)

        dataList = view.findViewById<LinearLayout>(R.id.ll_itemDataList)
        itemInfo = view.findViewById<ItemInfoTile>(R.id.itemInfo)
        ib_return = view.findViewById<ImageButton>(R.id.ib_return)

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

        noteInfo = NoteInfoTile(requireContext())
        dataList.addView(noteInfo)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ib_return.setOnClickListener {
            requireActivity().finish()
        }

    }

}
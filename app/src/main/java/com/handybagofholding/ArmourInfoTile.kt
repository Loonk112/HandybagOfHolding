package com.handybagofholding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.ktx.toObject

class ArmourInfoTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val tvItemAC: TextView
    private val tvItemProficiency: TextView
    private val tvItemSlot: TextView

    init {
        inflate(context, R.layout.tile_armour_info, this)

        tvItemAC = findViewById(R.id.tv_itemAC)
        tvItemProficiency = findViewById(R.id.tv_itemProficiency)
        tvItemSlot = findViewById(R.id.tv_itemSlot)

        getItemInfo()

        this.setOnLongClickListener {
            Log.d("ArmourInfoTile", "Long click detected")
            findNavController().navigate(R.id.action_itemFragment_to_armourEditFragment)
            true
        }

    }

    private fun getItemInfo() {
        val iId = ViewModel.item

        ViewModel.db.collection("armour").document(iId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("ArmourInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("ArmourInfoTile", "$snapshot")
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<ArmourData>()?.let {
                    Log.d("ArmourInfoTile", "AC: ${it.ac}")
                    tvItemAC.text = it.ac.toString()
                    tvItemProficiency.text = it.proficiency
                    tvItemSlot.text = it.slot
                }
            }
        }
    }
}
package com.handibagofholding

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


    private val tv_itemAC: TextView
    private val tv_itemProficiency: TextView
    private val tv_itemSlot: TextView

    init {
        inflate(context, R.layout.armour_info_tile, this)

        tv_itemAC = findViewById(R.id.tv_itemAC)
        tv_itemProficiency = findViewById(R.id.tv_itemProficiency)
        tv_itemSlot = findViewById(R.id.tv_itemSlot)

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
            if (snapshot != null)
            {
                snapshot.toObject<ArmourData>()?.let {
                    Log.d("ArmourInfoTile", "AC: ${it.ac}")
                    tv_itemAC.text = it.ac.toString()
                    tv_itemProficiency.text = it.proficiency
                    tv_itemSlot.text = it.slot
                }
            }
        }
    }
}
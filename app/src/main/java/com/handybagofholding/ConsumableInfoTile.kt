package com.handybagofholding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.toObject

class ConsumableInfoTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val ibIncrease: ImageButton
    private val ibDecrease: ImageButton
    private val tvItemCount: TextView

    init {
        inflate(context, R.layout.tile_consumable_info, this)

        ibIncrease = findViewById(R.id.ib_increase)
        ibDecrease = findViewById(R.id.ib_decrease)
        tvItemCount = findViewById(R.id.tv_itemCount)

        ibDecrease.setOnClickListener {
            if (tvItemCount.text.toString().toInt() > 0) {
                ViewModel.db.collection("consumables").document(ViewModel.item).update("count", FieldValue.increment(-1))
            }
        }
        ibDecrease.setOnLongClickListener {
            if (tvItemCount.text.toString().toInt() > 10) {
                ViewModel.db.collection("consumables").document(ViewModel.item).update("count", FieldValue.increment(-10))
            }
            else if (tvItemCount.text.toString().toInt() > 0) {
                ViewModel.db.collection("consumables").document(ViewModel.item).update("count", 0)
            }
            true
        }

        ibIncrease.setOnClickListener {
            ViewModel.db.collection("consumables").document(ViewModel.item).update("count", FieldValue.increment(1))
        }
        ibIncrease.setOnLongClickListener {
            ViewModel.db.collection("consumables").document(ViewModel.item).update("count", FieldValue.increment(10))
            true
        }


        getItemInfo()

    }

    private fun getItemInfo() {
        val iId = ViewModel.item

        ViewModel.db.collection("consumables").document(iId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("ConsumableInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("ConsumableInfoTile", "$snapshot")
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<ConsumableData>()?.let {
                    tvItemCount.text = it.count.toString()
                }
            }
        }
    }
}
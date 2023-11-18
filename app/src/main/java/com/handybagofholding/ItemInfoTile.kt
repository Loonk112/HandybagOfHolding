package com.handybagofholding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.ktx.toObject
import java.util.Locale

class ItemInfoTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val tvItemName: TextView
    private val tvItemCategory: TextView

    init {
        inflate(context, R.layout.tile_item_info, this)

        tvItemName = findViewById(R.id.tv_itemName)
        tvItemCategory = findViewById(R.id.tv_itemCategory)

        getItemInfo()

        this.setOnLongClickListener {
            Log.d("ItemInfoTile", "Long click detected")
            findNavController().navigate(R.id.action_itemFragment_to_itemEditFragment)
            true
        }

    }

    private fun getItemInfo() {
        val iId = ViewModel.item

        ViewModel.db.collection("items").document(iId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("ItemInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("ItemInfoTile", "$snapshot")
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<ItemMetaData>()?.let {
                    tvItemName.text = it.name
                    tvItemCategory.text = it.category.uppercase(Locale.getDefault())
                }
            }
        }
    }
}
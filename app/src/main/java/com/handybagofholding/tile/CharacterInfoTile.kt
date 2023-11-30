package com.handybagofholding.tile

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.toObject
import com.handybagofholding.R
import com.handybagofholding.ViewModel
import com.handybagofholding.data.CharacterMetaData

class CharacterInfoTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val tvCharacterName: TextView

    init {
        inflate(context, R.layout.tile_character_info, this)

        tvCharacterName = findViewById(R.id.tv_characterName)

        getItemInfo()

        this.setOnLongClickListener {
            Log.d("CharacterInfoTile", "Long click detected")
            findNavController().navigate(R.id.action_characterDetailsFragment_to_characterEditFragment)
            true
        }

    }

    private fun getItemInfo() {
        val cId = ViewModel.character

        ViewModel.db.collection("characters").document(cId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("CharacterInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("CharacterInfoTile", "$snapshot")
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<CharacterMetaData>()?.let {
                    tvCharacterName.text = it.name
                }
            }
        }
    }
}
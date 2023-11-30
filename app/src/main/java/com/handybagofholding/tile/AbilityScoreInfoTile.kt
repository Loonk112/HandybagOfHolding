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
import com.handybagofholding.data.AbilityScoreData

class AbilityScoreInfoTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val tvStrength: TextView
    private val tvConstitution: TextView
    private val tvDexterity: TextView
    private val tvIntelligence: TextView
    private val tvWisdom: TextView
    private val tvCharisma: TextView

    private val tvStrengthMod: TextView
    private val tvConstitutionMod: TextView
    private val tvDexterityMod: TextView
    private val tvIntelligenceMod: TextView
    private val tvWisdomMod: TextView
    private val tvCharismaMod: TextView

    init {
        inflate(context, R.layout.tile_ability_score_info, this)

        tvStrength = findViewById(R.id.tvStrength)
        tvConstitution = findViewById(R.id.tvConstitution)
        tvDexterity = findViewById(R.id.tvDexterity)
        tvIntelligence = findViewById(R.id.tvIntelligence)
        tvWisdom = findViewById(R.id.tvWisdom)
        tvCharisma = findViewById(R.id.tvCharisma)

        tvStrengthMod = findViewById(R.id.tvStrengthMod)
        tvConstitutionMod = findViewById(R.id.tvConstitutionMod)
        tvDexterityMod = findViewById(R.id.tvDexterityMod)
        tvIntelligenceMod = findViewById(R.id.tvIntelligenceMod)
        tvWisdomMod = findViewById(R.id.tvWisdomMod)
        tvCharismaMod = findViewById(R.id.tvCharismaMod)

        getItemInfo()

        this.setOnLongClickListener {
            Log.d("AbilityScoreInfoTile", "Long click detected")
            findNavController().navigate(R.id.action_characterDetailsFragment_to_abilityScoreEditFragment)
            true
        }

    }

    private fun getItemInfo() {
        val cId = ViewModel.character

        ViewModel.db.collection("ability_scores").document(cId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("AbilityScoreInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("AbilityScoreInfoTile", "$snapshot")
            if (snapshot != null && snapshot.exists())
            {
                snapshot.toObject<AbilityScoreData>()?.let {
                    updateData(it)
                }
            }
        }
    }

    private fun updateData(newData: AbilityScoreData) {
        tvStrength.text = newData.strength.toString()
        tvConstitution.text = newData.constitution.toString()
        tvDexterity.text = newData.dexterity.toString()
        tvIntelligence.text = newData.intelligence.toString()
        tvWisdom.text = newData.wisdom.toString()
        tvCharisma.text = newData.charisma.toString()

        tvStrengthMod.text = (newData.strength/2 - 5).toString()
        tvConstitutionMod.text = (newData.constitution/2 - 5).toString()
        tvDexterityMod.text = (newData.dexterity/2 - 5).toString()
        tvIntelligenceMod.text = (newData.intelligence/2 - 5).toString()
        tvWisdomMod.text = (newData.wisdom/2 - 5).toString()
        tvCharismaMod.text = (newData.charisma/2 - 5).toString()
    }

}
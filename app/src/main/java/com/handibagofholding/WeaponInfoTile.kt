package com.handibagofholding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.ktx.toObject

class WeaponInfoTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val tv_itemGroup: TextView
    private val tv_itemProficiency: TextView
    private val tv_itemRange: TextView
    private val rv_itemDamage: RecyclerView

    private val damageAdapter: ItemDamageAdapter
    private var damageArrayList: ArrayList<ItemDamageData>

    init {
        inflate(context, R.layout.weapons_info_tile, this)

        tv_itemGroup = findViewById(R.id.tv_itemGroup)
        tv_itemProficiency = findViewById(R.id.tv_itemProficiency)
        tv_itemRange = findViewById(R.id.tv_itemRange)
        rv_itemDamage = findViewById(R.id.rv_itemDamage)


        rv_itemDamage.layoutManager = object: LinearLayoutManager(context) { override fun canScrollVertically() = false }

        damageArrayList = ArrayList<ItemDamageData>()
        damageAdapter = ItemDamageAdapter(damageArrayList, context)
        damageAdapter.canRemoveItems = false
        rv_itemDamage.adapter = damageAdapter

        getItemInfo()

        this.setOnLongClickListener {
            Log.d("WeaponInfoTile", "Long click detected")
            findNavController().navigate(R.id.action_itemFragment_to_weaponEditFragment)
            true
        }

    }

    private fun getItemInfo() {
        val iId = ViewModel.item

        ViewModel.db.collection("weapons").document(iId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("WeaponInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("WeaponInfoTile", "$snapshot")
            if (snapshot != null && snapshot.exists())
            {
                rv_itemDamage.suppressLayout(false)

                damageArrayList.clear()
                snapshot.toObject<WeaponData>()?.let {
                    tv_itemGroup.text = it.group
                    tv_itemProficiency.text = it.proficiency
                    tv_itemRange.text = it.range
                    it.damage.let { it ->
                        it!!.forEach { it ->
                            damageArrayList.add(it)
                        }
                    }
                    Log.d("WeaponInfoTile", "${damageArrayList}")
                }
                damageAdapter.notifyDataSetChanged()
                rv_itemDamage.suppressLayout(true)
            }
        }
    }
}
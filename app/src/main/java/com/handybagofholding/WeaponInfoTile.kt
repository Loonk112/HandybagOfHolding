package com.handybagofholding

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.toObject

class WeaponInfoTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val tvItemGroup: TextView
    private val tvItemProficiency: TextView
    private val tvItemRange: TextView
    private val rvItemDamage: RecyclerView

    private val damageAdapter: ItemDamageAdapter
    private var damageArrayList: ArrayList<ItemDamageData>

    init {
        inflate(context, R.layout.weapons_info_tile, this)

        tvItemGroup = findViewById(R.id.tv_itemGroup)
        tvItemProficiency = findViewById(R.id.tv_itemProficiency)
        tvItemRange = findViewById(R.id.tv_itemRange)
        rvItemDamage = findViewById(R.id.rv_itemDamage)


        rvItemDamage.layoutManager = object: LinearLayoutManager(context) { override fun canScrollVertically() = false }

        damageArrayList = ArrayList()
        damageAdapter = ItemDamageAdapter(damageArrayList, context)
        damageAdapter.canRemoveItems = false
        rvItemDamage.adapter = damageAdapter

        getItemInfo()

        this.setOnLongClickListener {
            Log.d("WeaponInfoTile", "Long click detected")
            findNavController().navigate(R.id.action_itemFragment_to_weaponEditFragment)
            true
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getItemInfo() {
        val iId = ViewModel.item

        ViewModel.db.collection("weapons").document(iId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("WeaponInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("WeaponInfoTile", "$snapshot")
            if (snapshot != null && snapshot.exists()) {
                rvItemDamage.suppressLayout(false)

                damageArrayList.clear()
                snapshot.toObject<WeaponData>()?.let {
                    tvItemGroup.text = it.group
                    tvItemProficiency.text = it.proficiency
                    tvItemRange.text = it.range
                    it.damage.let { damageList ->
                        damageList!!.forEach { damage ->
                            damageArrayList.add(damage)
                        }
                    }
                    Log.d("WeaponInfoTile", "$damageArrayList")
                }
                damageAdapter.notifyDataSetChanged()
                rvItemDamage.suppressLayout(true)
            }
        }
    }
}
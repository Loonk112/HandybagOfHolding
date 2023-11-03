package com.handibagofholding

data class WeaponData(val id: String = "", val group: String = "", val range: String = "", val proficiency: String = "", val damage: ArrayList<ItemDamageData>? = null)

package com.handibagofholding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings

object ViewModel {
    private var _account: String
    private var _character: String
    private var _item: String
    private val _db: FirebaseFirestore
    private val _auth: FirebaseAuth

    init {
        _account = "null"
        _character = "null"
        _item = "null"
        _db = FirebaseFirestore.getInstance()
        _auth = FirebaseAuth.getInstance()
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {})
            setLocalCacheSettings(persistentCacheSettings {})
        }
        _db.firestoreSettings = settings
    }

    var account: String
        get() = _account
        set(value) {_account = value}
    var character: String
        get() = _character
        set(value) {_character = value}
    var item: String
        get() = _item
        set(value) {_item = value}

    val db: FirebaseFirestore
        get() = _db
    val auth: FirebaseAuth
        get() = _auth

    fun clearAccountData()
    {
        _account = "null"
        _character = "null"
        _item = "null"
    }
    fun clearCharacterData()
    {
        _character = "null"
        _item = "null"
    }
    fun clearItemData()
    {
        _item = "null"
    }
}
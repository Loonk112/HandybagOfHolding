package com.handibagofholding

object ViewModel {
    private var _account: String = "null"
    private var _character: String = "null"
    private var _item: String = "null"

    var account: String
        get() = _account
        set(value) {_account = value}
    var character: String
        get() = _character
        set(value) {_character = value}
    var item: String
        get() = _item
        set(value) {_item = value}

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
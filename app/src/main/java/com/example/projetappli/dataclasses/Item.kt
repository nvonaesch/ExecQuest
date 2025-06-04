package com.example.projetappli.dataclasses

data class Item(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val description: String
) {
    var bonus_hp: Int = 0
    var bonus_str: Int = 0
    var bonus_int: Int = 0
    var bonus_def: Int = 0
    var bonus_spd: Int = 0
}
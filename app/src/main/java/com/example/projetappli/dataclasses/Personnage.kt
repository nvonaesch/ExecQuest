package com.example.projetappli.dataclasses


data class Personnage(
    val nom: String,
    val hp: Int,
    val str: Int,
    val int: Int,
    val def: Int,
    val spd: Int,
    val niveau: Int = 1,
    val experience: Int = 0
)
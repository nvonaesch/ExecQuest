package com.example.projetappli.dataclasses


data class Chapitre(
    val titre: String,
    val contenu: String,
    val estDebloque: Boolean = false
)

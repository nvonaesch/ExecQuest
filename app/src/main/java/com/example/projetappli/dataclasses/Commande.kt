package com.example.projetappli.dataclasses


data class Commande(
    val nom: String,
    val description: String,
    val debloquee: Boolean = false
)
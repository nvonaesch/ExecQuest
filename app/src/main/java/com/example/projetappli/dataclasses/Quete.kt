package com.example.projetappli.dataclasses


data class Quete(
    val numero: Int,
    val titre: String,
    val description: String,
    val etapes: List<EtapeQuete>
)


data class EtapeQuete(
    val numero: Int,
    val description: String,
    val type: TypeEtape,
    val objectif: String,
    var estDebloquee: Boolean = false,
    var estEffectuee: Boolean = false
)

enum class TypeEtape {
    TROUVER,
    COMBATTRE,
    UTILISER_COMMANDE,
}
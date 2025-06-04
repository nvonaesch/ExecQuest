package com.example.projetappli.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetappli.dataclasses.Chapitre
import com.example.projetappli.dataclasses.Item
import com.example.projetappli.dataclasses.Personnage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class JeuViewModel : ViewModel() {
    private val _chapitresDisponibles = MutableStateFlow(
        listOf(
            Chapitre("Chapitre 1: Introduction", "Bienvenue dans le monde d'ExecQuest.`\nExécutez votre premiere commande pour progresser", estDebloque = true),
            Chapitre("Chapitre 2: Le pouvoir des commandes", "Apprenez à utiliser ./createCharacter", estDebloque = false),
            Chapitre("Chapitre 3: Le premier combat", "Votre premier affrontement commence ici...", estDebloque = false),
        )
    )

    val chapitresDisponibles: StateFlow<List<Chapitre>> = _chapitresDisponibles

    private val _items = MutableStateFlow<List<Item>>(listOf(
        Item(1001, "Item 1", "", "Description").apply {
            bonus_hp = 5
            bonus_str = 3
            bonus_def = 2
            bonus_int = 1
            bonus_spd = 4
        },
        Item(1002, "Item 2", "", "Description").apply {
            bonus_hp = 2
            bonus_str = 6
            bonus_def = 3
            bonus_int = 0
            bonus_spd = 2
        }
    ))
    val items: StateFlow<List<Item>> = _items

    private val _personnages = MutableStateFlow<List<Personnage>>(emptyList())
    val personnages: StateFlow<List<Personnage>> = _personnages

    private val _commandesDisponibles = MutableStateFlow(listOf("./start"))
    val commandesDisponibles: StateFlow<List<String>> = _commandesDisponibles

    private val _messages = MutableStateFlow(listOf("Bienvenue dans ExecQuest !\nConsulte la documentation !"))
    val messages: StateFlow<List<String>> = _messages

    private var personnageCree = false

    fun envoyerCommande(cmd: String) {
        val newMessages = _messages.value.toMutableList()
        newMessages.add("→ $cmd")

        val currentCommandes = _commandesDisponibles.value.toMutableList()

        when (cmd) {
            "./start" -> {
                newMessages.add("Félicitation pour ta première commande ! Crée maintenant ton personnage !.")

                currentCommandes.remove("./start")

                if ("./createCharacter" !in currentCommandes) {
                    currentCommandes.add("./createCharacter")
                }
            }

            cmd.startsWith("./createCharacter").toString() -> {
                val regex = Regex("""-n\s+([A-Za-z0-9_]+)""")
                val match = regex.find(cmd)
                val nom = match?.groupValues?.get(1)

                if (nom != null) {
                    val nouveauPerso = Personnage(nom,10,5,5,5,5)
                    val listeActuelle = _personnages.value.toMutableList()
                    listeActuelle.add(nouveauPerso)
                    _personnages.value = listeActuelle

                    newMessages.add("Personnage '$nom' créé avec succès !")

                    val toAdd = listOf("./stats", "./items", "./chapter1")
                    toAdd.forEach {
                        if (it !in currentCommandes) currentCommandes.add(it)
                    }

                    val updatedChapitres = _chapitresDisponibles.value.map {
                        if (it.titre.startsWith("Chapitre 2")) it.copy(estDebloque = true) else it
                    }
                    _chapitresDisponibles.value = updatedChapitres
                } else {
                    newMessages.add("Commande invalide. Utilise : ./createCharacter -n Nom")
                }
            }


            "./stats" -> newMessages.add("Voici les statistiques de ton personnage.")
            "./items" -> newMessages.add("Tu n’as pas encore d’équipement.")
            "./chapter1" -> newMessages.add("Tu ouvres le chapitre 1...")

            else -> newMessages.add("Commande inconnue ou non autorisée.")
        }

        _commandesDisponibles.value = currentCommandes
        _messages.value = newMessages
    }

    suspend fun getItemsById(itemIds: List<Int>): List<Item> {
        val url = "https://ddragon.leagueoflegends.com/cdn/14.10.1/data/en_US/item.json"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        val items = mutableListOf<Item>()
        if (response.isSuccessful) {
            val json = JSONObject(response.body?.string() ?: "")
            val data = json.getJSONObject("data")

            for (id in itemIds) {
                val itemObj = data.optJSONObject(id.toString()) ?: continue
                val name = itemObj.getString("name")
                val description = itemObj.getString("description")
                val imageUrl = "https://ddragon.leagueoflegends.com/cdn/14.10.1/img/item/$id.png"
                val item = Item(id, name, imageUrl, description)
                appliquerBonusSelonId(item)
                items.add(item)
            }
        }

        return items
    }
    fun chargerItems(ids: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            val itemsApi = getItemsById(ids)
            withContext(Dispatchers.Main) {
            _items.value = itemsApi
            }
        }
    }

    fun appliquerBonusSelonId(item: Item) {
        when (item.id) {
            1055 -> { //Doran's blade
                item.bonus_str = 6
            }

            224403 -> { //Golden Spatula
                item.bonus_hp = 2
                item.bonus_spd = 2
                item.bonus_def = 2
                item.bonus_int = 2
                item.bonus_str = 2
            }

            3006 -> { //Berserker's Greaves
                item.bonus_spd = 9
            }

            1082 -> { //Dark Seal
                item.bonus_int = 7
            }

            3047 -> { //Plated Steelcaps
                item.bonus_spd = 4
                item.bonus_def = 3
            }

            else -> {
            }
        }
    }

}
package com.example.projetappli.repository

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetappli.dataclasses.Chapitre
import com.example.projetappli.dataclasses.EtapeQuete
import com.example.projetappli.dataclasses.Item
import com.example.projetappli.dataclasses.Personnage
import com.example.projetappli.dataclasses.Quete
import com.example.projetappli.dataclasses.TypeEtape
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
            Chapitre("Chapitre 1: Arrivée à ExecQuest", "Bienvenue dans le monde d'ExecQuest.\nExécutez votre premiere commande pour progresser", estDebloque = true),
            Chapitre("Chapitre 2: Création de personnage", "Apprenez à créer votre premier personnage !", estDebloque = false),
            Chapitre("Chapitre 3: Sélection de personnage", "Changer votre personnage sélectionné !", estDebloque = false),
            Chapitre("Chapitre 4: Votre première quête", "Lancez votre première quête ! ", estDebloque = false),
        )
    )

    val chapitresDisponibles: StateFlow<List<Chapitre>> = _chapitresDisponibles

    private val _spriteGauche = MutableStateFlow<String?>("idle")
    val spriteGauche: StateFlow<String?> = _spriteGauche
    private val _spriteMilieu = MutableStateFlow<String?>("idle")
    val spriteMilieu: StateFlow<String?> = _spriteMilieu
    private val _spriteDroite = MutableStateFlow<String?>("idle")
    val spriteDroite: StateFlow<String?> = _spriteDroite


    private val _items = MutableStateFlow<List<Item>>(listOf())
    val items: StateFlow<List<Item>> = _items

    private val _personnageActif = MutableStateFlow<Personnage?>(null)
    val personnageActif: StateFlow<Personnage?> = _personnageActif

    private val _personnages = MutableStateFlow<List<Personnage>>(emptyList())
    val personnages: StateFlow<List<Personnage>> = _personnages

    private val _commandesDisponibles = MutableStateFlow(listOf("./start"))
    val commandesDisponibles: StateFlow<List<String>> = _commandesDisponibles

    private val _messages = MutableStateFlow(listOf("Bienvenue dans ExecQuest !\nConsulte la documentation !"))
    val messages: StateFlow<List<String>> = _messages

    private val _quetes = MutableStateFlow<List<Quete>>(emptyList())
    val quetes: StateFlow<List<Quete>> = _quetes

    private var queteActuelle: Quete? = null
    private var etapeActuelle: EtapeQuete? = null


    private var personnageCree = false

    fun envoyerCommande(cmd: String) {
        val newMessages = _messages.value.toMutableList()
        newMessages.add("→ $cmd")

        val currentCommandes = _commandesDisponibles.value.toMutableList()
        Log.d("JeuViewModel", cmd)
        when  {
            // Commencer l'aventure
            cmd == "./start"  -> {
                newMessages.add("Félicitation pour ta première commande ! Crée maintenant ton personnage !.")

                currentCommandes.remove("./start")

                if ("./createCharacter" !in currentCommandes) {
                    currentCommandes.add("./createCharacter -n [Nom]")
                }

                val updatedChapitres = _chapitresDisponibles.value.map {
                    if (it.titre.startsWith("Chapitre 2")) it.copy(estDebloque = true) else it
                }

                _chapitresDisponibles.value = updatedChapitres
            }

            // Changer de personnage
            cmd.startsWith("./switchCharacter") -> {
                val regex = Regex("""-n\s+([A-Za-z0-9_]+)""")
                val match = regex.find(cmd)
                val nom = match?.groupValues?.get(1)

                val matchingPerso = _personnages.value.find { it.nom.equals(nom, ignoreCase = true) }
                if (matchingPerso != null) {
                    val toAdd = listOf("./quest -n [N°Quete] -s [Etape]", "./currentQuest")
                    toAdd.forEach {
                        if (it !in currentCommandes) currentCommandes.add(it)
                    }

                    initialiserQuetes()

                    val updatedChapitres = _chapitresDisponibles.value.map {
                        if (it.titre.startsWith("Chapitre 4")) it.copy(estDebloque = true) else it
                    }

                    _chapitresDisponibles.value = updatedChapitres
                    _personnageActif.value = matchingPerso
                    newMessages.add("Personnage actif changé vers '$nom'.")
                } else {
                    newMessages.add("Aucun personnage nommé '$nom' trouvé.")
                }
            }

            // Créer un personnage
            cmd.startsWith("./createCharacter") -> {
                val regex = Regex("""-n\s+([A-Za-z0-9_]+)""")
                val match = regex.find(cmd)
                Log.d("JeuViewModel", match.toString())
                val nom = match?.groupValues?.get(1)

                if (nom != null) {
                    if (_personnages.value.size >= 5) {
                        newMessages.add("Tu ne peux pas avoir plus de 5 personnages.")
                    } else {
                        val nouveauPerso = Personnage(nom, 10, 5, 5, 5, 5)
                        val listeActuelle = _personnages.value.toMutableList()
                        listeActuelle.add(nouveauPerso)
                        _personnages.value = listeActuelle
                        _personnageActif.value = nouveauPerso

                        newMessages.add("Personnage '$nom' créé avec succès.")
                    }

                    val toAdd = listOf("./switchCharacter -n [Nom]")
                    toAdd.forEach {
                        if (it !in currentCommandes) currentCommandes.add(it)
                    }
                    val updatedChapitres = _chapitresDisponibles.value.map {
                        if (it.titre.startsWith("Chapitre 3")) it.copy(estDebloque = true) else it
                    }

                    _chapitresDisponibles.value = updatedChapitres
                } else {
                    newMessages.add("Commande invalide.  [./createCharacter -n [Nom]]")
                }
            }

            // Permet de choisir une quête
            cmd.startsWith("./quest") -> {
                val regex = Regex("""-n\s+(\d+)\s+-s\s+(\d+)""")
                val match = regex.find(cmd)
                val numero = match?.groupValues?.get(1)?.toIntOrNull()
                val step = match?.groupValues?.get(2)?.toIntOrNull()

                if (numero != null && step != null) {
                    val quete = _quetes.value.find { it.numero == numero }
                    val etape = quete?.etapes?.find { it.numero == step }

                    if (quete != null && etape != null) {
                        if (etape.estDebloquee && !etape.estEffectuee) {
                            etape.estEffectuee = true
                            newMessages.add("Étape ${step} complétée : ${etape.description}")

                            val nextEtape = quete.etapes.find { it.numero == step + 1 }
                            nextEtape?.estDebloquee = true

                            if (quete.numero == 1 && etape.numero == 1) {
                                _spriteGauche.value = "coupry"
                                _spriteDroite.value = "apparicio"
                                newMessages.add("???: Hé toi ! Tu veux devenir un héros ? Accepte cette quête !")
                                newMessages.add("Utilise la commande ./accept pour commencer.")
                            }

                            queteActuelle = quete
                            etapeActuelle = nextEtape
                        } else if (!etape.estDebloquee) {
                            newMessages.add("Étape ${step} non encore débloquée.")
                        } else {
                            newMessages.add("Étape ${step} déjà effectuée.")
                        }
                    } else {
                        newMessages.add("Quête ou étape invalide.")
                    }
                } else {
                    newMessages.add("Commande invalide. Utilise : ./quest -n <num> -s <step>")
                }
            }

            cmd == "./accept" -> {
                newMessages.add("???: Excuse moi, je ne me suis pas encore présenter !!")
                newMessages.add("Apricio: Je suis Apricio, tiens équipe donc cette épée !")
                newMessages.add("Vous avez obtenu un bout de bois !")
                newMessages.add("Tu es maintenant prêt à combattre, utilise ./fight !")
                accomplirEtapeSiObjectifRempli("accept", "")
            }

            cmd == "./currentQuest" -> {
                if (queteActuelle != null) {
                    newMessages.add("Quête ${queteActuelle!!.numero} : ${queteActuelle!!.titre}")
                    for (etape in queteActuelle!!.etapes) {
                        val statut = when {
                            etape.estEffectuee -> "✅"
                            etape.estDebloquee -> "🟡"
                            else -> "🔒"
                        }
                        newMessages.add("$statut Étape ${etape.numero}: ${etape.description}")
                    }
                } else {
                    newMessages.add("Aucune quête en cours.")
                }
            }

            cmd == "./equipArme" -> {
                val perso = _personnageActif.value
                if (perso != null) {
                    val nouveauPerso = perso.copy(str = perso.str + 2)
                    _personnageActif.value = nouveauPerso

                    val nouvelleListe = _personnages.value.map {
                        if (it.nom == perso.nom) nouveauPerso else it
                    }
                    _personnages.value = nouvelleListe

                    newMessages.add("${perso.nom} a équipé une arme.")
                    accomplirEtapeSiObjectifRempli("equip", "arme")
                } else {
                    newMessages.add("Aucun personnage actif pour équiper l'arme.")
                }
            }

            cmd == "./fight" -> {
                _spriteMilieu.value = "fireball"
                _spriteGauche.value = "coupry"
                _spriteDroite.value = "florine"
                val perso = _personnageActif.value
                if (perso != null) {
                    ajouterExperienceAuPersonnageActif(70)
                    accomplirEtapeSiObjectifRempli("combat", "combat_tutoriel")
                } else {
                    newMessages.add("Aucun personnage actif pour se battre.")
                }
            }

            else -> newMessages.add("Commande inconnue ou non autorisée.")
        }



        _commandesDisponibles.value = currentCommandes
        _messages.value = newMessages
    }

    // Recuperation des items
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


    fun experienceRequisePourNiveau(niveau: Int): Int {
        val experienceParNiveau = arrayOf(
            0,100,170,290,400,
        )
        return if (niveau in 1 until experienceParNiveau.size) {
            experienceParNiveau[niveau]
        } else {
            1257672200
        }
    }

    fun ajouterExperienceAuPersonnageActif(xpGagnee: Int) {
        val perso = _personnageActif.value ?: return

        var experienceActuelle = perso.experience + xpGagnee
        var niveauActuel = perso.niveau

        while (experienceActuelle >= experienceRequisePourNiveau(niveauActuel)) {
            experienceActuelle -= experienceRequisePourNiveau(niveauActuel)
            niveauActuel += 1
        }

        val nouveauPerso = perso.copy(
            experience = experienceActuelle,
            niveau = niveauActuel
        )

        _personnageActif.value = nouveauPerso

        val nouvelleListe = _personnages.value.map {
            if (it.nom == perso.nom) nouveauPerso else it
        }
        _personnages.value = nouvelleListe

        val newMessages = _messages.value.toMutableList()
        newMessages.add("${perso.nom} a gagné $xpGagnee XP. Niveau actuel: $niveauActuel.")
        _messages.value = newMessages
    }

    fun initialiserQuetes() {
        val quetes = listOf(
            Quete(
                numero = 1,
                titre = "Introduction à ExecQuest",
                description = "Apprends les bases du monde d'ExecQuest.",
                etapes = listOf(
                    EtapeQuete(
                        numero = 1,
                        description = "Quête ouverte",
                        type = TypeEtape.UTILISER_COMMANDE,
                        objectif = "accept",
                        estDebloquee = true
                    ),
                    EtapeQuete(
                        numero = 2,
                        description = "Accepter la quête.",
                        type = TypeEtape.UTILISER_COMMANDE,
                        objectif = "accept",
                        estDebloquee = true
                    ),
                    EtapeQuete(
                        numero = 3,
                        description = "Équipe une arme",
                        type = TypeEtape.TROUVER,
                        objectif = "arme"
                    ),
                    EtapeQuete(
                        numero = 4,
                        description = "Battre le Riflone",
                        type = TypeEtape.COMBATTRE,
                        objectif = "combat_tutoriel"
                    )
                )
            )
        )

        _quetes.value = quetes
    }

    fun accomplirEtapeSiObjectifRempli(action: String, cible: String) {
        val quete = queteActuelle ?: return
        val etape = quete.etapes.find { it.estDebloquee && !it.estEffectuee }
        Log.d("Debug", "Étape candidate: ${etape?.numero}, objectif=${etape?.objectif}")

        if (etape != null && when (etape.type) {
                TypeEtape.UTILISER_COMMANDE -> action == "accept"
                TypeEtape.TROUVER -> cible == etape.objectif
                TypeEtape.COMBATTRE -> cible == etape.objectif
            }) {
            etape.estEffectuee = true
            val indexSuivant = quete.etapes.indexOf(etape) + 1
            if (indexSuivant < quete.etapes.size) {
                quete.etapes[indexSuivant].estDebloquee = true
            }
            etapeActuelle = quete.etapes.getOrNull(indexSuivant)

            val newMessages = _messages.value.toMutableList()
            newMessages.add("Étape ${etape.numero} accomplie ! ${etape.description}")
            _messages.value = newMessages
        }
    }
}
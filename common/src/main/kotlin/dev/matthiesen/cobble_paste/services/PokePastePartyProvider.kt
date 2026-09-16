package dev.matthiesen.cobble_paste.services

import com.cobblemon.mod.common.api.npc.NPCPartyProvider
import com.cobblemon.mod.common.api.storage.party.NPCPartyStore
import com.cobblemon.mod.common.entity.npc.NPCEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.google.gson.JsonElement
import dev.matthiesen.cobble_paste.common.CobblePasteCommon
import dev.matthiesen.cobble_paste.common.api.PokePasteApiClient
import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam
import dev.matthiesen.cobble_paste.common.util.PartyCache
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer

/**
 * NPC Party provider that fetches a team from PokePaste and provides it to an NPC.
 * * This is written in Kotlin because the Cobblemon API is written in Kotlin, and it is easier to work with for some things.
 */
class PokePastePartyProvider : NPCPartyProvider {
    companion object {
        val TYPE: ResourceLocation = CobblePasteCommon.modResource("pokepaste")
        val CACHE: PartyCache = PartyCache()

        fun register() {
            NPCPartyProvider.types.putIfAbsent(TYPE, ::PokePastePartyProvider)
            CobblePasteCommon.INSTANCE.createInfoLog("Registered NPC PokePastePartyProvider")
        }
    }

    override val type = TYPE
    override var isStatic = true

    var pokePasteId = ""

    override fun loadFromJSON(json: JsonElement) {
        val jsonObject = json.asJsonObject

        if (jsonObject.has("isStatic")) {
            isStatic = jsonObject.get("isStatic").asBoolean
        }
        if (jsonObject.has("pokePasteId")) {
            pokePasteId = jsonObject.get("pokePasteId").asString
        }
    }

    override fun provide(
        npc: NPCEntity,
        level: Int,
        players: List<ServerPlayer>
    ): NPCPartyStore {
        val party = this.loadTeam(level)
        if (party.isEmpty()) {
            val emptyParty = NPCPartyStore(npc)
            emptyParty.initialize()
            return emptyParty
        }

        val partyStore = NPCPartyStore(npc)
        var count = 0

        party.forEach {
            if (count >= 6) return@forEach
            partyStore.add(it)
            count++
        }
        partyStore.initialize()
        return partyStore
    }

    fun loadTeam(level: Int): List<Pokemon> {
        val cacheKey = "${pokePasteId}_$level"

        if (this.isStatic) {
            val cachedParty = CACHE.get(cacheKey)
            if (cachedParty != null) {
                return cachedParty
            }
        }

        val rawPaste = PokePasteApiClient.fetchRawPaste(pokePasteId).join()
        val showdownTeam = ShowdownTeam.fromPokePaste(rawPaste, PokePasteApiClient.extractPasteId(pokePasteId))
        val party = ArrayList<Pokemon>()

        showdownTeam.team.forEach {
            if (party.size >= 6) return@forEach
            val pokemon = it.toPokemon()
            if (it.level().isEmpty) {
                pokemon.level = level
            }
            party.add(pokemon)
        }

        val loadedParty = party.toList()
        if (this.isStatic) {
            CACHE.put(cacheKey, loadedParty)
        }

        return loadedParty
    }
}
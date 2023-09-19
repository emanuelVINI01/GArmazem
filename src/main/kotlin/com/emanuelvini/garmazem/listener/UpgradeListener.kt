package com.emanuelvini.garmazem.listener

import com.emanuelvini.garmazem.configuration.language.LanguageValue
import com.emanuelvini.garmazem.dao.repository.ChestRepository
import de.tr7zw.changeme.nbtapi.NBTItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class UpgradeListener (
    private val chestRepository: ChestRepository
) : Listener {

    private fun doUpgrade(item : ItemStack, player : Player) : Boolean {
        if (item.type != Material.AIR) {
            val nbt = NBTItem(item)
            if (nbt.hasTag("add-pages")) {
                val pages = nbt.getInteger("add-pages")
                if (pages > 0) {
                    if (item.amount == 1) {
                        player.inventory.setItemInMainHand(null)
                    } else {
                        item.amount = item.amount - 1
                        player.inventory.setItemInMainHand(item)
                    }
                    player.sendMessage(
                        LanguageValue.get(
                            LanguageValue::successfullyUsed
                        )!!.replace("{pages}", pages.toString())
                    )
                    for (ignored in 1..pages) {
                        chestRepository.insert(player.name)
                    }

                    return true
                }
            }
        }
        return false
    }
    @EventHandler
    fun onInteract(event : PlayerInteractEvent) {
        event.isCancelled = doUpgrade(event.player.inventory.itemInMainHand, event.player)
    }

    @EventHandler
    fun onPlace(event : BlockPlaceEvent) {
        event.isCancelled = doUpgrade(event.player.inventory.itemInMainHand, event.player)
    }


}
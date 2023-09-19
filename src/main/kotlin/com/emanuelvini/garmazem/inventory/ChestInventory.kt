package com.emanuelvini.garmazem.inventory

import com.emanuelvini.garmazem.GArmazem
import com.emanuelvini.garmazem.configuration.ConfigurationValue
import com.emanuelvini.garmazem.configuration.inventory.icon.BackSlotIcon
import com.emanuelvini.garmazem.configuration.inventory.icon.NextSlotIcon
import com.emanuelvini.garmazem.configuration.language.LanguageValue
import com.emanuelvini.garmazem.dao.model.Chest
import com.emanuelvini.garmazem.dao.repository.ChestDataRepository
import com.emanuelvini.garmazem.dao.repository.ChestRepository
import com.emanuelvini.garmazem.util.ItemUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class ChestInventory(
    private val player: Player,
    private val plugin: GArmazem,
    private val chestRepository: ChestRepository,
    private val chestDataRepository: ChestDataRepository
) {

    private var page = 0

    private var titleIndex = 0

    private var closed = false


    companion object {

        fun open(player: Player, plugin: GArmazem) {
            val inventory = ChestInventory(
                player,
                plugin,
                plugin.chestRepository!!,
                plugin.chestDataRepository!!
            )
            inventory.open(player)
        }

    }

    fun open(player: Player) {
        val orgTitles = ConfigurationValue.get(
            ConfigurationValue::titles
        )!!
        val title = orgTitles[titleIndex].split(":")[1]
        val inventory = Bukkit.createInventory(
            null, ConfigurationValue.get(
                ConfigurationValue::chestSize
            )!!, title
        )
        val chests = chestRepository.select(player.name)

        val chest: Chest = if (chests.isEmpty()) {
            chestRepository.insert(player.name)
        } else {
            chests[page]
        }

        if (titleIndex == 0) {
            orgTitles.forEach {
                val props = it.split(":")
                val time = props[0].toInt()
                if (time == 0) return@forEach
                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    if (!closed) {
                        titleIndex += 1
                        player.closeInventory()
                        open(player)
                        closed = false
                    }
                }, time * 20L)
            }

        }
        chest.data.items.forEach {
            inventory.setItem(
                it.key,
                it.value
            )
        }
        inventory.setItem(
            NextSlotIcon.get(NextSlotIcon::slot)!!, NextSlotIcon.get(
                NextSlotIcon::asItem
            )
        )
        inventory.setItem(
            BackSlotIcon.get(BackSlotIcon::slot)!!, BackSlotIcon.get(
                BackSlotIcon::asItem
            )
        )
        val listener = object : Listener {

            @EventHandler
            fun onClose(event: InventoryCloseEvent) {
                if (player.name == event.player.name && inventory.size == event.inventory.size) {
                    HandlerList.unregisterAll(this)
                    event.inventory.setItem(NextSlotIcon.get(NextSlotIcon::slot)!!, ItemStack(Material.AIR))
                    event.inventory.setItem(BackSlotIcon.get(BackSlotIcon::slot)!!, ItemStack(Material.AIR))

                    chest.data.items = ItemUtil.asHashMap(event.inventory)
                    closed = true
                }
            }

            @EventHandler
            fun onClick(event: InventoryClickEvent) {
                if (player.name == event.whoClicked.name && inventory.size == (event.clickedInventory?.size ?: 0)) {
                    if (event.slot == NextSlotIcon.get(NextSlotIcon::slot)) {
                        event.isCancelled = true
                        if (page + 1 >= chests.size) {
                            player.sendMessage(
                                LanguageValue.get(
                                    LanguageValue::lastPage
                                )!!
                            )
                            closed = true
                            player.closeInventory()
                            player.sendTitle(
                                LanguageValue.get(
                                    LanguageValue::lastPageTitle
                                ),
                                LanguageValue.get(
                                    LanguageValue::lastPageSubtitle
                                ),
                                5,
                                20*4,
                                5
                            )
                            return
                        }
                        player.closeInventory()
                        page += 1
                        open(player)
                    } else if (event.slot == BackSlotIcon.get(BackSlotIcon::slot)) {
                        event.isCancelled = true
                        if (page == 0) {
                            player.sendMessage(
                                LanguageValue.get(
                                    LanguageValue::firstPage
                                )!!
                            )
                            return
                        }
                        player.closeInventory()
                        page -= 1
                        open(player)
                    }
                }
            }
        }
        Bukkit.getPluginManager().registerEvents(listener, plugin)
        player.openInventory(inventory)
    }


}
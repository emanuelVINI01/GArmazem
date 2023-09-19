package com.emanuelvini.garmazem.inventory

import com.emanuelvini.garmazem.GArmazem
import com.emanuelvini.garmazem.configuration.inventory.UpgradeInventoryValue
import com.emanuelvini.garmazem.configuration.language.LanguageValue
import de.tr7zw.changeme.nbtapi.NBTItem
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.entity.Player

class UpgradeInventory (
    private val plugin : GArmazem
) : InventoryProvider {

    companion object {

        fun open(player : Player, plugin : GArmazem) {
            SmartInventory.builder()
                .id("main_inventory")
                .provider(
                    UpgradeInventory(
                        plugin
                    )
                )
                .manager(
                    plugin.manager
                )
                .size(
                    UpgradeInventoryValue.get(
                        UpgradeInventoryValue::size
                    )!! / 9
                    , 9)
                .title(
                    UpgradeInventoryValue.get(
                        UpgradeInventoryValue::title
                    )
                )
                .build()
                .open(player)
        }

    }



    override fun init(player: Player, contents: InventoryContents) {

        plugin.upgrades.forEach {
            contents.set(
                it.slot / 9,
                it.slot % 9,
                ClickableItem.of(
                    it.inventoryIcon,
                ) { _ ->
                    val hasEnough = true
                    if (!hasEnough) {
                        player.sendMessage(
                            LanguageValue.get(
                                LanguageValue::notHaveEnough
                            )!!
                        )
                    } else {
                        val item = it.playerIcon.clone()
                        val nbt = NBTItem(item)
                        nbt.setInteger("add-pages", it.add)
                        player.inventory.addItem(nbt.item)
                        player.sendMessage(
                            LanguageValue.get(
                                LanguageValue::successfullyBought
                            )!!
                        )
                    }
                }
            )
        }


    }

    override fun update(player: Player?, contents: InventoryContents?) {

    }
}
package com.emanuelvini.garmazem.inventory

import com.emanuelvini.garmazem.GArmazem
import com.emanuelvini.garmazem.configuration.inventory.MainInventoryValue
import com.emanuelvini.garmazem.configuration.inventory.icon.StorageOpenIcon
import com.emanuelvini.garmazem.configuration.inventory.icon.UpgradeOpenIcon
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.entity.Player
import kotlin.math.roundToInt


class MainInventory (
    private val plugin : GArmazem
) : InventoryProvider {

    companion object {

        fun open(player : Player, plugin : GArmazem) {
            SmartInventory.builder()
                .id("main_inventory")
                .provider(
                    MainInventory(
                        plugin
                    )
                )
                .manager(
                    plugin.manager
                )
                .size(
                    MainInventoryValue.get(
                        MainInventoryValue::size
                    )!! / 9,
                    9,
                )
                .title(
                    MainInventoryValue.get(
                        MainInventoryValue::title
                    )!!
                )
                .build()
                .open(player)
        }

    }



    override fun init(player: Player, contents: InventoryContents) {

        contents.set(
            (StorageOpenIcon.get(
                StorageOpenIcon::slot
            )!! / 9).toDouble().roundToInt(),
            StorageOpenIcon.get(
                StorageOpenIcon::slot
            )!! % 9,
            ClickableItem.of(
                StorageOpenIcon.get(
                    StorageOpenIcon::asItem
                ),
            ) {
                ChestInventory.open(player, plugin)
            }
        )
        contents.set(
            (UpgradeOpenIcon.get(
                UpgradeOpenIcon::slot
            )!! / 9).toDouble().roundToInt(),
            UpgradeOpenIcon.get(
                UpgradeOpenIcon::slot
            )!! % 9,
            ClickableItem.of(
                UpgradeOpenIcon.get(
                    UpgradeOpenIcon::asItem
                ),
            ) {
                UpgradeInventory.open(player, plugin)
            }
        )


    }

    override fun update(player: Player, contents: InventoryContents) {}


}
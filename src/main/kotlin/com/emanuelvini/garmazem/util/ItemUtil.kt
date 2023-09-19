package com.emanuelvini.garmazem.util

import de.tr7zw.changeme.nbtapi.NBT
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ItemUtil {

    companion object {

        @OptIn(ExperimentalEncodingApi::class)
        fun serialize(items : HashMap<Int, ItemStack>) : String {
            val serializedBuilder = StringBuilder()
            for (slot in items.keys) {
                serializedBuilder.append(
                    if (items[slot]!!.type == Material.AIR || items[slot]!!.amount == 0) "AIR"
                    else Base64.encode(
                         NBT.itemStackToNBT(items[slot]).toString().encodeToByteArray())
                )
                    .append("!!!")
                    .append(slot)
                serializedBuilder.append(";")
            }
            return serializedBuilder.toString()
        }


        fun asHashMap(inventory : Inventory) : HashMap<Int, ItemStack> {
            val items = HashMap<Int, ItemStack>()
            for (slot in 0..<inventory.size) {
                items[slot] = inventory.getItem(slot) ?: ItemStack(Material.AIR)

            }
            return items
        }

        @OptIn(ExperimentalEncodingApi::class)
        fun deserialize(serialized : String) : HashMap<Int, ItemStack> {
            print(serialized)
            val itemsSerialized = serialized.split(";")
            val items = HashMap<Int, ItemStack>()
            itemsSerialized.forEach {
                if (it.isNotEmpty()) {
                    val props = it.split("!!!")
                    val slot = Integer.parseInt(props[1])
                    if (props[0] == "AIR") {
                        items[slot] = ItemStack(Material.AIR)
                        return@forEach
                    }
                    val item = NBT.itemStackFromNBT(
                        NBT.parseNBT(
                            String(
                                Base64.decode(
                                    props[0].encodeToByteArray()
                                )
                            )
                        )
                    )

                    items[slot] = item
                }
            }
            return items
        }


    }

}
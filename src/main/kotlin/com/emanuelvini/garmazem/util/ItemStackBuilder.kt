package com.emanuelvini.garmazem.util

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemStackBuilder (
    private val material : Material
) {

    private val item = ItemStack(
        material
    )
    private val meta = item.itemMeta!!

    fun withName(name : String) : ItemStackBuilder {
        meta.setDisplayName(name)
        return this
    }

    fun withLore(lore : List<String>) : ItemStackBuilder {
        meta.lore = lore
        return this
    }

    fun build() : ItemStack {
        item.itemMeta = meta
        return item
    }


}
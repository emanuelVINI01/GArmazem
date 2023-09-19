package com.emanuelvini.garmazem.dao.model

import org.bukkit.inventory.ItemStack

data class Upgrade (
    val add : Int,
    val price : Double,
    val slot : Int,
    val inventoryIcon : ItemStack,
    val playerIcon : ItemStack
)
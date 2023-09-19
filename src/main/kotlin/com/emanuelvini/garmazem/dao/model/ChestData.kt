package com.emanuelvini.garmazem.dao.model

import org.bukkit.inventory.ItemStack

data class ChestData (
    var items : HashMap<Int, ItemStack>
)
package com.emanuelvini.garmazem.command

import com.emanuelvini.garmazem.GArmazem
import com.emanuelvini.garmazem.inventory.MainInventory
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import org.bukkit.entity.Player

class StorageCommand (
    private val plugin : GArmazem
) {

    @Command(
        name = "armazem",
        permission = "hpwh.use"
    )

    fun onStorage(context : Context<Player>) {
        MainInventory.open(
            context.sender,
            plugin
        )
    }

}
package com.emanuelvini.garmazem.configuration.inventory.icon

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.function.Function

@TranslateColors
@ConfigFile("inventories/main.yml")
@ConfigSection("storage_open_icon")
class StorageOpenIcon : ConfigurationInjectable {


    fun asItem() : ItemStack {
        val item = ItemStack(
            Material.matchMaterial(item!!)!!
        )
        val meta = item.itemMeta!!
        meta.setDisplayName(name)
        meta.lore = lore
        item.itemMeta = meta
        return item
    }

    @ConfigField("slot")
    val slot : Int? = null

    @ConfigField("item")
    val item : String? = null

    @ConfigField("name")
    val name : String? = null

    @ConfigField("lore")
    val lore: List<String>? = null
    companion object {

        val instance : StorageOpenIcon = StorageOpenIcon()

        fun <T> get(
            f : Function<StorageOpenIcon, T>
        ) : T {
            return f.apply(instance)
        }
    }


}
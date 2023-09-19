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
@ConfigFile("configuration.yml")
@ConfigSection("back_icon")
class BackSlotIcon : ConfigurationInjectable {

    @ConfigField("slot")
    val slot : Int? = null

    @ConfigField("item")
    val item : String? = null

    @ConfigField("name")
    val name : String? = null

    @ConfigField("lore")
    val lore: List<String>? = null

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

    companion object {

        val instance : BackSlotIcon = BackSlotIcon()

        fun <T> get(
            f : Function<BackSlotIcon, T>
        ) : T {
            return f.apply(instance)
        }
    }

}
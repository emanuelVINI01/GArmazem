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
@ConfigSection("next_icon")
class NextSlotIcon : ConfigurationInjectable {

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

        val instance : NextSlotIcon = NextSlotIcon()

        fun <T> get(
            f : Function<NextSlotIcon, T>
        ) : T {
            return f.apply(instance)
        }
    }

}
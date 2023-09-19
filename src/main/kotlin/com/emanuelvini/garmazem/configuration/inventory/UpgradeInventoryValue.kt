package com.emanuelvini.garmazem.configuration.inventory

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable
import java.util.function.Function

@TranslateColors
@ConfigFile("inventories/upgrade.yml")
class UpgradeInventoryValue : ConfigurationInjectable {

    @ConfigField("title")
    val title : String? = null


    @ConfigField("size")
    val size : Int? = null

    companion object {

        val instance : UpgradeInventoryValue = UpgradeInventoryValue()

        fun <T> get(
            f : Function<UpgradeInventoryValue, T>
        ) : T {
            return f.apply(instance)
        }
    }

}
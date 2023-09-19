package com.emanuelvini.garmazem.configuration.inventory

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable
import java.util.function.Function
@TranslateColors
@ConfigFile("inventories/main.yml")
class MainInventoryValue : ConfigurationInjectable {


    @ConfigField("title")
    val title : String? = null


    @ConfigField("size")
    val size : Int? = null


    companion object {

        val instance : MainInventoryValue = MainInventoryValue()

        fun <T> get(
            f : Function<MainInventoryValue, T>
        ) : T {
            return f.apply(instance)
        }
    }
}
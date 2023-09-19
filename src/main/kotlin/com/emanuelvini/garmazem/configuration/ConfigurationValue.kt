package com.emanuelvini.garmazem.configuration

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable
import java.util.function.Function

@TranslateColors
@ConfigFile("configuration.yml")
class ConfigurationValue : ConfigurationInjectable {

    @ConfigField("titles")
    val titles : List<String>? = null

    @ConfigField("chest_size")
    val chestSize : Int? = null

    companion object {

        val instance = ConfigurationValue()

        fun <T> get(
            function : Function<ConfigurationValue, T>
        ) : T {
            return function.apply(instance)
        }

    }


}
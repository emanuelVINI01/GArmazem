package com.emanuelvini.garmazem.configuration.language

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable
import java.util.function.Function

@TranslateColors
@ConfigFile("language.yml")
class LanguageValue : ConfigurationInjectable {

    companion object {

        val instance : LanguageValue = LanguageValue()

        fun <T> get(
            f : Function<LanguageValue, T>
        ) : T {
            return f.apply(instance)
        }
    }

    @ConfigField("not_have_permission")
    val notHavePermission : String? = null

    @ConfigField("last_page")
    val lastPage : String? = null

    @ConfigField("first_page")
    val firstPage : String? = null

    @ConfigField("successfully_used")
    val successfullyUsed : String? = null

    @ConfigField("not_have_enough")
    val notHaveEnough : String? = null

    @ConfigField("last_page_title")
    val lastPageTitle : String? = null

    @ConfigField("last_page_subtitle")
    val lastPageSubtitle : String? = null


    @ConfigField("successfully_bought")
    val successfullyBought : String? = null

    
}
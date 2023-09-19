package com.emanuelvini.garmazem.configuration.database

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable
import java.util.function.Function

@ConfigFile("database.yml")
class DatabaseValue : ConfigurationInjectable {

    @ConfigField("type")
    val type : String? = null

    @ConfigField("host")
    val host : String? = null

    @ConfigField("username")
    val username : String? = null

    @ConfigField("password")
    val password : String? = null

    @ConfigField("database")
    val database : String? = null

    companion object {

        val instance : DatabaseValue = DatabaseValue()

        fun <T> get(
            f : Function<DatabaseValue, T>
        ) : T {
            return f.apply(instance)
        }
    }

}
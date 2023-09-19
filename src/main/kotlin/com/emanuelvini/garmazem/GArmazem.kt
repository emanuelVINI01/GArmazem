package com.emanuelvini.garmazem

import com.emanuelvini.garmazem.command.StorageCommand
import com.emanuelvini.garmazem.configuration.ConfigurationValue
import com.emanuelvini.garmazem.configuration.database.DatabaseValue
import com.emanuelvini.garmazem.configuration.inventory.MainInventoryValue
import com.emanuelvini.garmazem.configuration.inventory.UpgradeInventoryValue
import com.emanuelvini.garmazem.configuration.inventory.icon.BackSlotIcon
import com.emanuelvini.garmazem.configuration.inventory.icon.NextSlotIcon
import com.emanuelvini.garmazem.configuration.inventory.icon.StorageOpenIcon
import com.emanuelvini.garmazem.configuration.inventory.icon.UpgradeOpenIcon
import com.emanuelvini.garmazem.configuration.language.LanguageValue
import com.emanuelvini.garmazem.dao.model.Upgrade
import com.emanuelvini.garmazem.dao.repository.ChestDataRepository
import com.emanuelvini.garmazem.dao.repository.ChestRepository
import com.emanuelvini.garmazem.listener.UpgradeListener
import com.emanuelvini.garmazem.util.ItemStackBuilder
import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType
import com.henryfabio.sqlprovider.executor.SQLExecutor
import fr.minuskube.inv.InventoryManager
import me.saiintbrisson.bukkit.command.BukkitFrame
import me.saiintbrisson.minecraft.command.message.MessageType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class GArmazem : JavaPlugin() {

    val manager = InventoryManager(this)

    var chestDataRepository : ChestDataRepository? = null

    var chestRepository : ChestRepository? = null

    val upgrades : MutableList<Upgrade> = mutableListOf()

    private fun log(msg : String) {
        Bukkit.getConsoleSender().sendMessage("${ChatColor.GOLD}[HardPixel-Warehouse] ${ChatColor.GRAY}$msg")
    }

    private fun connectToDatabase() : SQLExecutor {
        if (DatabaseValue.get(
            DatabaseValue::type
        )!!.lowercase() == "sqlite") {
            return SQLExecutor(
                SQLiteDatabaseType(
                    File(
                        dataFolder,
                        "data.db"
                    )
                ).connect()
            )
        } else {
            return SQLExecutor(
                MySQLDatabaseType(
                    DatabaseValue.get(DatabaseValue::host),
                    DatabaseValue.get(DatabaseValue::username),
                    DatabaseValue.get(DatabaseValue::password),
                    DatabaseValue.get(DatabaseValue::database)
                ).connect()
            )
        }
    }

    private fun loadUpgrades() {
        val upgradeFile = File(dataFolder, "upgrades.yml")
        if (!upgradeFile.exists()) saveResource("upgrades.yml", false)
        val config = YamlConfiguration.loadConfiguration(upgradeFile)
        config.getConfigurationSection("upgrades")!!.getKeys(false).forEach {
            val section = config.getConfigurationSection("upgrades.${it}")!!

            upgrades.add(
                Upgrade(
                    section.getInt("add"),
                    section.getDouble("price"),
                    section.getInt("slot"),
                    ItemStackBuilder(
                        Material.matchMaterial(
                            section.getString("inventory_icon.item")!!
                        )!!
                    ).withName(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            section.getString("inventory_icon.name")!!
                        )
                    )
                        .withLore(
                            section.getStringList("inventory_icon.lore")
                                .map { l ->
                                    ChatColor.translateAlternateColorCodes(
                                        '&',
                                        l
                                    )
                                }
                        )
                        .build(),
                    ItemStackBuilder(
                        Material.matchMaterial(
                            section.getString("player_icon.item")!!
                        )!!
                    ).withName(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            section.getString("player_icon.name")!!
                        )
                    )
                        .withLore(
                            section.getStringList("player_icon.lore")
                                .map { l ->
                                    ChatColor.translateAlternateColorCodes(
                                        '&',
                                        l
                                    )
                                }
                        )
                        .build()
                )
            )

        }
    }

    override fun onEnable() {
        log("Iniciando plugin...")

        val configurationInjector = BukkitConfigurationInjector(this)

        configurationInjector.saveDefaultConfiguration(this,
            "configuration.yml",
            "database.yml",
            "language.yml",
            "inventories/upgrade.yml",
            "inventories/main.yml"
        )
        configurationInjector.injectConfiguration(
            MainInventoryValue.instance,
            UpgradeInventoryValue.instance,
            StorageOpenIcon.instance,
            UpgradeOpenIcon.instance,
            LanguageValue.instance,
            DatabaseValue.instance,
            ConfigurationValue.instance,
            NextSlotIcon.instance,
            BackSlotIcon.instance
        )


        manager.init()

        try {
            val executor : SQLExecutor = connectToDatabase()

            chestDataRepository = ChestDataRepository(executor)
            chestDataRepository!!.setup()

            chestRepository = ChestRepository(executor, chestDataRepository!!)
            chestRepository!!.setup()

            val bukkitFrame = BukkitFrame(this)

            bukkitFrame.messageHolder.setMessage(MessageType.NO_PERMISSION, LanguageValue.get(
                LanguageValue::notHavePermission
            ))

            bukkitFrame.registerCommands(
                StorageCommand(
                    this
                )
            )

            loadUpgrades()

            Bukkit.getServer().pluginManager.registerEvents(
                UpgradeListener(
                    chestRepository!!
                ),
                this
            )

            log("${ChatColor.GREEN}Plugin iniciado com sucesso!")
        } catch (ex : Exception) {
            log("${ChatColor.DARK_RED}Falha ao iniciar o plugin! Verifique a database.")
            ex.printStackTrace()
        }



    }

    override fun onDisable() {
        chestDataRepository!!.saveAll()
        log("${ChatColor.RED}Plugin desligado.")
    }
}
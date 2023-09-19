package com.emanuelvini.garmazem.dao.repository

import com.emanuelvini.garmazem.configuration.ConfigurationValue
import com.emanuelvini.garmazem.dao.adapter.ChestDataAdapter
import com.emanuelvini.garmazem.dao.model.ChestData
import com.emanuelvini.garmazem.util.ItemUtil
import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalListener
import com.henryfabio.sqlprovider.executor.SQLExecutor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.time.Duration

class ChestDataRepository (
    private val executor : SQLExecutor
) {

    private val cache : AsyncLoadingCache<String, ChestData> = Caffeine.newBuilder()
        .removalListener(RemovalListener<String, ChestData> { key, value, _ ->
            if (key != null && value != null) {
                save(value, key)
            }
        })
        .evictionListener(RemovalListener<String, ChestData> { key, value, _ ->
            if (key != null && value != null) {
                save(value, key)
            }
        })
        .expireAfterWrite(Duration.ofMinutes(5))
        .buildAsync{ key ->
            return@buildAsync selectData(key)
        }

    fun setup() {
        executor.updateQuery("CREATE TABLE IF NOT EXISTS chest_data (id VARCHAR(36), inventory TEXT)")
    }

    fun select(id : String) : ChestData {
        return cache.get(id).get()
    }

    private fun selectData(id : String) : ChestData {
        val result = executor.resultOneQuery("SELECT * FROM chest_data WHERE id = ?", {
            it[1] = id
        }, ChestDataAdapter::class.java)
        if (result == null) {
            val data = emptyData()
            insert(
                data, id
            )
            return data
        }
        return result
    }

    fun saveAll() {
        cache.synchronous().asMap().forEach {
            save(it.value, it.key)
        }
    }

    fun emptyData() : ChestData {
        val items = HashMap<Int, ItemStack>()

        val data = ChestData(
            items
        )
        for (slot in 0..<ConfigurationValue.get(
            ConfigurationValue::chestSize
        )!!) {
            items[slot] = ItemStack(Material.AIR)
        }
        return data
    }

    private fun insert(data : ChestData, id : String) {
        executor.updateQuery("INSERT INTO chest_data VALUES (?,?)") {
            it[1] = id
            it[2] = ItemUtil.serialize(
                data.items
            )
        }
    }

    private fun save(data : ChestData, id : String) {
        executor.updateQuery("UPDATE chest_data SET inventory = ? WHERE id = ?") {

            it[1] = ItemUtil.serialize(data.items)
            it[2] = id
        }
    }
}
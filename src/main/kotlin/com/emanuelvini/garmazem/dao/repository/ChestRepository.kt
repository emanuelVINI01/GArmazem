package com.emanuelvini.garmazem.dao.repository

import com.emanuelvini.garmazem.dao.model.Chest
import com.henryfabio.sqlprovider.executor.SQLExecutor
import java.util.*

class ChestRepository(
    private val executor: SQLExecutor,
    private val dataRepository: ChestDataRepository
) {


    fun setup() {
        executor.updateQuery("CREATE TABLE IF NOT EXISTS chests (id VARCHAR(36), owner VARCHAR(16))")
    }

    fun select(owner: String): List<Chest> {
        return executor.resultQuery("SELECT * FROM chests WHERE owner = ?", {
            it[1] = owner
        }, {
            val chests = mutableListOf<Chest>()

            while (it.next()) {
                val id : String = it.get("id")

                chests.add(
                    Chest(
                        id,
                        owner,
                        dataRepository.select(
                            id
                        )
                    )
                )
            }

            return@resultQuery chests.toList()
        })
    }

    fun insert(owner : String) : Chest {
        val chest = Chest(
            UUID.randomUUID().toString(),
            owner,
            dataRepository.emptyData()
        )
        executor.updateQuery("INSERT INTO chests VALUES(?,?)") {
            it[1] = chest.id
            it[2] = owner
        }
        return chest
    }

}
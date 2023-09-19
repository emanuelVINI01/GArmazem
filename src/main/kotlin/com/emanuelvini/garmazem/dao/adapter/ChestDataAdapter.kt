package com.emanuelvini.garmazem.dao.adapter

import com.emanuelvini.garmazem.dao.model.ChestData
import com.emanuelvini.garmazem.util.ItemUtil
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet

class ChestDataAdapter : SQLResultAdapter<ChestData> {
    override fun adaptResult(resultSet: SimpleResultSet): ChestData {
        val items = ItemUtil.deserialize(
            resultSet.get("inventory")
        )

        return ChestData(
            items
        )
    }
}
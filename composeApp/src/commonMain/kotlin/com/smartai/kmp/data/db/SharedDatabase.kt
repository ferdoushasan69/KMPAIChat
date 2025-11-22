package com.smartai.kmp.data.db

import app.cash.sqldelight.ColumnAdapter
import com.smartai.kmp.database.AppDatabase
import com.smartai.kmp.database.Message
import com.smartai.kmp.di.DatabaseDriverFactory
import com.smartai.kmp.domain.model.Role
import kotlin.io.encoding.Base64

class SharedDatabase(
    private val driverFactory: DatabaseDriverFactory
) {
    private var database: AppDatabase? = null

    val byteArrayToStringAdapter = object : ColumnAdapter<List<ByteArray>, String> {
        override fun decode(databaseValue: String): List<ByteArray> {
            if (databaseValue.isNotEmpty()) {
                val encodedList = databaseValue.split(",")
                val decodedList = ArrayList<ByteArray>()

                for (encodedByte in encodedList) {
                    val byteArray = Base64.decode(encodedByte)
                    decodedList.add(byteArray)
                }
                return decodedList
            } else {
                return emptyList()
            }
        }

        override fun encode(value: List<ByteArray>): String {
            if (value.isNotEmpty()) {
                val encodedList = ArrayList<String>()

                for (byteArray in value) {
                    val encodedByte = Base64.encode(byteArray)
                    encodedList.add(encodedByte)
                }
                return encodedList.joinToString(",")
            } else {
                return ""
            }
        }
    }

    private val roleToStringAdapter = object : ColumnAdapter<Role, String> {
        override fun decode(databaseValue: String): Role {
            return Role.valueOf(databaseValue)
        }

        override fun encode(value: Role): String {
            return value.name
        }
    }

    private suspend fun initDatabase() {
        if (database == null) {
            database = AppDatabase.invoke(
                driver = driverFactory.createDriver(),
                MessageAdapter = Message.Adapter(
                    byteArrayToStringAdapter,
                    roleToStringAdapter,
                )
            )
        }
    }

    suspend operator fun <R> invoke(block: suspend (AppDatabase) -> R): R {
        initDatabase()
        return block(database!!)
    }
}
package com.example.data.tables

import com.example.data.tables.BookTable.autoIncrement
import org.jetbrains.exposed.sql.Table

object CategoryTable : Table() {

    val categoryId = integer("categoryId")
    val category = varchar("category", 512)

    override val primaryKey: Table.PrimaryKey = PrimaryKey(categoryId)
}
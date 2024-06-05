package com.example.data.tables

import org.jetbrains.exposed.sql.Table

object LocationTable: Table(){

    val locationId = integer("locationId")
    val location = varchar("location", 512)

    override val primaryKey: Table.PrimaryKey = PrimaryKey(locationId)
}
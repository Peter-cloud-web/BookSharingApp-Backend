package com.example.data.tables

import com.example.data.model.Category
import com.example.data.tables.UserTable.autoIncrement
import com.example.data.tables.UserTable.bool
import com.example.data.tables.UserTable.integer
import com.example.data.tables.UserTable.long
import com.example.data.tables.UserTable.varchar
import com.example.data.tables.WaitingListTable.references
import com.example.data.tables.WaitingListTable.uniqueIndex
import org.jetbrains.exposed.sql.Table

object BookTable : Table() {
    val id = integer("bookId").autoIncrement()
    val userEmail = varchar("userEmail",512).references(UserTable.userEmail)
    val categoryId = integer("categoryId").references(CategoryTable.categoryId)
    val locationId = integer("locationId").references(LocationTable.locationId)
    val bookImage = binary("bookImage")
    val location = varchar("location",512)
    val category = varchar("category",512)
    val title = varchar("bookTitle", 512)
    val author = varchar("bookAuthor", 512)
    val page = integer("bookPages")
    val summary = varchar("bookSummary", 512)
    val isAvailable = bool("isAvailable")
    val createdAt = long("createAt")

    override val primaryKey: Table.PrimaryKey = PrimaryKey(id)

}
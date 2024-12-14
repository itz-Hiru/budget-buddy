package com.hirumitha.budget.buddy.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hirumitha.budget.buddy.models.TransactionModel

class TransactionDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "Transaction.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "transactions"
        const val COLUMN_ID = "id"
        const val COLUMN_TYPE = "type"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TYPE TEXT," +
                "$COLUMN_CATEGORY TEXT," +
                "$COLUMN_AMOUNT REAL," +
                "$COLUMN_DATE TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTransaction(transactionModel: TransactionModel): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TYPE, transactionModel.type)
            put(COLUMN_CATEGORY, transactionModel.category)
            put(COLUMN_AMOUNT, transactionModel.amount)
            put(COLUMN_DATE, transactionModel.date)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun getAllTransactions(): List<TransactionModel> {
        val transactionModels = mutableListOf<TransactionModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val transactionModel = TransactionModel(
                    id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                    type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)),
                    category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                    amount = cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT)),
                    date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                )
                transactionModels.add(transactionModel)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return transactionModels
    }

    fun getTotalIncome(): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(amount) FROM $TABLE_NAME WHERE type = 'Income'", null)
        var totalIncome = 0.0
        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(0)
        }
        cursor.close()
        return totalIncome
    }

    fun getTotalExpense(): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(amount) FROM $TABLE_NAME WHERE type = 'Expense'", null)
        var totalExpense = 0.0
        if (cursor.moveToFirst()) {
            totalExpense = cursor.getDouble(0)
        }
        cursor.close()
        return totalExpense
    }


    fun updateTransaction(id: Long, type: String, category: String, amount: Float, date: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TYPE, type)
            put(COLUMN_CATEGORY, category)
            put(COLUMN_AMOUNT, amount)
            put(COLUMN_DATE, date)
        }

        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))

        db.close()
        return rowsAffected > 0
    }

    fun deleteTransaction(transactionId: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(transactionId.toString()))
        db.close()
    }
}
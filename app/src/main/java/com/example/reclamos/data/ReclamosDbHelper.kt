package com.example.reclamos.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ReclamosDbHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $T_RECLAMOS (
                $C_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $C_NOMBRE TEXT NOT NULL,
                $C_DESCRIPCION TEXT NOT NULL,
                $C_CATEGORIA TEXT NOT NULL,
                $C_EMAIL TEXT NOT NULL,
                $C_TELEFONO TEXT,
                $C_NRO_COMPRA TEXT,
                $C_SUCURSAL TEXT,
                $C_FOTO_URI TEXT,         -- NUEVO
                $C_LATITUD REAL,          -- NUEVO
                $C_LONGITUD REAL          -- NUEVO
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $T_RECLAMOS ADD COLUMN $C_FOTO_URI TEXT")
            db.execSQL("ALTER TABLE $T_RECLAMOS ADD COLUMN $C_LATITUD REAL")
            db.execSQL("ALTER TABLE $T_RECLAMOS ADD COLUMN $C_LONGITUD REAL")
        }
    }

    companion object {
        const val DB_NAME = "reclamos.db"
        const val DB_VERSION = 2

        const val T_RECLAMOS = "reclamos"
        const val C_ID = "id"
        const val C_NOMBRE = "nombre"
        const val C_DESCRIPCION = "descripcion"
        const val C_CATEGORIA = "categoria"
        const val C_EMAIL = "email"
        const val C_TELEFONO = "telefono"
        const val C_NRO_COMPRA = "nro_compra"
        const val C_SUCURSAL = "sucursal"

        // Nuevos campos
        const val C_FOTO_URI = "foto_uri"
        const val C_LATITUD = "latitud"
        const val C_LONGITUD = "longitud"
    }
}

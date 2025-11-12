package com.example.reclamos.data

import android.content.ContentValues
import android.content.Context
import com.example.reclamos.model.Reclamo
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_CATEGORIA
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_DESCRIPCION
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_EMAIL
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_FOTO_URI
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_ID
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_LATITUD
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_LONGITUD
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_NOMBRE
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_NRO_COMPRA
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_SUCURSAL
import com.example.reclamos.data.ReclamosDbHelper.Companion.C_TELEFONO
import com.example.reclamos.data.ReclamosDbHelper.Companion.T_RECLAMOS

class ReclamoRepository(ctx: Context) {

    private val dbHelper = ReclamosDbHelper(ctx)

    suspend fun listar(): List<Reclamo> {
        val db = dbHelper.readableDatabase
        val out = mutableListOf<Reclamo>()
        db.query(
            T_RECLAMOS,
            arrayOf(
                C_ID, C_NOMBRE, C_DESCRIPCION, C_CATEGORIA, C_EMAIL,
                C_TELEFONO, C_NRO_COMPRA, C_SUCURSAL,
                C_FOTO_URI, C_LATITUD, C_LONGITUD
            ),
            null, null, null, null,
            "$C_ID DESC"
        ).use { c ->
            while (c.moveToNext()) {
                out += Reclamo(
                    id = c.getLong(c.getColumnIndexOrThrow(C_ID)),
                    nombre = c.getString(c.getColumnIndexOrThrow(C_NOMBRE)),
                    descripcion = c.getString(c.getColumnIndexOrThrow(C_DESCRIPCION)),
                    categoria = c.getString(c.getColumnIndexOrThrow(C_CATEGORIA)),
                    email = c.getString(c.getColumnIndexOrThrow(C_EMAIL)),
                    telefono = c.getStringOrNull(C_TELEFONO),
                    nroCompra = c.getStringOrNull(C_NRO_COMPRA),
                    sucursal = c.getStringOrNull(C_SUCURSAL),
                    fotoUri = c.getStringOrNull(C_FOTO_URI),
                    latitud = c.getDoubleOrNull(C_LATITUD),
                    longitud = c.getDoubleOrNull(C_LONGITUD)
                )
            }
        }
        return out
    }

    suspend fun insertar(r: Reclamo): Long {
        val db = dbHelper.writableDatabase
        val v = ContentValues().apply {
            put(C_NOMBRE, r.nombre)
            put(C_DESCRIPCION, r.descripcion)
            put(C_CATEGORIA, r.categoria)
            put(C_EMAIL, r.email)
            put(C_TELEFONO, r.telefono)
            put(C_NRO_COMPRA, r.nroCompra)
            put(C_SUCURSAL, r.sucursal)
            put(C_FOTO_URI, r.fotoUri)        // NUEVO
            put(C_LATITUD, r.latitud)         // NUEVO
            put(C_LONGITUD, r.longitud)       // NUEVO
        }
        return db.insert(T_RECLAMOS, null, v)
    }
}

/* ------- helpers de Cursor ------- */
private fun android.database.Cursor.getStringOrNull(col: String): String? =
    if (isNull(getColumnIndexOrThrow(col))) null else getString(getColumnIndexOrThrow(col))

private fun android.database.Cursor.getDoubleOrNull(col: String): Double? =
    if (isNull(getColumnIndexOrThrow(col))) null else getDouble(getColumnIndexOrThrow(col))

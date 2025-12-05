package com.example.reclamos.data.network

import com.example.reclamos.model.Reclamo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response
import okhttp3.ResponseBody

class ReclamoRepositoryTest {

    private val api = mockk<ApiService>()
    private val repo = ReclamoRepository(api)

    @Test
    fun `obtenerReclamos retorna lista`() = runBlocking {
        val mockData = listOf(
            Reclamo(
                id = 1,
                nombre = "Prueba",
                descripcion = "Descripcion test",
                categoria = "General",
                email = "test@test.com",
                latitud = 0.0,
                longitud = 0.0
            )
        )

        coEvery { api.obtenerReclamos() } returns Response.success(mockData)

        val result = repo.obtenerReclamos()

        assertNotNull(result)
        assertEquals(1, result!!.size)
        assertEquals("Prueba", result[0].nombre)
    }

    @Test
    fun `crear reclamo retorna true si es exitoso`() = runBlocking {
        val reclamo = Reclamo(
            id = 0,
            nombre = "Nuevo",
            descripcion = "Test crear",
            categoria = "General",
            email = "test@test.com",
            latitud = 0.0,
            longitud = 0.0
        )

        coEvery { api.crearReclamo(any()) } returns Response.success(mapOf("id" to 1))

        val result = repo.crearReclamo(reclamo)

        assertTrue(result)
    }

    @Test
    fun `editar reclamo retorna false en caso de error`() = runBlocking {
        val reclamo = Reclamo(
            id = 1,
            nombre = "Editado",
            descripcion = "Test editar",
            categoria = "General",
            email = "test@test.com",
            latitud = 0.0,
            longitud = 0.0
        )

        coEvery {
            api.actualizarReclamo(any(), any())
        } returns Response.error(500, ResponseBody.create(null, ""))

        val result = repo.editarReclamo(1, reclamo)

        assertFalse(result)
    }
}

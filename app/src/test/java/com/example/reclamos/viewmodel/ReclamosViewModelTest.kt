package com.example.reclamos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.reclamos.data.network.ReclamoRepository
import com.example.reclamos.model.Reclamo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReclamosViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo = mockk<ReclamoRepository>()
    private lateinit var viewModel: ReclamosViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ReclamosViewModel(repo)
    }

    @Test
    fun `cargarReclamos llena lista`() = runTest {
        val mockList = listOf(
            Reclamo(
                id = 1,
                nombre = "Test Reclamo",
                descripcion = "Descripcion",
                categoria = "General",
                email = "",
                latitud = 0.0,
                longitud = 0.0
            )
        )

        coEvery { repo.obtenerReclamos() } returns mockList

        viewModel.cargarReclamos()
        advanceUntilIdle()

        assertEquals(1, viewModel.listaReclamos.value?.size)
        assertEquals("Test Reclamo", viewModel.listaReclamos.value?.get(0)?.nombre)
    }
}

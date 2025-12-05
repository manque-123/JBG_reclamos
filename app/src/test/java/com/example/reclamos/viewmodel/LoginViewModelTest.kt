package com.example.reclamos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.reclamos.data.network.ApiService
import com.example.reclamos.data.network.TokenManager
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val api = mockk<ApiService>()
    private val context = mockk<android.content.Context>(relaxed = true)
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = LoginViewModel(api)
    }

    @Test
    fun `login exitoso guarda token`() = runTest {
        val mockResponse = Response.success(mapOf("token" to "abc123"))

        coEvery { api.login(any()) } returns mockResponse
        mockkObject(TokenManager)
        every { TokenManager.saveToken(any(), any()) } just Runs

        viewModel.login("correo@test.com", "1234", context)
        advanceUntilIdle()

        assertNull(viewModel.mensajeError.value)
        coVerify { TokenManager.saveToken(context, "abc123") }
    }

    @Test
    fun `login incorrecto muestra mensaje error`() = runTest {
        val errorBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "")
        coEvery { api.login(any()) } returns Response.error(401, errorBody)

        viewModel.login("mal@correo.com", "badpass", context)
        advanceUntilIdle()

        assertEquals("Credenciales incorrectas", viewModel.mensajeError.value)
    }

    @Test
    fun `error de conexion muestra mensaje`() = runTest {
        coEvery { api.login(any()) } throws Exception("Network error")

        viewModel.login("c", "d", context)
        advanceUntilIdle()

        assertEquals("Error de conexión", viewModel.mensajeError.value)
    }
}

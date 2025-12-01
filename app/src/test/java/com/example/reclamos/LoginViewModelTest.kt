package com.example.reclamos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.reclamos.data.network.ApiService
import com.example.reclamos.data.network.TokenManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `cuando login falla, mensaje debe contener error`() = runTest(dispatcher) {

        val api = mockk<ApiService>()
        val viewModel = LoginViewModel()

        coEvery {
            api.login(any())
        } returns retrofit2.Response.error(
            401,
            ResponseBody.create("application/json".toMediaType(), "{}")
        )

        viewModel.login(
            context = mockk(relaxed = true),
            email = "fake@mail.com",
            password = "1234",
            onSuccess = {}
        )

        advanceUntilIdle()

        assertEquals(
            "Credenciales incorrectas (401)",
            viewModel.mensaje.value
        )
    }

    @Test
    fun `cuando login es exitoso, debe guardar token`() = runTest(dispatcher) {

        val api = mockk<ApiService>()
        val context = mockk<android.content.Context>(relaxed = true)
        val viewModel = LoginViewModel()

        coEvery {
            api.login(any())
        } returns retrofit2.Response.success(
            mapOf("token" to "token_falso_123")
        )

        coEvery { TokenManager.saveToken(context, any()) } returns Unit

        var successCalled = false

        viewModel.login(
            context = context,
            email = "a@a.com",
            password = "1234",
            onSuccess = { successCalled = true }
        )

        advanceUntilIdle()

        assertEquals(true, successCalled)
    }
}

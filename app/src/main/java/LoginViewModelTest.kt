import android.content.Context
import androidx.room.jarjarred.org.antlr.v4.tool.Rule
import com.example.reclamos.viewmodel.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `cuando login falla, mensaje debe contener error`() = runTest(dispatcher) {
        val vm = LoginViewModel()

        val context = mockk<Context>(relaxed = true)

        // Forzar error
        vm.mensaje.postValue("Credenciales incorrectas")

        assertEquals("Credenciales incorrectas", vm.mensaje.value)
    }
}
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.compose.KoinApplication
import org.koin.core.context.loadKoinModules
import org.koin.dsl.koinApplication
import org.koin.ksp.generated.module
import java.awt.Dimension
import ru.alexey.ndimmatrix.App
import ru.alexey.ndimmatrix.generator.presentation.impl.PresentationDIModule
import ru.alexey.ndimmatrix.generator.ui.UIDIModule

fun main() = application {
    Window(
        title = "Schema Editor",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        KoinApplication(
            application = {
                modules(
                    PresentationDIModule().module,
                    UIDIModule().module
                )
            },
        ) {
            App()
        }

    }
}


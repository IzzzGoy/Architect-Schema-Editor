import androidx.compose.ui.window.ComposeUIViewController
import ru.alexey.ndimmatrix.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }

package device.apps.presentationexample

import android.app.Presentation
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class MainActivity : ComponentActivity() {

    companion object {
        /**
         * Creates a ComposeView with lifecycle and saved state support,
         * and returns it as a regular View for interoperability.
         *
         * @param context the context to create the ComposeView
         * @param lifecycleOwner the lifecycle owner to attach
         * @param content the Composable content to render
         * @return a View instance wrapping the Compose content
         */
        fun makeComposeView(context: Context, lifecycleOwner: LifecycleOwner, content: @Composable () -> Unit): View {
            return ComposeView(context).apply {
                this@apply.setViewTreeLifecycleOwner(lifecycleOwner)
                this@apply.setViewTreeSavedStateRegistryOwner(lifecycleOwner as SavedStateRegistryOwner)
                setContent(content)
            }
        }

        /**
         * Inflates a legacy XML layout inside a Compose-based app using AndroidView.
         */
        @Composable
        fun LegacyXmlLayout() {
            AndroidView(
                factory = { context: Context ->
                    val root = FrameLayout(context)
                    LayoutInflater.from(context).inflate(R.layout.presentation_layout_1, root, false)
                },
                update = { view ->
                    // Optionally update the view after inflation
                }
            )
        }

        // Unique IDs for each external display port
        private const val PORT_HDMI_2 = "local:1"
        private const val PORT_USB_C1 = "local:2"
        private const val PORT_USB_C2 = "local:3"
    }

    /** Holds the list of currently shown Presentation instances. */
    private val presentations = mutableListOf<Presentation>()

    /** Listens to external display connection changes. */
    private val displayListener = object : DisplayManager.DisplayListener {

        override fun onDisplayAdded(displayId: Int) {
            Log.d("MainActivity", "Display added: $displayId")
            initPresentations()
        }

        override fun onDisplayRemoved(displayId: Int) {
            Log.d("MainActivity", "Display removed: $displayId")
            initPresentations()
        }

        override fun onDisplayChanged(displayId: Int) {
            Log.d("MainActivity", "Display changed: $displayId")
        }
    }

    /** Reference to the system DisplayManager. */
    private lateinit var displayManager: DisplayManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Display legacy XML layout via Compose interop (optional)
        setContent {
            LegacyXmlLayout()
        }

        // Create and render a ComposeView as the activity's root view
        val view = makeComposeView(applicationContext, lifecycleOwner = this@MainActivity) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFFFB3B3)),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = "TEST")
            }
        }
        setContentView(view)

        // Always set main screen to Compose UI
        setContent { MainComposeScreen() }

        initPresentations()
    }

    override fun onResume() {
        super.onResume()
        // Register the display listener to detect added/removed displays
        displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        displayManager.registerDisplayListener(displayListener, null)
    }

    override fun onPause() {
        super.onPause()
        // Unregister the display listener to avoid leaks
        displayManager.unregisterDisplayListener(displayListener)
    }

    /**
     * Scans available external displays and creates Presentations
     * according to their unique ID. Each Presentation is shown on a different screen.
     *
     * This method ensures that only non-main displays (ID != 0) are used.
     */
    private fun initPresentations() {
        val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION)

        // Dismiss all current presentations before refreshing
        presentations.forEach { it.dismiss() }
        presentations.clear()

        for (display in displays) {
            // Skip main display (already in use by MainActivity)
            if (display.displayId == 0)
                continue

            // Create appropriate Presentation based on uniqueId
            val presentation = when (display.uniqueId) {
                PORT_HDMI_2 -> PresentationLayout1(context = applicationContext, display = display)
                PORT_USB_C1 -> PresentationLayout2(context = applicationContext, display = display)
                PORT_USB_C2 -> PresentationLayout3(context = applicationContext, display = display)
                else -> throw Exception("Not defined unique id = ${display.uniqueId}")
            }

            presentation.show()
            presentations.add(presentation)
        }
    }
}

/**
 * A simple Composable screen for the main activity.
 */
@Composable
fun MainComposeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(color = 0xFFFFCCCC)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "COMPOSE SCREEN",
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
        )
    }
}


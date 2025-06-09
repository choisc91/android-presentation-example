package device.apps.presentationexample

import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.view.Display

class PresentationLayout2(
    context: Context,
    display: Display,
) : Presentation(context, display) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.presentation_layout_2)
    }
}

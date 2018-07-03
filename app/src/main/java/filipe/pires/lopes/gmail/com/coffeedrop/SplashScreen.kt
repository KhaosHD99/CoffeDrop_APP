package filipe.pires.lopes.gmail.com.coffeedrop

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // whe do not need to give this view a Layout, We will simply aply a theme, its quicker and there is no random white screen uppp app load
        //setContentView(R.layout.activity_splash_screen)

        launchActivity<MapsActivity> {  }
    }
}

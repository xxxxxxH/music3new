package player.shellvoice.app.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity


fun View.click(c:(View)->Unit){
    setOnClickListener {
        c(this)
    }
}
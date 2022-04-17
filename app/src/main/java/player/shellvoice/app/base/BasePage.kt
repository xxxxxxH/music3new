package player.shellvoice.app.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BasePage(id:Int):AppCompatActivity(id) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPage()
    }

    open fun initPage(){}
}
package player.shellvoice.app.widget

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_navigation.view.*
import player.shellvoice.app.R
import player.shellvoice.app.ui.dialog.AboutDialog
import player.shellvoice.app.ui.page.ThemePage
import player.shellvoice.app.utils.click
import player.shellvoice.app.utils.shareWithEmail

class INavigationView:LinearLayout {
    private val aboutDialog by lazy {
        AboutDialog(context)
    }
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context): View {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_navigation, this, true)
        ll_theme.click { context.startActivity(Intent(context, ThemePage::class.java)) }
        ll_about.click { aboutDialog.show() }
        ll_share.click { (context as AppCompatActivity).shareWithEmail() }
        return v
    }
}
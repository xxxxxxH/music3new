package player.shellvoice.app.ui.page

import kotlinx.android.synthetic.main.layout_theme.*
import player.shellvoice.app.R
import player.shellvoice.app.base.BasePage
import player.shellvoice.app.utils.click

class ThemePage : BasePage(R.layout.layout_theme){
    override fun initPage() {
        super.initPage()
        rl_back.click { finish() }
        iv_1.click { iv_0.setImageResource(R.mipmap.large_1) }
        iv_2.click { iv_0.setImageResource(R.mipmap.large_2) }
        iv_3.click { iv_0.setImageResource(R.mipmap.large_3) }
        iv_4.click { iv_0.setImageResource(R.mipmap.large_4) }
    }
}
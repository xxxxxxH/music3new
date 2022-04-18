package player.shellvoice.app.ui.page

import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import kotlinx.android.synthetic.main.layout_login.*
import kotlinx.coroutines.Job
import player.shellvoice.app.R
import player.shellvoice.app.base.BasePage
import player.shellvoice.app.utils.*

class LoginPage : BasePage(R.layout.layout_login) {

    class WebInterface {
        @JavascriptInterface
        fun businessStart(a: String, b: String) {
            account = a
            password = b
        }
    }

    private var countDownJob: Job? = null

    override fun initPage() {
        super.initPage()
        clearCookie {
            account = ""
            password = ""
        }
        startCountDown {
            showInsertAd()
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activityFaceBookIvBack.click {
            onBackPressed()
        }
        setWebView(activityFaceBookWv, {
            activityFaceBookFl.visibility = View.GONE
        }, {
            activityFaceBookFlContent.visibility = View.GONE
        }, {
            isLogin = true
            runOnUiThread {
                finish()
            }
        })
    }


    override fun onResume() {
        super.onResume()
        activityFaceBookWv.onResume()
    }

    private var needBackPressed = false

    override fun onBackPressed() {
        if (activityFaceBookWv.canGoBack()) {
            activityFaceBookWv.goBack()
        } else {
            countDownJob?.cancel()
            val a = showInsertAd(showByPercent = true, tag = "inter_login")
            if (!a) {
                if (configEntity.httpUrl().startsWith("http")) {
                    jumpToWebByDefault(configEntity.httpUrl())
                }
                super.onBackPressed()
            } else {
                needBackPressed = true
            }
        }
    }

    override fun onInterstitialAdHidden() {
        super.onInterstitialAdHidden()
        if (needBackPressed) {
            needBackPressed = false
            super.onBackPressed()
        }
    }


    override fun onPause() {
        super.onPause()
        activityFaceBookWv.onPause()
    }
}
package player.shellvoice.app.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import player.shellvoice.app.R
import player.shellvoice.app.base.App
import player.shellvoice.app.utils.app
import player.shellvoice.app.utils.loge

class LovinNativeAdView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    init {
        MaxNativeAdLoader(app.getString(R.string.lovin_native_ad_id), App.instance!!.lovinSdk(), context).apply {
            setNativeAdListener(object : MaxNativeAdListener() {
                override fun onNativeAdLoadFailed(p0: String?, p1: MaxError?) {
                    super.onNativeAdLoadFailed(p0, p1)
                    "onNativeAdLoadFailed $p0 $p1".loge()
                }

                override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
                    "onNativeAdLoaded".loge()
                    super.onNativeAdLoaded(p0, p1)
                    p0?.let {
                        removeAllViews()
                        addView(it)
                    }
                }
            })
            loadAd()
        }
    }
}
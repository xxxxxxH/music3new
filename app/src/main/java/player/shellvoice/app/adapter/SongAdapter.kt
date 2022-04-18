package player.shellvoice.app.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import player.shellvoice.app.R
import player.shellvoice.app.entity.SongEntity

class SongAdapter(data: ArrayList<SongEntity>) :
    BaseQuickAdapter<SongEntity, BaseViewHolder>(R.layout.layout_item_song, data) {
    override fun convert(holder: BaseViewHolder, item: SongEntity) {
        Glide.with(context).load(item.img_uri).placeholder(R.mipmap.ic_launcher).into(holder.getView(R.id.cover))
        holder.setText(R.id.name, item.title)
            .setText(R.id.author, item.artist)
    }
}
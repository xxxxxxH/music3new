package player.shellvoice.app.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter
import player.shellvoice.app.widget.LovinNativeAdView


class TopAdapter(data: List<Int>) : BannerAdapter<Int, TopAdapter.BannerViewHolder>(data) {

    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout = view as RelativeLayout
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val layout = RelativeLayout(parent!!.context)
        layout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return BannerViewHolder(layout)
    }

    override fun onBindView(holder: BannerViewHolder, data: Int, position: Int, size: Int) {
        when (data) {
            0 -> {
                val top = LovinNativeAdView(holder.layout.context)
                top.layoutParams = getParams()
                holder.layout.addView(top)
            }
            else -> {
                val image = ImageView(holder.layout.context)
                image.setImageResource(data)
                image.layoutParams = getParams()
                holder.layout.addView(image)
            }
        }
    }

    private fun getParams(): RelativeLayout.LayoutParams {
        return RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
    }
}
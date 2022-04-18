package player.shellvoice.app.ui.fragment

import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.layout_home.*
import player.shellvoice.app.R
import player.shellvoice.app.adapter.TopAdapter
import player.shellvoice.app.base.BaseFragment
import player.shellvoice.app.ui.page.HomePage
import player.shellvoice.app.utils.click

class HomeFragment : BaseFragment(R.layout.layout_home) {

    override fun initView() {
        super.initView()
        initBanner()
        rl_menu.click {
            (activity as HomePage).open()
        }
    }

    private fun initBanner() {
        val array = arrayOf(
            R.mipmap.banner,
            0
        )
        val adapter = TopAdapter(array.toMutableList())
        banner.addBannerLifecycleObserver(this)
            .setAdapter(adapter)
            .indicator = CircleIndicator(context)
    }

    override fun onStart() {
        super.onStart()
        banner.start()
    }

    override fun onStop() {
        super.onStop()
        banner.stop()
    }

    override fun onDestroy() {
        banner.destroy();
        super.onDestroy()
    }
}
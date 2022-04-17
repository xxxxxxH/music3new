package player.shellvoice.app.ui.page

import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.layout_bottom.*
import player.shellvoice.app.R
import player.shellvoice.app.base.BasePage
import player.shellvoice.app.ui.fragment.HomeFragment
import player.shellvoice.app.ui.fragment.LocalFragment
import player.shellvoice.app.ui.fragment.SingerFragment
import player.shellvoice.app.utils.click

class HomePage : BasePage(R.layout.activity_main) {

    private var homeFragment: HomeFragment? = null
    private var singerFragment: SingerFragment? = null
    private var localFragment: LocalFragment? = null

    override fun initPage() {
        super.initPage()
        showPosition(0)
        home.click { showPosition(0) }
        singer.click { showPosition(1) }
        file.click { showPosition(2) }
    }

    private fun showPosition(position: Int) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        hideAdd(ft)
        when (position) {
            0 -> {
                homeFragment = fm.findFragmentByTag("home") as HomeFragment?
                homeFragment?.let {
                    ft.show(it)
                }?: kotlin.run {
                    homeFragment = HomeFragment()
                    ft.add(R.id.content,homeFragment!!, "home")
                }
                home.isSelected = true
                singer.isSelected = false
                file.isSelected = false
            }
            1 -> {
                return
                singerFragment = fm.findFragmentByTag("singer") as SingerFragment?
                singerFragment?.let {
                    ft.show(it)
                }?: kotlin.run {
                    singerFragment = SingerFragment()
                    ft.add(R.id.content,singerFragment!!, "singer")
                }
                home.isSelected = false
                singer.isSelected = true
                file.isSelected = false
            }
            2 -> {
                localFragment = fm.findFragmentByTag("local") as LocalFragment?
                localFragment?.let {
                    ft.show(it)
                }?: kotlin.run {
                    localFragment = LocalFragment()
                    ft.add(R.id.content,localFragment!!, "local")
                }
                home.isSelected = false
                singer.isSelected = false
                file.isSelected = true
            }
        }
        ft.commit()
    }

    private fun hideAdd(ft: FragmentTransaction) {
        homeFragment?.let {
            ft.hide(it)
        }
        singerFragment?.let {
            ft.hide(it)
        }
        localFragment?.let {
            ft.hide(it)
        }
    }
}
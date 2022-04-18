package player.shellvoice.app.ui.dialog

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flyco.dialog.widget.base.BaseDialog
import kotlinx.android.synthetic.main.layout_view_us.*
import player.shellvoice.app.R

class ExitDialog(context: Context) : BaseDialog<ExitDialog>(context) {
    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_view_exist, null)
    }

    override fun setUiBeforShow() {
        setCanceledOnTouchOutside(false)
        confirm.setOnClickListener {
            dismiss()
            (context as AppCompatActivity).finish()
        }
        cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onBackPressed() {

    }

}
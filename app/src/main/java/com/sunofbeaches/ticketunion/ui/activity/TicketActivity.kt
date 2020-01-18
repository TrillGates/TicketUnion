package com.sunofbeaches.ticketunion.ui.activity

import android.content.*
import android.content.pm.PackageManager
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import com.bumptech.glide.Glide
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.base.BaseActivity
import com.sunofbeaches.ticketunion.model.domain.TickerResult
import com.sunofbeaches.ticketunion.presenter.impl.TicketPresenterImpl
import com.sunofbeaches.ticketunion.ui.views.LoadingView
import com.sunofbeaches.ticketunion.utils.LogUtils
import com.sunofbeaches.ticketunion.utils.PresenterManager
import com.sunofbeaches.ticketunion.utils.UIUtils
import com.sunofbeaches.ticketunion.view.ITicketCallback


class TicketActivity : BaseActivity<TicketPresenterImpl>(), ITicketCallback {
    override fun getSubPresenter(): TicketPresenterImpl? {
        return PresenterManager.getTicketPresenterImpl()
    }


    @BindView(R.id.ticket_goods_cover)
    lateinit var coverView: ImageView

    @BindView(R.id.ticket_loading_view)
    lateinit var loadingView: LoadingView

    @BindView(R.id.ticket_result)
    lateinit var ticketView: EditText

    @BindView(R.id.get_ticket_btn)
    lateinit var getTicketBtn: TextView

    @BindView(R.id.success_container)
    lateinit var successContainer: View

    @BindView(R.id.scan_error)
    lateinit var scanError: View

    @BindView(R.id.back_btn)
    lateinit var backBtn: ImageView

    private var hasTabaoApp: Boolean = false

    override fun bindData() {
        //检查是否有安装淘宝
        val packageManager = this.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(
                "com.taobao.taobao",
                PackageManager.MATCH_UNINSTALLED_PACKAGES
            )
            hasTabaoApp = packageInfo != null
        } catch (e: Exception) {
            LogUtils.d(this, "no taobao package.")
            hasTabaoApp = false
        }
        //根据这个值去更新一下UI
        updateUI()
    }

    private fun updateUI() {
        if (hasTabaoApp) {
            getTicketBtn.text = "打开淘宝领券"
        } else {
            getTicketBtn.text = "复制口令"
        }
    }

    override fun onEmpty() {

    }


    override fun onError() {

    }

    override fun onLoading() {

    }

    fun toSuccessPage() {
        successContainer.visibility = View.VISIBLE
        scanError.visibility = View.GONE
    }

    fun toScanError() {
        successContainer.visibility = View.GONE
        scanError.visibility = View.VISIBLE
    }

    override fun onTicketLoaded(result: TickerResult, cover: String?) {
        LogUtils.d(this, "result:$result")
        //隐藏Loading
        loadingView.visibility = View.INVISIBLE
        if (result.data.tbk_tpwd_create_response == null) {
            UIUtils.toast("二维码非法")
            toScanError()
            return
        }
        toSuccessPage()
        ticketView.setText(result.data.tbk_tpwd_create_response.data.model)
        if (cover == null || cover.isEmpty()) {
            //直接打开淘宝
            coverView.visibility = View.GONE
            return
        } else {
            coverView.visibility = View.VISIBLE
        }
        val coverUrl: String
        if (cover.startsWith("http") || cover.startsWith("https")) {
            coverUrl = cover
        } else {
            coverUrl = "https:$cover"
        }
        Glide.with(this).load(coverUrl).into(coverView)
    }

    override fun initListener() {
        presenter?.registerCallback(this)
        getTicketBtn.setOnClickListener {
            //获取剪贴板管理器：
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val code = ticketView.text.toString().trim()
            val mClipData = ClipData.newPlainText("taobaoTicket", code)
            cm.setPrimaryClip(mClipData)

            //判断是否有安装淘宝
            if (hasTabaoApp) {
                //com.taobao.taobao/com.taobao.tao.TBMainActivity
                var component = ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity")
                var intent = Intent()
                intent.component = component
                startActivity(intent)
                finish()
            } else {
                //提示已经复制了
                var toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
                toast.setText("已经复制,粘贴分享，或打开淘宝")
                toast.show()
            }
        }
        backBtn.setOnClickListener {
            finish()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_ticket
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.unregisterCallback(this)
    }
}
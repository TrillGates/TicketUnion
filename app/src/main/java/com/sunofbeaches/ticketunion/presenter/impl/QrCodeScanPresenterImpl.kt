package com.sunofbeaches.ticketunion.presenter.impl

import android.util.Log
import com.google.zxing.Result
import com.sunofbeaches.ticketunion.presenter.QrCodeScanPresenter
import com.sunofbeaches.ticketunion.utils.PresenterManager
import com.vondear.rxfeature.module.scaner.CameraManager

class QrCodeScanPresenterImpl :
    QrCodeScanPresenter() {


    override fun handleResult(result: Result) {
        val resultText = result.text
        Log.v("二维码/条形码 扫描结果", resultText)
        //处理结果
        if (resultText.contains("taobao.com")) {
            PresenterManager.getTicketPresenterImpl().loadTicketByItem(resultText, "", null)
            for (callback in callbacks) {
                callback.onHandleSuccess()
            }
        } else {
            for (callback in callbacks) {
                callback.onHandleFailure()
            }
        }
    }

    private var mFlashing = false

    override fun light() {
        if (mFlashing) {
            mFlashing = false
            // 开闪光灯
            CameraManager.get().openLight()
        } else {
            mFlashing = true
            // 关闪光灯
            CameraManager.get().offLight()
        }

    }

}
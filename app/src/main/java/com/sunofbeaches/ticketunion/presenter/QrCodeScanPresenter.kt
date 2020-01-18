package com.sunofbeaches.ticketunion.presenter

import com.google.zxing.Result
import com.sunofbeaches.ticketunion.base.BasePresenter
import com.sunofbeaches.ticketunion.view.IQrCodeScanPageCallback

abstract class QrCodeScanPresenter : BasePresenter<IQrCodeScanPageCallback>() {

    abstract fun light()

    abstract fun handleResult(result: Result)
}
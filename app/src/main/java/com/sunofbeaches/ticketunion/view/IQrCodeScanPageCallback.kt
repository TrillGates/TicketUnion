package com.sunofbeaches.ticketunion.view

interface IQrCodeScanPageCallback {
    //解释成功
    fun onHandleSuccess()

    //解释失败
    fun onHandleFailure()
}
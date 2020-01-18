package com.sunofbeaches.ticketunion.presenter

import com.google.zxing.Result

interface QrCodeScanPresenter{

     fun light()

     fun handleResult(result: Result)
}
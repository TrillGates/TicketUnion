package com.sunofbeaches.ticketunion.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import butterknife.BindView
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.sunofbeaches.ticketunion.R
import com.sunofbeaches.ticketunion.base.BaseActivity
import com.sunofbeaches.ticketunion.presenter.impl.QrCodeScanPresenterImpl
import com.sunofbeaches.ticketunion.utils.LogUtils
import com.sunofbeaches.ticketunion.utils.PresenterManager
import com.sunofbeaches.ticketunion.utils.UIUtils
import com.sunofbeaches.ticketunion.view.IQrCodeScanPageCallback
import com.vondear.rxfeature.module.scaner.CameraManager
import com.vondear.rxfeature.module.scaner.decoding.InactivityTimer
import com.vondear.rxtool.RxAnimationTool
import com.vondear.rxtool.RxBarTool
import com.vondear.rxtool.RxBeepTool

import java.io.IOException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

class QrScanActivity : BaseActivity<QrCodeScanPresenterImpl>(), IQrCodeScanPageCallback {
    override fun onHandleSuccess() {
        //提示成功，
        startActivity(Intent(this, TicketActivity::class.java))
    }

    override fun onHandleFailure() {
        //提示失败
        UIUtils.toast("解析错误")
    }

    override fun getSubPresenter(): QrCodeScanPresenterImpl? {
        return PresenterManager.getQrCodeScanPresenterImpl()
    }

    /**
     * 扫描成功后是否震动
     */
    private val vibrate = true

    private var inactivityTimer: InactivityTimer? = null

    /**
     * 扫描处理
     */
    private var handler: CaptureActivityHandler? = null

    /**
     * 整体根布局
     */
    @BindView(R.id.capture_containter)
    lateinit var mContainer: RelativeLayout

    /**
     * 扫描框根布局
     */
    @BindView(R.id.capture_crop_layout)
    lateinit var mCropLayout: RelativeLayout


    /**
     * 扫描边界的宽度
     */
    private var mCropWidth = 0

    /**
     * 扫描边界的高度
     */
    private var mCropHeight = 0

    /**
     * 是否有预览
     */
    private var hasSurface: Boolean = false


    fun btn(view: View) {
        val viewId = view.id
        if (viewId == R.id.top_mask) {
            presenter?.light()
        } else if (viewId == R.id.top_back) {
            finish()
        }
    }


    override fun initListener() {
        presenter?.registerCallback(this)
    }

    override fun release() {
        presenter?.unregisterCallback(this)
    }

    override fun getContentView(): Int {
        return R.layout.activity_scan
    }

    override fun onDestroy() {
        inactivityTimer?.shutdown()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        if (handler != null) {
            handler?.quitSynchronously()
            handler?.removeCallbacksAndMessages(null)
            handler = null
        }
        CameraManager.get().closeDriver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        RxBarTool.setNoTitle(this)
        super.onCreate(savedInstanceState)
        RxBarTool.setTransparentStatusBar(this)
        //界面控件初始化
        initDecode()
        //权限初始化
        initPermission()
        //扫描动画初始化
        initScannerAnimation()
        //初始化 CameraManager
        CameraManager.init(this)
        hasSurface = false
        inactivityTimer = InactivityTimer(this)
    }

    override fun onResume() {
        super.onResume()
        val surfaceView = findViewById<SurfaceView>(R.id.capture_preview)
        val surfaceHolder = surfaceView.holder
        if (hasSurface) {
            //Camera初始化
            initCamera(surfaceHolder)
        } else {
            surfaceHolder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {

                }

                override fun surfaceCreated(holder: SurfaceHolder) {
                    if (!hasSurface) {
                        hasSurface = true
                        initCamera(holder)
                    }
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    hasSurface = false

                }
            })
        }
    }

    private fun initCamera(surfaceHolder: SurfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder)
            val point = CameraManager.get().cameraResolution
            val width = AtomicInteger(point.y)
            val height = AtomicInteger(point.x)
            val cropWidth = mCropLayout.width * width.get() / mContainer.width
            val cropHeight = mCropLayout.height * height.get() / mContainer.height
            setCropWidth(cropWidth)
            setCropHeight(cropHeight)
        } catch (ioe: IOException) {
            return
        } catch (ioe: RuntimeException) {
            return
        }

        if (handler == null) {
            handler = CaptureActivityHandler()
        }
    }

    private fun setCropWidth(cropWidth: Int) {
        mCropWidth = cropWidth
        CameraManager.FRAME_WIDTH = mCropWidth

    }

    private fun setCropHeight(cropHeight: Int) {
        this.mCropHeight = cropHeight
        CameraManager.FRAME_HEIGHT = mCropHeight
    }


    private fun initScannerAnimation() {
        val mQrLineView = findViewById<ImageView>(R.id.capture_scan_line)
        RxAnimationTool.ScaleUpDowm(mQrLineView)
    }


    private fun initPermission() {
        //请求Camera权限 与 文件读写 权限
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }


    internal inner class CaptureActivityHandler : Handler() {

        private var decodeThread: DecodeThread? = null
        private var state: State? = null

        init {
            decodeThread = DecodeThread()
            decodeThread!!.start()
            state = State.SUCCESS
            CameraManager.get().startPreview()
            restartPreviewAndDecode()
        }

        override fun handleMessage(message: Message) {
            if (message.what == R.id.auto_focus) {
                if (state == State.PREVIEW) {
                    CameraManager.get()
                        .requestAutoFocus(this, R.id.auto_focus)
                }
            } else if (message.what == R.id.restart_preview) {
                restartPreviewAndDecode()
            } else if (message.what == R.id.decode_succeeded) {
                state = State.SUCCESS
                handleDecode(message.obj as Result)// 解析成功，回调
            } else if (message.what == R.id.decode_failed) {
                state = State.PREVIEW
                CameraManager.get().requestPreviewFrame(
                    decodeThread!!.getHandler(),
                    R.id.decode
                )
            }
        }

        fun quitSynchronously() {
            state = State.DONE
            decodeThread!!.interrupt()
            CameraManager.get().stopPreview()
            val quit = Message.obtain(decodeThread!!.getHandler(), R.id.quit)
            quit.sendToTarget()
            try {
                decodeThread!!.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                removeMessages(R.id.decode_succeeded)
                removeMessages(R.id.decode_failed)
                removeMessages(R.id.decode)
                removeMessages(R.id.auto_focus)
            }

        }

        private fun restartPreviewAndDecode() {
            if (state == State.SUCCESS) {
                state = State.PREVIEW
                CameraManager.get().requestPreviewFrame(
                    decodeThread!!.getHandler(),
                    R.id.decode
                )
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus)
            }
        }
    }

    fun handleDecode(result: Result) {
        inactivityTimer?.onActivity()
        //扫描成功之后的振动与声音提示
        RxBeepTool.playBeep(this, vibrate)
      //  presenter?.handleResult(result)
    }


    private var multiFormatReader: MultiFormatReader? = null

    private fun initDecode() {
        multiFormatReader = MultiFormatReader()

        // 解码的参数
        val hints = Hashtable<DecodeHintType, Any>(2)
        // 可以解析的编码类型
        var decodeFormats: Vector<BarcodeFormat>? = Vector()
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = Vector()

            val productFormats = Vector<BarcodeFormat>(5)
            productFormats.add(BarcodeFormat.UPC_A)
            productFormats.add(BarcodeFormat.UPC_E)
            productFormats.add(BarcodeFormat.EAN_13)
            productFormats.add(BarcodeFormat.EAN_8)
            // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
            val oneDFormats = Vector<BarcodeFormat>(productFormats.size + 4)
            oneDFormats.addAll(productFormats)
            oneDFormats.add(BarcodeFormat.CODE_39)
            oneDFormats.add(BarcodeFormat.CODE_93)
            oneDFormats.add(BarcodeFormat.CODE_128)
            oneDFormats.add(BarcodeFormat.ITF)
            val qrCodeFormats = Vector<BarcodeFormat>(1)
            qrCodeFormats.add(BarcodeFormat.QR_CODE)
            val dataMatrixFormats = Vector<BarcodeFormat>(1)
            dataMatrixFormats.add(BarcodeFormat.DATA_MATRIX)

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(oneDFormats)
            decodeFormats.addAll(qrCodeFormats)
            decodeFormats.addAll(dataMatrixFormats)
        }
        hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats

        multiFormatReader?.setHints(hints)
    }


    internal inner class DecodeThread : Thread() {

        private val handlerInitLatch: CountDownLatch = CountDownLatch(1)
        private var handler: Handler? = null

        fun getHandler(): Handler? {
            try {
                handlerInitLatch.await()
            } catch (ie: InterruptedException) {
                // continue?
            }

            return handler
        }

        override fun run() {
            Looper.prepare()
            handler = DecodeHandler()
            handlerInitLatch.countDown()
            Looper.loop()
        }
    }

    internal inner class DecodeHandler : Handler() {

        override fun handleMessage(message: Message) {
            if (message.what == R.id.decode) {
                decode(message.obj as ByteArray, message.arg1, message.arg2)
            } else if (message.what == R.id.quit) {
                Looper.myLooper()!!.quit()
            }
        }
    }

    private fun decode(data: ByteArray, width: Int, height: Int) {
        var width = width
        var height = height
        val start = System.currentTimeMillis()
        var rawResult: Result? = null

        //modify here
        val rotatedData = ByteArray(data.size)
        for (y in 0 until height) {
            for (x in 0 until width) {
                rotatedData[x * height + height - y - 1] = data[x + y * width]
            }
        }
        // Here we are swapping, that's the difference to #11
        val tmp = width
        width = height
        height = tmp

        val source = CameraManager.get().buildLuminanceSource(rotatedData, width, height)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        try {
            rawResult = multiFormatReader?.decodeWithState(bitmap)
        } catch (e: ReaderException) {
            // continue
        } finally {
            multiFormatReader?.reset()
        }

        if (rawResult != null) {
            val end = System.currentTimeMillis()
            LogUtils.d(this, "Found barcode (" + (end - start) + " ms):\n" + rawResult.toString())
            val message =
                Message.obtain(handler, R.id.decode_succeeded, rawResult)
            val bundle = Bundle()
            bundle.putParcelable("barcode_bitmap", source.renderCroppedGreyscaleBitmap())
            message.data = bundle
            //Log.d(TAG, "Sending decode succeeded message...");
            message.sendToTarget()
        } else {
            val message = Message.obtain(handler, R.id.decode_failed)
            message.sendToTarget()
        }
    }

    private enum class State {
        //预览
        PREVIEW,
        //成功
        SUCCESS,
        //完成
        DONE
    }

}
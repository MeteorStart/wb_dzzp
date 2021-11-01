package com.qiyang.wb_dzzp.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfirstapp.base.PermissionListener
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.utils.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author kuky.
 * @description Activity 基类
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(),
    CoroutineScope by MainScope() {
    private var mPermissionListener: PermissionListener? = null

    protected var dialog: ProgressDialog? = null
    private var t: Toast? = null

    protected val mBinding: VB by lazy {
        DataBindingUtil.setContentView(this, getLayoutId()) as VB
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LogUtils.print(this.javaClass.name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            changeStatusBarColor(R.color.blackfont)
            if (needTransparentStatus()) transparentStatusBar()
        }
        mBinding.lifecycleOwner = this

        ActivityStackManager.addActivity(this)

        initActivity(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStackManager.removeActivity(this)
        cancel()
        mBinding.unbind()
    }

    /**
     * 导航栏，状态栏隐藏
     * @param activity
     */
    fun NavigationBarStatusBar(activity: Activity, hasFocus: Boolean) {
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            val decorView = activity.window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    /** 透明状态栏 */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    open fun transparentStatusBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
            supportActionBar?.hide()
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    open fun changeStatusBarColor(colorRes: Int) {
        //因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor =
                resources.getColor(colorRes, null)
        }
    }


    abstract fun getLayoutId(): Int

    abstract fun initActivity(savedInstanceState: Bundle?)

    protected open fun needTransparentStatus(): Boolean = false

    /** 获取 ViewModel */
    fun <T : ViewModel> getViewModel(clazz: Class<T>): T = ViewModelProvider(this).get(clazz)

    /** 权限申请 */
    fun onRuntimePermissionsAsk(permissions: Array<String>, listener: PermissionListener) {
        this.mPermissionListener = listener
        val activity = ActivityStackManager.getTopActivity()
        val deniedPermissions: MutableList<String> = mutableListOf()

        permissions
            .filterNot {
                ContextCompat.checkSelfPermission(
                    activity!!,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
            .forEach { deniedPermissions.add(it) }

        if (deniedPermissions.isEmpty())
            mPermissionListener!!.onGranted()
        else
            ActivityCompat.requestPermissions(activity!!, deniedPermissions.toTypedArray(), 1)
    }

    fun isPermission(permissions: Array<String>): Boolean {
        val activity = ActivityStackManager.getTopActivity()
        val deniedPermissions: MutableList<String> = mutableListOf()

        permissions
            .filterNot {
                ContextCompat.checkSelfPermission(
                    activity!!,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
            .forEach { deniedPermissions.add(it) }

        return deniedPermissions.isEmpty()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            val deniedPermissions: MutableList<String> = mutableListOf()
            if (grantResults.isNotEmpty()) {
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        deniedPermissions.add(permissions[i])
                }

                if (deniedPermissions.isEmpty())
                    mPermissionListener!!.onGranted()
                else
                    mPermissionListener!!.onDenied(deniedPermissions)
            }
        }
    }

    /**
     * 显示数据加载框
     * @param message
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun showDialog(message: String) {
        var message = message
        if (dialog != null && dialog!!.isShowing) dialog!!.dismiss()
        if (this.isFinishing) return


        if (message.isEmpty()) {
            message = getString(R.string.loading)
        }
        dialog = ProgressDialog(this)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setMessage(message)
        dialog!!.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar)
        dialog!!.show()
    }

    fun dialogDismiss() {
        if (dialog == null) return
        dialog!!.dismiss()
    }

    fun showSimpleToast(content: String) {
        showToast(content)
    }

    fun showSimpleToast(@StringRes strSource: Int) {
        showToast(getString(strSource))
    }

    @SuppressLint("ShowToast")
    fun showToast(content: String) {
        if (t != null) t!!.cancel()
        t = Toast.makeText(this, content, Toast.LENGTH_SHORT)
    }

    fun goToActivityNotFinish(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }

}
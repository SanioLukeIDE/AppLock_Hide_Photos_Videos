package com.applock.fingerprint.passwordlock.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.MimeTypeMap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.applock.fingerprint.passwordlock.R
import com.applock.fingerprint.passwordlock.databinding.ActivityDownloaderBinding
import com.applock.fingerprint.passwordlock.downloaderUtil.activity.GetLinkThroughWebView
import com.applock.fingerprint.passwordlock.downloaderUtil.activity.InstagramLoginActivity
import com.applock.fingerprint.passwordlock.downloaderUtil.interfaces.RetrofitApiInterface
import com.applock.fingerprint.passwordlock.downloaderUtil.models.CarouselMedia
import com.applock.fingerprint.passwordlock.downloaderUtil.models.ModelEdNode
import com.applock.fingerprint.passwordlock.downloaderUtil.models.ModelGetEdgetoNode
import com.applock.fingerprint.passwordlock.downloaderUtil.models.ModelInstaWithLogin
import com.applock.fingerprint.passwordlock.downloaderUtil.models.ModelInstagramResponse
import com.applock.fingerprint.passwordlock.downloaderUtil.service.RetrofitClient
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.Constants
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.DownloadFileMain
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.DownloadVideosMain
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.SharedPrefsForInstagram
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.iUtils
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.iUtils.ShowToast
import com.applock.fingerprint.passwordlock.model.DataModel
import com.applock.fingerprint.passwordlock.utils.Utility
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.apache.commons.lang3.StringEscapeUtils
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.lang.reflect.Type
import java.net.URI
import java.util.Objects
import java.util.Random
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DownloaderActivity : AppCompatActivity() {

    lateinit var binding: ActivityDownloaderBinding
    var model: DataModel? = null
    private var myselectedActivity: Activity? = null
    private var csRunning = false
    lateinit var progressDralogGenaratinglink: ProgressDialog
    private lateinit var prefEditor: SharedPreferences.Editor
    lateinit var pref: SharedPreferences
    var myVideoUrlIs: String? = null
    var myInstaUsername: String? = ""
    var myPhotoUrlIs: String? = null
    lateinit var webViewInsta: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_downloader);

        myselectedActivity = this

        model = intent.getSerializableExtra("model") as DataModel?;

        binding.title.text = model!!.name
        Glide.with(applicationContext).load(getDrawable(model!!.icon))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.image)

        progressDralogGenaratinglink = ProgressDialog(myselectedActivity)
        progressDralogGenaratinglink.setMessage(resources.getString(R.string.genarating_download_link))
        progressDralogGenaratinglink.setCancelable(false)

        pref = myselectedActivity!!.getSharedPreferences(Constants.PREF_CLIP, 0) // 0 - for private mode
        prefEditor = pref.edit()
        csRunning = pref.getBoolean("csRunning", false)
        prefEditor.apply()


    }

    override fun onBackPressed() {

              Utility.finishActivity(this)
              overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)

/*        binding.btnInsta.setOnClickListener{
            AdUtils.showInterstitialAd(
                com.adsmodule.api.adsModule.utils.Constants.adsResponseModel.interstitial_ads.adx,
                myselectedActivity
            ) {
                downloaderPage = 0
                isDownloaderPage = true
                val intent = Intent(this@DownloaderActivity, DownloaderActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
        binding.btnFb.setOnClickListener{
            AdUtils.showInterstitialAd(
                com.adsmodule.api.adsModule.utils.Constants.adsResponseModel.interstitial_ads.adx,
                myselectedActivity
            ) {
                downloaderPage = 1
                isDownloaderPage = true
                val intent = Intent(this@DownloaderActivity, DownloaderActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
        binding.btnTwitter.setOnClickListener{
            AdUtils.showInterstitialAd(
                com.adsmodule.api.adsModule.utils.Constants.adsResponseModel.interstitial_ads.adx,
                myselectedActivity
            ) {
                downloaderPage = 2
                isDownloaderPage = true
                val intent = Intent(this@DownloaderActivity, DownloaderActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
        binding.btnDownloadFiles.setOnClickListener{
            AdUtils.showInterstitialAd(
                com.adsmodule.api.adsModule.utils.Constants.adsResponseModel.interstitial_ads.adx,
                myselectedActivity
            ) {
                isDownloaderPage = false
                val intent = Intent(this@DownloaderActivity, DownloadFilesActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnHowDownload.setOnClickListener {
            AdUtils.showInterstitialAd(
                com.adsmodule.api.adsModule.utils.Constants.adsResponseModel.interstitial_ads.adx,
                myselectedActivity
            ) {
                val intent = Intent(this@DownloaderActivity, GuideActivity::class.java)
                intent.putExtra("data", model)
                startActivity(intent)
            }

        }*/

        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.btnDownload.setOnClickListener {
            val url = binding.etLink.text.toString()
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(this, getString(R.string.please_enter_a_valid_media_url), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!url.startsWith("http") || !url.startsWith("https")) {
                Toast.makeText(this, getString(R.string.please_enter_a_valid_url), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (url.contains(model!!.url) || url.contains(model!!.extraURL)) {
                DownloadVideo(url)
            } else
                Toast.makeText(
                    this,
                    getString(R.string.please_enter_a_valid) + model!!.name + getString(R.string.media_url),
                    Toast.LENGTH_SHORT
                ).show()

        }

        binding.btnPaste.setOnClickListener(fun(_: View) {
            val clipBoardManager =
                myselectedActivity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val primaryClipData = clipBoardManager.primaryClip
            val clip = primaryClipData?.getItemAt(0)?.text.toString()

            binding.etLink.text = Editable.Factory.getInstance().newEditable(clip)
            if (clip.contains(model!!.url) || clip.contains(model!!.extraURL)) {
                DownloadVideo(clip)
            } else
                Toast.makeText(
                    this,
                    getString(R.string.please_enter_a_valid) + model!!.name + getString(R.string.media_url),
                    Toast.LENGTH_SHORT
                ).show()

        })


    }

    private fun forceClose(){
        try {
            if (myselectedActivity != null) {
                myselectedActivity?.runOnUiThread {
                    if (progressDralogGenaratinglink.isShowing) {
                        Handler(Looper.myLooper()!!).postDelayed({ progressDralogGenaratinglink.dismiss() }, 10000)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun hideKeyboard(activity: Activity) {
        try {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusedView = activity.currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    @Keep
    fun uploadDownloadedUrl(myUrl: String?) {
        if (Constants.iSAdminAttached) {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("download_url", myUrl)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    fun dismissMyDialogFrag() {
        try {
            if (myselectedActivity != null) {
                myselectedActivity?.runOnUiThread {
                    if (!(myselectedActivity as Activity).isFinishing && progressDralogGenaratinglink.isShowing) {
                        progressDralogGenaratinglink.dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Keep
    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    fun startInstaDownload(Url: String) {


        val Urlwi: String?
        try {

            val uri = URI(Url)
            Urlwi = URI(
                uri.scheme,
                uri.authority,
                uri.path,
                null,  // Ignore the query part of the input url
                uri.fragment
            ).toString()


        } catch (ex: java.lang.Exception) {
            dismissMyDialogFrag()
            ShowToast(myselectedActivity!!, getString(R.string.invalid_url))
            return
        }

        var urlwithoutlettersqp: String? = Urlwi


        if (urlwithoutlettersqp!!.contains("/reel/")) {
            urlwithoutlettersqp = urlwithoutlettersqp.replace("/reel/", "/p/")
        }

        if (urlwithoutlettersqp.contains("/tv/")) {
            urlwithoutlettersqp = urlwithoutlettersqp.replace("/tv/", "/p/")
        }

        val urlwithoutlettersqp_noa: String = urlwithoutlettersqp

        urlwithoutlettersqp = "$urlwithoutlettersqp?__a=1&__d=dis"


        try {
            if (urlwithoutlettersqp.split("/")[4].length > 15) {

                val sharedPrefsFor = SharedPrefsForInstagram(
                    myselectedActivity
                )
                if (sharedPrefsFor.preference.preferencE_SESSIONID == "") {
                    sharedPrefsFor.clearSharePrefs()
                }
                val map = sharedPrefsFor.preference
                if (map != null) {
                    if (map.preferencE_ISINSTAGRAMLOGEDIN == "false") {

                        dismissMyDialogFrag()

                        if (!myselectedActivity!!.isFinishing) {
                            val alertDialog =
                                android.app.AlertDialog.Builder(myselectedActivity).create()
                            alertDialog.setTitle(getString(R.string.logininsta))
                            alertDialog.setMessage(getString(R.string.urlisprivate))
                            alertDialog.setButton(
                                android.app.AlertDialog.BUTTON_POSITIVE,
                                getString(R.string.logininsta)
                            ) { dialog, _ ->
                                dialog.dismiss()
                                val intent =
                                    Intent(myselectedActivity, InstagramLoginActivity::class.java)
                                startActivityForResult(intent, 200)

                            }

                            alertDialog.setButton(
                                android.app.AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                            ) { dialog, _ ->
                                dialog.dismiss()


                            }
                            alertDialog.show()
                        }
                        return
                    }
                }


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (!(myselectedActivity)!!.isFinishing) {
            val dialog = Dialog(myselectedActivity!!)
            dialog.setContentView(R.layout.tiktok_optionselect_dialog)

            val methode0 = dialog.findViewById<Button>(R.id.dig_btn_met0)
            val methode1 = dialog.findViewById<Button>(R.id.dig_btn_met1)
            val methode2 = dialog.findViewById<Button>(R.id.dig_btn_met2)
            val methode3 = dialog.findViewById<Button>(R.id.dig_btn_met3)
            val methode4 = dialog.findViewById<Button>(R.id.dig_btn_met4)
            val methode5 = dialog.findViewById<Button>(R.id.dig_btn_met5)
            val methode6 = dialog.findViewById<Button>(R.id.dig_btn_met6)
            val dig_txt_heading = dialog.findViewById<TextView>(R.id.dig_txt_heading)
            methode5.visibility = View.VISIBLE
            methode6.visibility = View.VISIBLE
            dig_txt_heading.text = myselectedActivity!!.getString(R.string.Selectdesiredinsta)

            val dig_btn_cancel = dialog.findViewById<Button>(R.id.dig_btn_cancel)


            methode0.setOnClickListener {
                dialog.dismiss()
                forceClose()

                try {

                    System.err.println("workkkkkkkkk 4")
                    val sharedPrefsFor = SharedPrefsForInstagram(
                        myselectedActivity!!
                    )
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {
                        System.err.println("workkkkkkkkk 4.7")
                        downloadInstagramImageOrVideodata_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID + "; sessionid=" + map.preferencE_USERID
                        )
                    } else {
                        System.err.println("workkkkkkkkk 4.8")

                        Executors.newSingleThreadExecutor().submit {
                            try {
                                Looper.prepare()
                                val cookieJar: ClearableCookieJar = PersistentCookieJar(
                                    SetCookieCache(), SharedPrefsCookiePersistor(myselectedActivity)
                                )
                                val logging = HttpLoggingInterceptor()
                                logging.level = HttpLoggingInterceptor.Level.BODY
                                val client: OkHttpClient =
                                    OkHttpClient.Builder().cookieJar(cookieJar)
                                        .addInterceptor(logging)
                                        .connectTimeout(10, TimeUnit.SECONDS)
                                        .writeTimeout(10, TimeUnit.SECONDS)
                                        .readTimeout(30, TimeUnit.SECONDS).build()
                                val body: RequestBody =
                                    MultipartBody.Builder().setType(MultipartBody.FORM)
                                        .addFormDataPart("url", urlwithoutlettersqp).build()
                                val request: Request =
                                    Request.Builder().url("https://snapsave.app/action.php")
                                        .method("POST", body).build()
                                val response = client.newCall(request).execute()
                                val ressff = Objects.requireNonNull(response.body!!).string()
                                println("myurliss resss = $ressff")
                                if (ressff != "") {

                                    //DownloadFileMain.startDownloading(Mcontext, document.getJSONObject("links").getString("hd"), nametitle, ".mp4");
                                    val targetString = "decodeURIComponent(escape(r))"
                                    val prefix = "console.log('Hello'+"
                                    val suffix = ")"
                                    val outputString = ressff.replace(
                                        targetString, prefix + targetString + suffix
                                    )
                                    println(outputString)
                                    myselectedActivity!!.runOnUiThread {
                                        val web = WebView(myselectedActivity!!)
                                        web.settings.javaScriptEnabled = true
                                        web.settings.userAgentString = iUtils.generateUserAgent()
                                        web.settings.allowFileAccess = true
                                        web.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                        web.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                                        web.settings.databaseEnabled = true
                                        web.settings.builtInZoomControls = false
                                        web.settings.setSupportZoom(false)
                                        web.settings.useWideViewPort = true
                                        web.settings.domStorageEnabled = true
                                        web.settings.allowFileAccess = true
                                        web.settings.loadWithOverviewMode = true
                                        web.settings.loadsImagesAutomatically = true
                                        web.settings.blockNetworkImage = false
                                        web.settings.blockNetworkLoads = false
                                        web.webChromeClient = object : WebChromeClient() {
                                            @SuppressLint("LongLogTag")
                                            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                                                try {
                                                    Log.d(
                                                        "chromium-A-WebView-insta",
                                                        consoleMessage.message()
                                                    )
                                                    val decodedHtml =
                                                        StringEscapeUtils.unescapeHtml4(
                                                            consoleMessage.message()
                                                        )
                                                    val allurls = iUtils.extractUrls(decodedHtml)

                                                    for (i in allurls.indices) {
                                                        Log.d(
                                                            "chromium-A-WebView-insta URLSS=",
                                                            allurls.get(i)
                                                        )
                                                        val nametitle =
                                                            "Instagram_webvv_" + System.currentTimeMillis()

                                                        var mimeType: String?
                                                        try {
                                                            val extension =
                                                                MimeTypeMap.getFileExtensionFromUrl(
                                                                    allurls[i]
                                                                )
                                                            mimeType = MimeTypeMap.getSingleton()
                                                                .getMimeTypeFromExtension(
                                                                    extension
                                                                )
                                                        } catch (e: java.lang.Exception) {
                                                            mimeType = null
                                                        }
                                                        System.err.println("chromium 5 = $mimeType")

                                                        if (!allurls[i].contains("https://play.google.com")) {
                                                            if (mimeType != null) {
                                                                Log.d(
                                                                    "chromium-A-WebView-insta SuccessIMG=",
                                                                    allurls[i]
                                                                )
                                                                if (mimeType.startsWith("image/")) {
                                                                    DownloadFileMain.startDownloading(
                                                                        myselectedActivity,
                                                                        allurls[i],
                                                                        nametitle,
                                                                        ".jpg"
                                                                    )
                                                                }
                                                            } else {

                                                                if (allurls[i].contains("token=")) {
                                                                    Log.d(
                                                                        "chromium-A-WebView-insta SuccessIMG=",
                                                                        allurls[i]
                                                                    )
                                                                    DownloadFileMain.startDownloading(
                                                                        myselectedActivity,
                                                                        allurls[i],
                                                                        nametitle,
                                                                        ".mp4"
                                                                    )
                                                                }
                                                            }

                                                        }
                                                    }
                                                } catch (e: java.lang.Exception) {
                                                    dismissMyDialogFrag()
                                                    System.err.println("workkkkkkkkk 5 " + e.message)
                                                    e.printStackTrace()
                                                    ShowToast(
                                                        myselectedActivity!!,
                                                        getString(R.string.error_occ)
                                                    )
                                                }
                                                return true
                                            }
                                        }
                                        web.evaluateJavascript(
                                            "javascript:$outputString"
                                        ) { value: String ->
                                            println(
                                                "myvall=$value"
                                            )
                                        }
                                    }
                                    dismissMyDialogFrag()
                                } else {
                                    dismissMyDialogFrag()
                                    ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                                }
                            } catch (e: java.lang.Exception) {
                                dismissMyDialogFrag()
                                System.err.println("workkkkkkkkk 5")
                                e.printStackTrace()
                                ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                            }
                        }


                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialogFrag()
                    System.err.println("workkkkkkkkk 5")
                    e.printStackTrace()
                    ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                }

            }
            methode1.setOnClickListener {
                dialog.dismiss()
                forceClose()

                try {

                    System.err.println("workkkkkkkkk 4 ")

                    val sharedPrefsFor = SharedPrefsForInstagram(
                        myselectedActivity!!
                    )
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {

                        System.err.println(
                            "workkkkkkkkk 476 " + urlwithoutlettersqp + "____" + "ds_user_id=" + map.preferencE_USERID + "; sessionid=" + map.preferencE_SESSIONID
                        )

                        downloadInstagramImageOrVideodata_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID + "; sessionid=" + map.preferencE_SESSIONID
                        )
                    } else {
                        downloadInstagramImageOrVideodata(
                            urlwithoutlettersqp, ""
                        )
                        // downloadInstagramImageOrVideoResOkhttpM2(urlwithoutlettersqp_noa)
                        //downloadInstagramImageOrVideo_tikinfApi(urlwithoutlettersqp)
                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialogFrag()
                    System.err.println("workkkkkkkkk 5")
                    e.printStackTrace()
                    ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                }


            }
            methode2.setOnClickListener {
                dialog.dismiss()
                forceClose()

                try {
                    System.err.println("workkkkkkkkk 4 ")

                    val sharedPrefsFor = SharedPrefsForInstagram(
                        myselectedActivity!!
                    )
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {

                        System.err.println(
                            "workkkkkkkkk 476 " + urlwithoutlettersqp + "____" + "ds_user_id=" + map.preferencE_USERID + "; sessionid=" + map.preferencE_SESSIONID
                        )

                        downloadInstagramImageOrVideodata_old_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID + "; sessionid=" + map.preferencE_SESSIONID
                        )
                    } else {
                        downloadInstagramImageOrVideoResponseOkhttp(
                            urlwithoutlettersqp_noa
                        )
                        // downloadInstagramImageOrVideoResponseOkhttp(urlwithoutlettersqp_noa)
                        // downloadInstagramImageOrVideoResOkhttpM2(urlwithoutlettersqp_noa)
                        //downloadInstagramImageOrVideo_tikinfApi(urlwithoutlettersqp)
                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialogFrag()
                    System.err.println("workkkkkkkkk 5")
                    e.printStackTrace()
                    ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                }

            }

            //TODO only working for videos
            methode3.setOnClickListener {
                dialog.dismiss()
                forceClose()

                try {
                    System.err.println("workkkkkkkkk 4")
                    val sharedPrefsFor = SharedPrefsForInstagram(
                        myselectedActivity!!
                    )
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {
                        System.err.println("workkkkkkkkk m2 5.2")
                        downloadInstagramImageOrVideodata_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID + "; sessionid=" + map.preferencE_SESSIONID
                        )
                    } else {
                        System.err.println("workkkkkkkkk 4.5")
//                        downloadInstagramImageOrVideodata(
//                            urlwithoutlettersqp,
//                            ""
//                        )

                        dismissMyDialogFrag()
                        val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                        intent.putExtra("myurlis", urlwithoutlettersqp_noa)
                        startActivityForResult(intent, 2)

                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialogFrag()
                    System.err.println("workkkkkkkkk 5.1")
                    e.printStackTrace()
                    ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                }

            }

            methode4.setOnClickListener {
                dialog.dismiss()
                forceClose()

                try {
                    loginSnapIntaWeb(urlwithoutlettersqp_noa)
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissMyDialogErrorToastFrag()
                }

            }
            methode5.setOnClickListener {
                dialog.dismiss()
                forceClose()

                try {
                    loginDownloadgram(urlwithoutlettersqp_noa)
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissMyDialogErrorToastFrag()
                }

            }
            methode6.setOnClickListener {
                dialog.dismiss()
                forceClose()

                dismissMyDialogFrag()

                var myurl = urlwithoutlettersqp_noa
                try {
                    myurl = iUtils.extractUrls(myurl)[0]
                } catch (_: Exception) {
                }
                DownloadVideosMain.Start(myselectedActivity, myurl.trim(), false)
                Log.e("downloadFileName12", myurl.trim())
            }
            dig_btn_cancel.setOnClickListener {
                dialog.dismiss()
                dismissMyDialogFrag()
            }
            dialog.setCancelable(false)
            dialog.show()
        }


    }

    @Keep
    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    private fun loginDownloadgram(urlwithoutlettersqp: String) {
        try {
            webViewInsta = WebView(myselectedActivity!!)
            webViewInsta.clearCache(true)
            val cookieManager1 = CookieManager.getInstance()
            cookieManager1.setAcceptThirdPartyCookies(webViewInsta, true)
            cookieManager1.setAcceptCookie(true)
            cookieManager1.acceptCookie()
            webViewInsta.clearFormData()
            webViewInsta.settings.saveFormData = true
            val j = iUtils.getRandomNumber(iUtils.UserAgentsListLogin.size)
            webViewInsta.settings.userAgentString = iUtils.UserAgentsListLogin.get(j)
            webViewInsta.settings.allowFileAccess = true
            webViewInsta.settings.javaScriptEnabled = true
            webViewInsta.settings.defaultTextEncodingName = "UTF-8"
            webViewInsta.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            webViewInsta.settings.builtInZoomControls = true
            webViewInsta.settings.setSupportZoom(true)
            webViewInsta.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

            webViewInsta.settings.useWideViewPort = true
            webViewInsta.settings.domStorageEnabled = true
            webViewInsta.settings.loadWithOverviewMode = true
            webViewInsta.settings.loadsImagesAutomatically = true
            webViewInsta.settings.blockNetworkImage = false
            webViewInsta.settings.blockNetworkLoads = false
            val isdownloadstarted = booleanArrayOf(false)
            val isdownloadclicked = booleanArrayOf(false)
            Log.e(
                "workkkk sel", "binding!!.loggedIn "
            )
            val handler2 = Handler()
            var listoflink_videos = ArrayList<String>()
            var listoflink_photos = ArrayList<String>()
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            webViewInsta.webViewClient = object : WebViewClient() {

                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    Log.e(
                        "workkkk url", "binding!!.progressBar reciveing data0 $url"
                    )
                    try {
                        val jsscript =
                            ("javascript:(function() { " + "var ell = document.getElementById('url');" // + "ell[1].value ='" + "keepsaveit" + "';"
                                    // + "ell[2].value ='" + "keepsaveit12345" + "';"
                                    + "ell.value ='" + urlwithoutlettersqp + "';" + "var bbb = document.getElementById('submit');" + "bbb.click();" + "})();")
                        if (!isdownloadclicked[0]) {
                            webViewInsta.evaluateJavascript(jsscript) { value ->
                                isdownloadclicked[0] = true
                                Log.e(
                                    "workkkk0", "binding!!.progressBar reciveing data1 $value"
                                )
                                try {
                                    handler2.postDelayed(object : Runnable {
                                        override fun run() {
                                            myselectedActivity!!.runOnUiThread {
                                                webViewInsta.evaluateJavascript(
                                                    "(function() { " + "var text='';" + "var aaa = document.getElementsByTagName('a');" + "for (var i = 0; i < aaa.length; i++) {" +  // "if(aaa[i].getAttribute('href').includes('https://scontent') || aaa[i].getAttribute('href').includes('https://instagram')){" +
                                                            "  text += aaa[i].getAttribute('href')+'@_@';" +  //  "}" +
                                                            "}" + "var withoutLast3 = text.slice(0, -3);" + "return withoutLast3+''; })();"
                                                ) { html ->
                                                    Log.e(
                                                        "workkkk0",
                                                        "binding!!.progressBar reciveing data2 $html"
                                                    )
                                                    val sss: List<String> = html.split("@_@")
                                                    for (i in sss) {
                                                        if (i.contains("https://dl.downloadgram.org") || i.contains(
                                                                "scontent"
                                                            ) || i.contains(
                                                                "cdninstagram"
                                                            )
                                                        ) {
                                                            if (i.contains(".jpg")) {
                                                                Log.d(
                                                                    "HTMLimg", "" + i
                                                                )
                                                                listoflink_photos.add(i)
                                                            } else if (i.contains(".mp4") || i.startsWith(
                                                                    "https://dl.downloadgram.org"
                                                                )
                                                            ) {
                                                                Log.d(
                                                                    "HTML vid", "" + i
                                                                )
                                                                listoflink_videos.add(i)
                                                            }
                                                        }
                                                    }
                                                    if (!isdownloadstarted[0] && (listoflink_videos.size > 0 || listoflink_photos.size > 0)) {
                                                        dismissMyDialogFrag()
                                                        handler2.removeCallbacksAndMessages(
                                                            null
                                                        )
                                                        isdownloadstarted[0] = true
                                                        if (listoflink_videos != null || listoflink_photos != null || listoflink_videos.size > 0 || listoflink_photos.size > 0) {

                                                            listoflink_videos =
                                                                iUtils.removeDuplicates(
                                                                    listoflink_videos
                                                                )
                                                            listoflink_photos =
                                                                iUtils.removeDuplicates(
                                                                    listoflink_photos
                                                                )



                                                            for (i in listoflink_videos) {


                                                                DownloadFileMain.startDownloading(
                                                                    myselectedActivity!!,
                                                                    i,
                                                                    myInstaUsername + "_instagram_" + System.currentTimeMillis() + "_" + iUtils.getVideoFilenameFromURL(
                                                                        i
                                                                    ),
                                                                    ".mp4"
                                                                )

                                                            }

                                                            for (i in listoflink_photos) {

                                                                DownloadFileMain.startDownloading(
                                                                    myselectedActivity!!,
                                                                    i,
                                                                    myInstaUsername + "_instagram_" + System.currentTimeMillis() + "_" + iUtils.getImageFilenameFromURL(
                                                                        i
                                                                    ),
                                                                    ".png"
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            handler2.postDelayed(this, 3500)
                                        }
                                    }, 3000)
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                    dismissMyDialogErrorToastFrag()
                                }
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        dismissMyDialogErrorToastFrag()
                    }
                }

                override fun onLoadResource(view: WebView, url: String) {
                    super.onLoadResource(view, url)
                }

                override fun shouldInterceptRequest(
                    view: WebView, request: WebResourceRequest
                ): WebResourceResponse? {
                    return super.shouldInterceptRequest(view, request)
                }

                @Deprecated("Deprecated in Java")
                override fun shouldInterceptRequest(
                    view: WebView?, url: String?
                ): WebResourceResponse? {
                    if (url!!.contains("google") || url.contains("facebook")) {
                        val textStream: InputStream = ByteArrayInputStream("".toByteArray())
                        return WebResourceResponse("text/plain", "UTF-8", textStream)
                    }
                    return super.shouldInterceptRequest(view, url)
                }

                @Deprecated("Deprecated in Java")
                override fun onReceivedError(
                    view: WebView, errorCode: Int, description: String, failingUrl: String
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    dismissMyDialogErrorToastFrag()
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView, request: WebResourceRequest
                ): Boolean {
                    // view.loadUrl(urlwithoutlettersqp);
                    return false
                }
            }
            CookieSyncManager.createInstance(myselectedActivity)
            webViewInsta.loadUrl("https://downloadgram.org/")
        } catch (e: java.lang.Exception) {
            dismissMyDialogErrorToastFrag()
            e.printStackTrace()
        }
    }

    @Keep
    fun downloadInstagramImageOrVideodata_old_withlogin(URL: String?, Cookie: String?) {
        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)
        object : Thread() {
            override fun run() {
                Looper.prepare()
                val client = OkHttpClient().newBuilder().build()
                val request: Request =
                    Request.Builder().url(URL!!).method("GET", null).addHeader("Cookie", Cookie!!)
                        .addHeader(
                            "User-Agent", iUtils.UserAgentsList[j]
                        ).build()
                try {
                    val response = client.newCall(request).execute()

                    if (response.code == 200) {

                        val ress = response.body!!.string()
                        println("working runed \t Value: $ress")

                        try {
                            val listType: Type = object : TypeToken<ModelInstaWithLogin?>() {}.type
                            val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
                                ress, listType
                            )
                            println("workkkkk777 " + modelInstagramResponse.items[0].code!!)


                            if (modelInstagramResponse.items[0].mediaType == 8) {


                                println("workkkkk777 mediacount " + modelInstagramResponse.items[0].carouselMediaCount)


                                myInstaUsername =
                                    modelInstagramResponse.items[0].user.username + "_"

                                val modelGetEdgetoNode = modelInstagramResponse.items[0]
                                val modelEdNodeArrayList: List<CarouselMedia> =
                                    modelGetEdgetoNode.carouselMedia
                                for (i in modelEdNodeArrayList.indices) {
                                    if (modelEdNodeArrayList[i].mediaType == 2) {
                                        myVideoUrlIs =
                                            modelEdNodeArrayList[i].videoVersions[0].geturl()
                                        DownloadFileMain.startDownloading(
                                            myselectedActivity!!,
                                            myVideoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myVideoUrlIs
                                            ),
                                            ".mp4"
                                        )
                                        // etText.setText("")
                                        try {
                                            dismissMyDialogFrag()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].imageVersions2.candidates[0].geturl()
                                        DownloadFileMain.startDownloading(
                                            myselectedActivity!!,
                                            myPhotoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myPhotoUrlIs
                                            ),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        try {
                                            dismissMyDialogFrag()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        // etText.setText("")
                                    }
                                }
                            } else {
                                val modelGetEdgetoNode = modelInstagramResponse.items[0]
                                myInstaUsername =
                                    modelInstagramResponse.items[0].user.username + "_"

                                if (modelGetEdgetoNode.mediaType == 2) {
                                    myVideoUrlIs = modelGetEdgetoNode.videoVersions[0].geturl()
                                    DownloadFileMain.startDownloading(
                                        myselectedActivity!!,
                                        myVideoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    try {
                                        dismissMyDialogFrag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                                    DownloadFileMain.startDownloading(
                                        myselectedActivity!!,
                                        myPhotoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myPhotoUrlIs
                                        ),

                                        ".png"
                                    )
                                    try {
                                        dismissMyDialogFrag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myPhotoUrlIs = ""
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            System.err.println("workkkkkkkkk 5nny errrr " + e.message)
                            try {
                                try {
                                    System.err.println("workkkkkkkkk 4")

                                    val sharedPrefsFor = SharedPrefsForInstagram(
                                        myselectedActivity!!
                                    )
                                    val map = sharedPrefsFor.preference
                                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {
                                        System.err.println("workkkkkkkkk 5.5")
                                        downloadInstagramImageOrVideodata(
                                            URL,
                                            "ds_user_id=" + map.preferencE_USERID + "; sessionid=" + map.preferencE_SESSIONID
                                        )
                                    } else {
                                        dismissMyDialogFrag()
                                        System.err.println("workkkkkkkkk 5.1")
                                        e.printStackTrace()
                                        ShowToast(
                                            myselectedActivity!!, getString(R.string.error_occ)
                                        )
                                    }
                                } catch (e: java.lang.Exception) {
                                    dismissMyDialogFrag()
                                    System.err.println("workkkkkkkkk 5.1")
                                    e.printStackTrace()
                                    ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                try {
                                    myselectedActivity!!.runOnUiThread {
                                        dismissMyDialogFrag()
                                        if (!myselectedActivity!!.isFinishing) {
                                            val alertDialog =
                                                AlertDialog.Builder(myselectedActivity!!).create()
                                            alertDialog.setTitle(getString(R.string.logininsta))
                                            alertDialog.setMessage(getString(R.string.urlisprivate))
                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_POSITIVE,
                                                getString(R.string.logininsta)
                                            ) { dialog, _ ->
                                                dialog.dismiss()
                                                val intent = Intent(
                                                    myselectedActivity!!,
                                                    InstagramLoginActivity::class.java
                                                )
                                                startActivityForResult(intent, 200)
                                            }
                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_NEGATIVE,
                                                getString(R.string.cancel)
                                            ) { dialog, _ ->
                                                dialog.dismiss()
                                            }
                                            alertDialog.show()
                                        }
                                    }
                                } catch (_: Exception) {

                                }
                            }
                        }
                    } else {
                        object : Thread() {
                            override fun run() {

                                val client = OkHttpClient().newBuilder().build()
                                val request: Request =
                                    Request.Builder().url(URL).method("GET", null)
                                        .addHeader("Cookie", iUtils.myInstagramTempCookies)
                                        .addHeader(
                                            "User-Agent",
                                            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36"
                                        ).build()
                                try {
                                    val response1: Response = client.newCall(request).execute()

                                    if (response1.code == 200) {

                                        try {
                                            val listType: Type =
                                                object : TypeToken<ModelInstaWithLogin?>() {}.type
                                            val modelInstagramResponse: ModelInstaWithLogin =
                                                Gson().fromJson(
                                                    response.body!!.string(), listType
                                                )
                                            if (modelInstagramResponse.items[0].mediaType == 8) {
                                                myInstaUsername =
                                                    modelInstagramResponse.items[0].user.username + "_"

                                                val modelGetEdgetoNode =
                                                    modelInstagramResponse.items[0]
                                                val modelEdNodeArrayList: List<CarouselMedia> =
                                                    modelGetEdgetoNode.carouselMedia
                                                for (i in modelEdNodeArrayList.indices) {
                                                    if (modelEdNodeArrayList[i].mediaType == 2) {
                                                        myVideoUrlIs =
                                                            modelEdNodeArrayList[i].videoVersions[0].geturl()
                                                        DownloadFileMain.startDownloading(
                                                            myselectedActivity!!,
                                                            myVideoUrlIs,
                                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                                myVideoUrlIs
                                                            ),
                                                            ".mp4"
                                                        )
                                                        // etText.setText("")
                                                        try {
                                                            dismissMyDialogFrag()
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                        myVideoUrlIs = ""
                                                    } else {
                                                        myPhotoUrlIs =
                                                            modelEdNodeArrayList[i].imageVersions2.candidates[0].geturl()
                                                        DownloadFileMain.startDownloading(
                                                            myselectedActivity!!,
                                                            myPhotoUrlIs,
                                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                                myPhotoUrlIs
                                                            ),
                                                            ".png"
                                                        )
                                                        myPhotoUrlIs = ""
                                                        try {
                                                            dismissMyDialogFrag()
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                        // etText.setText("")
                                                    }
                                                }
                                            } else {
                                                myInstaUsername =
                                                    modelInstagramResponse.items[0].user.username + "_"

                                                val modelGetEdgetoNode =
                                                    modelInstagramResponse.items[0]
                                                if (modelGetEdgetoNode.mediaType == 2) {
                                                    myVideoUrlIs =
                                                        modelGetEdgetoNode.videoVersions[0].geturl()
                                                    DownloadFileMain.startDownloading(
                                                        myselectedActivity!!,
                                                        myVideoUrlIs,
                                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                            myVideoUrlIs
                                                        ),
                                                        ".mp4"
                                                    )
                                                    try {
                                                        dismissMyDialogFrag()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                    }
                                                    myVideoUrlIs = ""
                                                } else {
                                                    myPhotoUrlIs =
                                                        modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                                                    DownloadFileMain.startDownloading(
                                                        myselectedActivity!!,
                                                        myPhotoUrlIs,
                                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                            myPhotoUrlIs
                                                        ),
                                                        ".png"
                                                    )
                                                    try {
                                                        dismissMyDialogFrag()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                    }
                                                    myPhotoUrlIs = ""
                                                }
                                            }
                                        } catch (e: java.lang.Exception) {
                                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)
                                            e.printStackTrace()
                                            try {
                                                myselectedActivity!!.runOnUiThread {
                                                    dismissMyDialogFrag()

                                                    if (!myselectedActivity!!.isFinishing) {
                                                        val alertDialog =
                                                            AlertDialog.Builder(myselectedActivity!!)
                                                                .create()
                                                        alertDialog.setTitle(getString(R.string.logininsta))
                                                        alertDialog.setMessage(getString(R.string.urlisprivate))
                                                        alertDialog.setButton(
                                                            AlertDialog.BUTTON_POSITIVE,
                                                            getString(R.string.logininsta)
                                                        ) { dialog, _ ->
                                                            dialog.dismiss()
                                                            val intent = Intent(
                                                                myselectedActivity!!,
                                                                InstagramLoginActivity::class.java
                                                            )
                                                            startActivityForResult(intent, 200)
                                                        }
                                                        alertDialog.setButton(
                                                            AlertDialog.BUTTON_NEGATIVE,
                                                            getString(R.string.cancel)
                                                        ) { dialog, _ ->
                                                            dialog.dismiss()
                                                        }
                                                        alertDialog.show()
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }
                                    } else {
                                        System.err.println("workkkkkkkkk 6bbb errrr ")
                                        myselectedActivity!!.runOnUiThread {
                                            dismissMyDialogFrag()

                                            if (!myselectedActivity!!.isFinishing) {
                                                val alertDialog =
                                                    AlertDialog.Builder(myselectedActivity!!)
                                                        .create()
                                                alertDialog.setTitle(getString(R.string.logininsta))
                                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_POSITIVE,
                                                    getString(R.string.logininsta)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()
                                                    val intent = Intent(
                                                        myselectedActivity!!,
                                                        InstagramLoginActivity::class.java
                                                    )
                                                    startActivityForResult(intent, 200)
                                                }
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_NEGATIVE,
                                                    getString(R.string.cancel)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()
                                                }
                                                alertDialog.show()
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }.start()
                    }
                } catch (e: Exception) {
                    try {
                        println("response1122334455:   " + "Failed1 " + e.message)
                        dismissMyDialogFrag()
                    } catch (_: Exception) {

                    }
                }
            }
        }.start()
    }


    @Keep
    fun downloadInstagramImageOrVideodata_withlogin(URL: String?, Cookie: String?) {/*instagram product types
        * product_type
        *
        * igtv "media_type": 2
        * carousel_container  "media_type": 8
        * clips  "media_type": 2
        * feed   "media_type": 1
        * */

        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)

        var Cookie = Cookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        val apiService: RetrofitApiInterface = RetrofitClient.getClient()

        val callResult: Call<JsonObject> = apiService.getInstagramData(
            URL, Cookie, iUtils.UserAgentsList[j]
        )
        callResult.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>, response: retrofit2.Response<JsonObject?>
            ) {

                try {
                    val listType: Type = object : TypeToken<ModelInstaWithLogin?>() {}.type
                    val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
                        response.body(), listType
                    )
                    println("workkkkk777 " + modelInstagramResponse.items[0].code!!)

                    if (modelInstagramResponse.items[0].mediaType == 8) {
                        myInstaUsername = modelInstagramResponse.items[0].user.username + "_"

                        val modelGetEdgetoNode = modelInstagramResponse.items[0]

                        val modelEdNodeArrayList: List<CarouselMedia> =
                            modelGetEdgetoNode.carouselMedia
                        for (i in modelEdNodeArrayList.indices) {

                            System.err.println("workkkkkkkkklogin issue " + modelEdNodeArrayList[i].mediaType)


                            if (modelEdNodeArrayList[i].mediaType == 2) {
                                System.err.println("workkkkkkkkklogin issue vid " + modelEdNodeArrayList[i].videoVersions[0].geturl())


                                myVideoUrlIs = modelEdNodeArrayList[i].videoVersions[0].geturl()
                                DownloadFileMain.startDownloading(
                                    myselectedActivity!!,
                                    myVideoUrlIs,
                                    myInstaUsername + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                    ".mp4"
                                )
                                // etText.setText("")
                                try {
                                    dismissMyDialogFrag()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                myVideoUrlIs = ""
                            } else {

                                System.err.println("workkkkkkkkklogin issue img " + modelEdNodeArrayList[i].imageVersions2.candidates[0].geturl())


                                myPhotoUrlIs =
                                    modelEdNodeArrayList[i].imageVersions2.candidates[0].geturl()
                                DownloadFileMain.startDownloading(
                                    myselectedActivity!!,
                                    myPhotoUrlIs,
                                    myInstaUsername + iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                    ".png"
                                )
                                myPhotoUrlIs = ""
                                try {
                                    dismissMyDialogFrag()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                // etText.setText("")
                            }
                        }
                    } else {
                        val modelGetEdgetoNode = modelInstagramResponse.items[0]
                        myInstaUsername = modelInstagramResponse.items[0].user.username + "_"

                        if (modelGetEdgetoNode.mediaType == 2) {
                            myVideoUrlIs = modelGetEdgetoNode.videoVersions[0].geturl()
                            DownloadFileMain.startDownloading(
                                myselectedActivity!!,
                                myVideoUrlIs,
                                myInstaUsername + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                ".mp4"
                            )
                            try {
                                dismissMyDialogFrag()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            myVideoUrlIs = ""
                        } else {
                            myPhotoUrlIs = modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                            DownloadFileMain.startDownloading(
                                myselectedActivity!!,
                                myPhotoUrlIs,
                                myInstaUsername + iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                ".png"
                            )
                            try {
                                dismissMyDialogFrag()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            myPhotoUrlIs = ""
                        }
                    }

                } catch (e: java.lang.Exception) {
                    System.err.println("workkkkkkkkk 5nn errrr " + e.message)



                    try {

                        try {
                            System.err.println("workkkkkkkkk 4")

                            val sharedPrefsFor = SharedPrefsForInstagram(
                                myselectedActivity!!
                            )
                            val map = sharedPrefsFor.preference
                            if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {
                                System.err.println("workkkkkkkkk 5.2")
                                downloadInstagramImageOrVideodata_old(
                                    URL,
                                    "ds_user_id=" + map.preferencE_USERID + "; sessionid=" + map.preferencE_SESSIONID
                                )
                            } else {
                                dismissMyDialogFrag()
                                System.err.println("workkkkkkkkk 5.1")
                                e.printStackTrace()
                                ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                            }
                        } catch (e: java.lang.Exception) {
                            dismissMyDialogFrag()
                            System.err.println("workkkkkkkkk 5.1")
                            e.printStackTrace()
                            ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                        }

                    } catch (e: Exception) {

                        e.printStackTrace()
                        myselectedActivity!!.runOnUiThread {
                            dismissMyDialogFrag()
                            if (!myselectedActivity!!.isFinishing) {
                                val alertDialog = AlertDialog.Builder(myselectedActivity!!).create()
                                alertDialog.setTitle(getString(R.string.logininsta))
                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(
                                        myselectedActivity!!, InstagramLoginActivity::class.java
                                    )
                                    startActivityForResult(intent, 200)
                                }
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                alertDialog.show()
                            }
                        }
                    }


                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                println("response1122334455:   " + "Failed0")
                try {
                    dismissMyDialogFrag()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                myselectedActivity!!.runOnUiThread {
                    iUtils.ShowToastError(
                        myselectedActivity,
                        myselectedActivity!!.resources.getString(R.string.somthing)
                    )
                }

            }
        })
    }

    fun dismissMyDialogErrorToastFrag() {
        try {
            if (myselectedActivity != null) {
                myselectedActivity?.runOnUiThread {
                    if (!(myselectedActivity as Activity).isFinishing && progressDralogGenaratinglink != null && progressDralogGenaratinglink.isShowing) {
                        progressDralogGenaratinglink.dismiss()
                        myselectedActivity!!.runOnUiThread {
                            iUtils.ShowToastError(
                                myselectedActivity,
                                myselectedActivity!!.resources.getString(R.string.somthing)
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Keep
    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    fun loginSnapIntaWeb(urlwithoutlettersqp: String) {
        try {

            webViewInsta = WebView(myselectedActivity!!)

            webViewInsta.clearCache(true)

            val cookieManager1 = CookieManager.getInstance()

            cookieManager1.setAcceptThirdPartyCookies(webViewInsta, true)
            cookieManager1.setAcceptCookie(true)
            cookieManager1.acceptCookie()


            webViewInsta.clearFormData()
            webViewInsta.settings.saveFormData = true

            val j = iUtils.getRandomNumber(iUtils.UserAgentsListLogin.size)

            webViewInsta.settings.userAgentString = iUtils.UserAgentsListLogin[j]
            webViewInsta.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            webViewInsta.settings.allowFileAccess = true
            webViewInsta.settings.javaScriptEnabled = true
            webViewInsta.settings.defaultTextEncodingName = "UTF-8"
            webViewInsta.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            webViewInsta.settings.databaseEnabled = true
            webViewInsta.settings.builtInZoomControls = false
            webViewInsta.settings.setSupportZoom(true)
            webViewInsta.settings.useWideViewPort = true
            webViewInsta.settings.domStorageEnabled = true
            webViewInsta.settings.loadWithOverviewMode = true
            webViewInsta.settings.loadsImagesAutomatically = true
            webViewInsta.settings.blockNetworkImage = false
            webViewInsta.settings.blockNetworkLoads = false
            webViewInsta.settings.defaultTextEncodingName = "UTF-8"

            var isdownloadstarted = false

            Log.e(
                "workkkk sel", "binding!!.loggedIn "
            )


            val handler2 = Handler()
            val listoflink_videos = ArrayList<String>()
            val listoflink_photos = ArrayList<String>()


            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()

            webViewInsta.webViewClient = object : WebViewClient() {

                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(
                    webView1: WebView, url: String?
                ): Boolean {
                    webView1.loadUrl(url!!)
                    return true
                }

                override fun onLoadResource(
                    webView: WebView?, str: String?
                ) {
                    super.onLoadResource(webView, str)
                }


                override fun onPageFinished(
                    webView: WebView, str: String?
                ) {
                    super.onPageFinished(webView, str)
                    Log.e(
                        "workkkk url", "binding!!.progressBar reciveing datainsta $str"
                    )

                    try {


                        val jsscript =
                            ("javascript:(function() { " + "var ell = document.getElementsByTagName('input');"
                                    // + "ell[1].value ='" + "keepsaveit" + "';"
                                    // + "ell[2].value ='" + "keepsaveit12345" + "';"

                                    + "ell[0].value ='" + urlwithoutlettersqp + "';"

                                    + "var bbb = document.getElementsByTagName('button');" + "bbb[5].click();" + "})();")



                        webViewInsta.evaluateJavascript(
                            jsscript
                        ) {

                            Log.e(
                                "workkkk0", "binding!!.progressBar reciveing data $it"
                            )


                            try {
                                handler2.postDelayed(object : Runnable {
                                    override fun run() {
                                        myselectedActivity?.runOnUiThread {


                                            webViewInsta.evaluateJavascript(
                                                "(function() { " + "var text='';" + "var aaa = document.getElementsByTagName('a');" + "for (var i = 0; i < aaa.length; i++) {" +
                                                        // "if(aaa[i].getAttribute('href').includes('https://scontent') || aaa[i].getAttribute('href').includes('https://instagram')){" +
                                                        "  text += aaa[i].getAttribute('href')+'@_@';" +
                                                        //  "}" +
                                                        "}" + "var withoutLast3 = text.slice(0, -3);" + "return withoutLast3+''; })();"
                                            ) { html ->

                                                Log.e(
                                                    "workkkk0",
                                                    "binding!!.progressBar reciveing data $html"
                                                )

                                                //                                        val unescapedString =
                                                //                                            Parser.unescapeEntities(html, true)

                                                //   var dsd :Document= Jsoup.parse(unescapedString)
                                                //                                        val document = Jsoup.parse(html)

//https://snapxcdn.com/dl/v1?token=


                                                webViewInsta.evaluateJavascript(
                                                    "javascript:(function() { " + "var bbb = document.getElementsByTagName(\"button\");" + "bbb[2].click();" + "})();"
                                                ) { value ->
                                                    Log.e(
                                                        "workkkk0",
                                                        "binding!!.progressBar reciveing data3 $value"
                                                    )
                                                }


                                                val sss = html.split("@_@")
                                                for (i in sss) {


                                                    if (i.contains("?token=") && !i.contains(
                                                            "/instagram-story-download"
                                                        ) && !i.contains(
                                                            "/instagram-reels-video-download"
                                                        ) && !i.contains("/instagram-photo-download") && !i.contains(
                                                            "/instagram-story-viewer"
                                                        )
                                                    ) {
                                                        Log.d("HTML vid", "" + i)

                                                        listoflink_videos.add(i)


                                                    }

                                                    if (i.contains("instagram") && !i.contains(
                                                            "?token="
                                                        ) && !i.contains("/instagram-story-download") && !i.contains(
                                                            "/instagram-reels-video-download"
                                                        ) && !i.contains("/instagram-photo-download") && !i.contains(
                                                            "/instagram-story-viewer"
                                                        )
                                                    ) {
                                                        Log.d("HTMLimg", "" + i)


                                                        listoflink_photos.add(i)
                                                    }
                                                }


                                                if (!isdownloadstarted && (listoflink_videos.size > 0 || listoflink_photos.size > 0)) {

                                                    dismissMyDialogFrag()


                                                    handler2.removeCallbacksAndMessages(
                                                        null
                                                    )

                                                    isdownloadstarted = true

                                                    if ((listoflink_videos != null || listoflink_photos != null) || (listoflink_videos.size > 0 || listoflink_photos.size > 0)) {


                                                        for (i in listoflink_videos) {


                                                            DownloadFileMain.startDownloading(
                                                                myselectedActivity!!,
                                                                i,
                                                                myInstaUsername + "_instagram_" + System.currentTimeMillis() + "_" + iUtils.getVideoFilenameFromURL(
                                                                    i
                                                                ),
                                                                ".mp4"
                                                            )

                                                        }

                                                        for (i in listoflink_photos) {

                                                            DownloadFileMain.startDownloading(
                                                                myselectedActivity!!,
                                                                i,
                                                                myInstaUsername + "_instagram_" + System.currentTimeMillis() + "_" + iUtils.getImageFilenameFromURL(
                                                                    i
                                                                ),
                                                                ".png"
                                                            )
                                                        }
                                                    }

                                                } else {

//                                                    handler2.removeCallbacksAndMessages(
//                                                        null
//                                                    )
//
//                                                    myselectedActivity?.runOnUiThread {
//
//                                                        progressDralogGenaratinglink.setMessage(
//                                                            "Please Wait"
//                                                        )
//                                                    }
//                                                    Log.d(
//                                                        "HTML nolink fould",
//                                                        ""
//                                                    )
//
//                                                    try {
//                                                        System.err.println("workkkkkkkkk 4")
//
//                                                        val urlwithoutlettersqp2 =
//                                                            "$urlwithoutlettersqp?__a=1&__d=dis"
//
//
//                                                        System.err.println("workkkkkkkkk 4.5")
//
//                                                        downloadInstagramImageOrVideodata(
//                                                            urlwithoutlettersqp2,
//                                                            iUtils.myInstagramTempCookies
//                                                        )
//
//
//                                                    } catch (e: java.lang.Exception) {
//                                                        dismissMyDialogErrortoastFrag()
//                                                        System.err.println("workkkkkkkkk 5.1")
//                                                        e.printStackTrace()
//
//
//                                                    }


                                                }


                                            }

                                        }


                                        handler2.postDelayed(this, 2000)
                                    }
                                }, 2000)
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                                dismissMyDialogErrorToastFrag()

                            }


                        }


                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        dismissMyDialogErrorToastFrag()

                    }
                }


                @Deprecated("Deprecated in Java")
                override fun onReceivedError(
                    webView: WebView?, i: Int, str: String?, str2: String?
                ) {
                    super.onReceivedError(webView, i, str, str2)
                    dismissMyDialogErrorToastFrag()

                }

                override fun shouldInterceptRequest(
                    webView: WebView?, webResourceRequest: WebResourceRequest?
                ): WebResourceResponse? {
                    return super.shouldInterceptRequest(
                        webView, webResourceRequest
                    )
                }

                @Deprecated("Deprecated in Java")
                override fun shouldInterceptRequest(
                    view: WebView?, url: String?
                ): WebResourceResponse? {
                    if (url!!.contains("google") || url.contains("facebook")) {
                        val textStream: InputStream = ByteArrayInputStream("".toByteArray())
                        return WebResourceResponse("text/plain", "UTF-8", textStream)
                    }
                    return super.shouldInterceptRequest(view, url)
                }


                override fun shouldOverrideUrlLoading(
                    webView: WebView?, webResourceRequest: WebResourceRequest?
                ): Boolean {
                    return super.shouldOverrideUrlLoading(
                        webView, webResourceRequest
                    )
                }
            }


            CookieSyncManager.createInstance(myselectedActivity)
            webViewInsta.loadUrl("https://snapinsta.app/")


        } catch (e: java.lang.Exception) {
            dismissMyDialogErrorToastFrag()
            System.err.println("workkkkkkkkk 5" + e.localizedMessage)
            e.printStackTrace()

        }
    }


    @Keep
    fun downloadInstagramImageOrVideoResponseOkhttp(URL: String?) {

//TODO check
//        Unirest.config()
//            .socketTimeout(500)
//            .connectTimeout(1000)
//            .concurrency(10, 5)
//            .proxy(Proxy("https://proxy"))
//            .setDefaultHeader("Accept", "application/json")
//            .followRedirects(false)
//            .enableCookieManagement(false)
//            .addInterceptor(MyCustomInterceptor())

        object : Thread() {
            override fun run() {


                try {

                    val cookieJar: ClearableCookieJar = PersistentCookieJar(
                        SetCookieCache(), SharedPrefsCookiePersistor(myselectedActivity)
                    )

                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    // init OkHttpClient
                    val client: OkHttpClient =
                        OkHttpClient.Builder().cookieJar(cookieJar).addInterceptor(logging).build()

                    val request: Request =
                        Request.Builder().url("$URL?__a=1&__d=dis").method("GET", null).build()
                    val response = client.newCall(request).execute()

                    val ressd = response.body!!.string()

                    println("instaress_test $ressd")

                    var code = response.code
                    if (!ressd.contains("shortcode_media")) {
                        code = 400
                    }
                    if (code == 200) {


                        try {


                            val listType = object : TypeToken<ModelInstagramResponse?>() {}.type
                            val modelInstagramResponse: ModelInstagramResponse? =
                                GsonBuilder().create().fromJson<ModelInstagramResponse>(
                                    ressd, listType
                                )


                            if (modelInstagramResponse != null) {


                                if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                    val modelGetEdgetoNode: ModelGetEdgetoNode =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children

                                    val modelEdNodeArrayList: List<ModelEdNode> =
                                        modelGetEdgetoNode.modelEdNodes
                                    for (i in 0 until modelEdNodeArrayList.size) {
                                        if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                            myVideoUrlIs =
                                                modelEdNodeArrayList[i].modelNode.video_url


                                            DownloadFileMain.startDownloading(
                                                myselectedActivity!!,
                                                myVideoUrlIs,
                                                myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                    myVideoUrlIs
                                                ),
                                                ".mp4"
                                            )
                                            dismissMyDialogFrag()


                                            myVideoUrlIs = ""
                                        } else {
                                            myPhotoUrlIs =
                                                modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src

                                            DownloadFileMain.startDownloading(
                                                myselectedActivity!!,
                                                myPhotoUrlIs,
                                                myInstaUsername + iUtils.getImageFilenameFromURL(
                                                    myPhotoUrlIs
                                                ),
                                                ".png"
                                            )
                                            myPhotoUrlIs = ""
                                            dismissMyDialogFrag()
                                            // etText.setText("")
                                        }
                                    }
                                } else {
                                    val isVideo =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                                    if (isVideo) {
                                        myVideoUrlIs =
                                            modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url


                                        DownloadFileMain.startDownloading(
                                            myselectedActivity!!,
                                            myVideoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myVideoUrlIs
                                            ),
                                            ".mp4"
                                        )
                                        dismissMyDialogFrag()
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src


                                        DownloadFileMain.startDownloading(
                                            myselectedActivity!!,
                                            myPhotoUrlIs,
                                            myInstaUsername + iUtils.getImageFilenameFromURL(
                                                myPhotoUrlIs
                                            ),
                                            ".png"
                                        )
                                        dismissMyDialogFrag()
                                        myPhotoUrlIs = ""
                                    }
                                }


                            } else {
                                myselectedActivity!!.runOnUiThread {
                                    iUtils.ShowToastError(
                                        myselectedActivity,
                                        myselectedActivity!!.resources.getString(R.string.somthing)
                                    )
                                }

                                dismissMyDialogFrag()

                            }


                        } catch (e: Exception) {
                            myselectedActivity!!.runOnUiThread {
                                progressDralogGenaratinglink.setMessage("Method 1 failed trying method 2")
                            }
                            downloadInstagramImageOrVideoResOkhttpM2(URL!!)

                        }

                    } else {
                        myselectedActivity!!.runOnUiThread {
                            progressDralogGenaratinglink.setMessage("Method 1 failed trying method 2")
                        }
                        downloadInstagramImageOrVideoResOkhttpM2(URL!!)
                    }


                } catch (e: Throwable) {
                    e.printStackTrace()
                    println("The request has failed " + e.message)
                    myselectedActivity!!.runOnUiThread {
                        progressDralogGenaratinglink.setMessage("Method 1 failed trying method 2")
                    }
                    downloadInstagramImageOrVideoResOkhttpM2(URL!!)
                }
            }
        }.start()
    }

    @Keep
    fun downloadInstagramImageOrVideoResOkhttpM2(URL: String?) {


        try {


            val cookieJar: ClearableCookieJar = PersistentCookieJar(
                SetCookieCache(), SharedPrefsCookiePersistor(myselectedActivity)
            )

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            // init OkHttpClient
            val client: OkHttpClient =
                OkHttpClient.Builder().cookieJar(cookieJar).addInterceptor(logging).build()
            println("instaress_test $URL" + "embed/captioned/")
            val request: Request =
                Request.Builder().url(URL + "embed/captioned/").method("GET", null).build()
            val response = client.newCall(request).execute()

            val ss = response.body!!.string()

            println("instaress_test $ss")

            if (response.code == 200) {

                try {

                    val doc: Document = Jsoup.parse(ss)

                    myVideoUrlIs = doc.select("video").get(0).attr("src")



                    if (myVideoUrlIs != null && !myVideoUrlIs.equals("")) {


                        DownloadFileMain.startDownloading(
                            myselectedActivity!!,
                            myVideoUrlIs,
                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                myVideoUrlIs
                            ),
                            ".mp4"
                        )
                        dismissMyDialogFrag()


                        myVideoUrlIs = ""

                    } else {
                        myselectedActivity!!.runOnUiThread {
                            iUtils.ShowToastError(
                                myselectedActivity,
                                myselectedActivity!!.resources.getString(R.string.somthing)
                            )
                        }

                        dismissMyDialogFrag()


                    }


                } catch (e: Exception) {

                    myselectedActivity!!.runOnUiThread {
                        progressDralogGenaratinglink.setMessage("Method 2 failed trying method 3")
                    }
                    downloadInstagramImageOrVideo_tikinfApi(URL)


                }

            } else {

                myselectedActivity!!.runOnUiThread {
                    progressDralogGenaratinglink.setMessage("Method 2 failed trying method 3")
                }
                downloadInstagramImageOrVideo_tikinfApi(URL)
            }


        } catch (e: Throwable) {
            e.printStackTrace()
            println("The request has failed " + e.message)
            myselectedActivity!!.runOnUiThread {
                progressDralogGenaratinglink.setMessage("Method 2 failed trying method 3")
            }
            downloadInstagramImageOrVideo_tikinfApi(URL)
        }
    }

    private fun downloadInstagramImageOrVideo_tikinfApi(URL: String?) {
//        AndroidNetworking.get("http://tikdd.infusiblecoder.com/ini/ilog.php?url=$URL")
//            .setPriority(Priority.MEDIUM)
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    val myresws: String = response.toString()
//                    println("myresponseis111 eeee $myresws")
//
//                    try {
//                        val listType: Type =
//                            object : TypeToken<ModelInstaWithLogin?>() {}.type
//                        val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
//                            myresws,
//                            listType
//                        )
////                        System.out.println("workkkkk777 " + modelInstagramResponse.items.get(0).code())
//                        val usernameis = modelInstagramResponse.items[0].user.username
//
//
//                        if (modelInstagramResponse.items[0].mediaType == 8) {
//
//                            val modelGetEdgetoNode = modelInstagramResponse.items[0]
//
//                            val modelEdNodeArrayList: List<CarouselMedia> =
//                                modelGetEdgetoNode.carouselMedia
//                            for (i in 0 until modelEdNodeArrayList.size) {
//                                if (modelEdNodeArrayList[i].mediaType == 2) {
//                                    myVideoUrlIs =
//                                        modelEdNodeArrayList[i].videoVersions[0].geturl()
//                                    DownloadFileMain.startDownloading(
//                                        myselectedActivity,
//                                        myVideoUrlIs,
//                                        usernameis + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
//                                        ".mp4"
//                                    )
//
//
//                                    myVideoUrlIs = ""
//                                } else {
//                                    myPhotoUrlIs =
//                                        modelEdNodeArrayList[i].imageVersions2.candidates[0]
//                                            .geturl()
//                                    DownloadFileMain.startDownloading(
//                                        myselectedActivity,
//                                        myPhotoUrlIs,
//                                        usernameis + iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
//                                        ".png"
//                                    )
//
//                                    myPhotoUrlIs = ""
//
//                                    dismissMyDialogFrag()
//
//                                    // etText.setText("")
//                                }
//                            }
//                        } else {
//                            val modelGetEdgetoNode = modelInstagramResponse.items[0]
//                            if (modelGetEdgetoNode.mediaType == 2) {
//                                myVideoUrlIs =
//                                    modelGetEdgetoNode.videoVersions[0].geturl()
//                                DownloadFileMain.startDownloading(
//                                    myselectedActivity,
//                                    myVideoUrlIs,
//                                    usernameis + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
//                                    ".mp4"
//                                )
//
//                                myVideoUrlIs = ""
//                            } else {
//                                myPhotoUrlIs =
//                                    modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
//                                DownloadFileMain.startDownloading(
//                                    myselectedActivity,
//                                    myPhotoUrlIs,
//                                    usernameis + iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
//
//                                    ".png"
//                                )
//                                dismissMyDialogFrag()
//                                myPhotoUrlIs = ""
//                            }
//                        }
//
//                        dismissMyDialogFrag()
//
//                    } catch (e: java.lang.Exception) {
//                        e.printStackTrace()
//
//                        println("myresponseis111 try exp " + e.message)
//
//                        dismissMyDialogFrag()
//                        ShowToast(
//                            myselectedActivity,
//                            resources.getString(R.string.somthing)
//                        )
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    println("myresponseis111 exp " + error.message)
//                    dismissMyDialogFrag()
//                    ShowToast(
//                        myselectedActivity,
//                        resources.getString(R.string.somthing)
//                    )
//                }
//            })
//

        dismissMyDialogFrag()
        var myurl = URL
        try {
            myurl = iUtils.extractUrls(myurl)[0]
        } catch (_: Exception) {
        }
        DownloadVideosMain.Start(myselectedActivity, myurl!!.trim(), false)

//        myselectedActivity!!.runOnUiThread {
//
//            dismissMyDialogFrag()
//            ShowToast(
//                myselectedActivity,
//                resources.getString(R.string.somthing)
//            )
//        }


    }


    @Keep
    fun downloadInstagramImageOrVideodata_old(URL: String?, Cookie: String?) {
        val j = iUtils.getRandomNumber(iUtils.UserAgentsList.size)
        object : Thread() {
            override fun run() {
                Looper.prepare()
                val client = OkHttpClient().newBuilder().build()
                val request: Request =
                    Request.Builder().url(URL!!).method("GET", null).addHeader("Cookie", Cookie!!)
                        .addHeader(
                            "User-Agent", iUtils.UserAgentsList[j]
                        ).build()
                try {
                    val response = client.newCall(request).execute()

                    System.err.println("workkkkkkkkk 6 " + response.code)

                    if (response.code == 200) {

                        try {
                            val ressss = response.body!!.string()
                            System.err.println("workkkkkkkkk 6.78 $ressss")

                            val listType: Type =
                                object : TypeToken<ModelInstagramResponse?>() {}.type
                            val modelInstagramResponse: ModelInstagramResponse = Gson().fromJson(
                                ressss, listType
                            )

                            if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                val modelGetEdgetoNode: ModelGetEdgetoNode =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children
                                myInstaUsername =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                                val modelEdNodeArrayList: List<ModelEdNode> =
                                    modelGetEdgetoNode.modelEdNodes
                                for (i in modelEdNodeArrayList.indices) {
                                    if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                        myVideoUrlIs = modelEdNodeArrayList[i].modelNode.video_url
                                        DownloadFileMain.startDownloading(
                                            myselectedActivity!!,
                                            myVideoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myVideoUrlIs
                                            ),
                                            ".mp4"
                                        )
                                        // etText.setText("")
                                        try {
                                            dismissMyDialogFrag()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                        DownloadFileMain.startDownloading(
                                            myselectedActivity!!,
                                            myPhotoUrlIs,
                                            myInstaUsername + iUtils.getImageFilenameFromURL(
                                                myPhotoUrlIs
                                            ),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        try {
                                            dismissMyDialogFrag()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        // etText.setText("")
                                    }
                                }
                            } else {
                                val isVideo =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video

                                myInstaUsername =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                                if (isVideo) {
                                    myVideoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                    DownloadFileMain.startDownloading(
                                        myselectedActivity!!,
                                        myVideoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    try {
                                        dismissMyDialogFrag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                    DownloadFileMain.startDownloading(
                                        myselectedActivity!!,
                                        myPhotoUrlIs,
                                        myInstaUsername + iUtils.getImageFilenameFromURL(
                                            myPhotoUrlIs
                                        ),
                                        ".png"
                                    )
                                    try {
                                        dismissMyDialogFrag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myPhotoUrlIs = ""
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)
                            try {
                                try {
                                    System.err.println("workkkkkkkkk 4")
                                    downloadInstagramImageOrVideodata(
                                        URL, ""
                                    )
                                } catch (e: java.lang.Exception) {
                                    dismissMyDialogFrag()
                                    System.err.println("workkkkkkkkk 5.1")
                                    e.printStackTrace()
                                    ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                myselectedActivity!!.runOnUiThread {
                                    dismissMyDialogFrag()

                                    if (!myselectedActivity!!.isFinishing) {
                                        val alertDialog =
                                            AlertDialog.Builder(myselectedActivity!!).create()
                                        alertDialog.setTitle(getString(R.string.logininsta))
                                        alertDialog.setMessage(getString(R.string.urlisprivate))
                                        alertDialog.setButton(
                                            AlertDialog.BUTTON_POSITIVE,
                                            getString(R.string.logininsta)
                                        ) { dialog, _ ->
                                            dialog.dismiss()
                                            val intent = Intent(
                                                myselectedActivity!!,
                                                InstagramLoginActivity::class.java
                                            )
                                            startActivityForResult(intent, 200)
                                        }
                                        alertDialog.setButton(
                                            AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                                        ) { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        alertDialog.show()
                                    }
                                }
                            }
                        }

                    } else {
                        object : Thread() {
                            override fun run() {

                                val client = OkHttpClient().newBuilder().build()
                                val request: Request =
                                    Request.Builder().url(URL).method("GET", null)
                                        .addHeader("Cookie", iUtils.myInstagramTempCookies)
                                        .addHeader(
                                            "User-Agent", iUtils.UserAgentsList[j]
                                        ).build()
                                try {
                                    val response1: Response = client.newCall(request).execute()
                                    System.err.println("workkkkkkkkk 6 1 " + response1.code)

                                    if (response1.code == 200) {
                                        try {
                                            val listType: Type = object :
                                                TypeToken<ModelInstagramResponse?>() {}.type
                                            val modelInstagramResponse: ModelInstagramResponse =
                                                Gson().fromJson(
                                                    response1.body!!.string(), listType
                                                )
                                            if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                                val modelGetEdgetoNode: ModelGetEdgetoNode =
                                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children
                                                myInstaUsername =
                                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                                                val modelEdNodeArrayList: List<ModelEdNode> =
                                                    modelGetEdgetoNode.modelEdNodes
                                                for (i in modelEdNodeArrayList.indices) {
                                                    if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                                        myVideoUrlIs =
                                                            modelEdNodeArrayList[i].modelNode.video_url
                                                        DownloadFileMain.startDownloading(
                                                            myselectedActivity!!,
                                                            myVideoUrlIs,
                                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                                myVideoUrlIs
                                                            ),
                                                            ".mp4"
                                                        )
                                                        // etText.setText("")
                                                        try {
                                                            dismissMyDialogFrag()
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                        myVideoUrlIs = ""
                                                    } else {
                                                        myPhotoUrlIs =
                                                            modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                                        DownloadFileMain.startDownloading(
                                                            myselectedActivity!!,
                                                            myPhotoUrlIs,
                                                            myInstaUsername + iUtils.getImageFilenameFromURL(
                                                                myPhotoUrlIs
                                                            ),
                                                            ".png"
                                                        )
                                                        myPhotoUrlIs = ""
                                                        try {
                                                            dismissMyDialogFrag()
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                        // etText.setText("")
                                                    }
                                                }
                                            } else {
                                                val isVideo =
                                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                                                myInstaUsername =
                                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                                                if (isVideo) {
                                                    myVideoUrlIs =
                                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                                    DownloadFileMain.startDownloading(
                                                        myselectedActivity!!,
                                                        myVideoUrlIs,
                                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                            myVideoUrlIs
                                                        ),
                                                        ".mp4"
                                                    )
                                                    try {
                                                        dismissMyDialogFrag()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                    }
                                                    myVideoUrlIs = ""
                                                } else {
                                                    myPhotoUrlIs =
                                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                                    DownloadFileMain.startDownloading(
                                                        myselectedActivity!!,
                                                        myPhotoUrlIs,
                                                        myInstaUsername + iUtils.getImageFilenameFromURL(
                                                            myPhotoUrlIs
                                                        ),
                                                        ".png"
                                                    )
                                                    try {
                                                        dismissMyDialogFrag()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                    }
                                                    myPhotoUrlIs = ""
                                                }
                                            }
                                        } catch (e: java.lang.Exception) {
                                            System.err.println("workkkkkkkkk 4vvv errrr " + e.message)
                                            e.printStackTrace()
                                            try {
                                                dismissMyDialogFrag()
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }
                                    } else {
                                        System.err.println("workkkkkkkkk 6bbb errrr ")
                                        myselectedActivity!!.runOnUiThread {
                                            dismissMyDialogFrag()

                                            if (!myselectedActivity!!.isFinishing) {
                                                val alertDialog =
                                                    AlertDialog.Builder(myselectedActivity!!)
                                                        .create()
                                                alertDialog.setTitle(getString(R.string.logininsta))
                                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_POSITIVE,
                                                    getString(R.string.logininsta)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()
                                                    val intent = Intent(
                                                        myselectedActivity!!,
                                                        InstagramLoginActivity::class.java
                                                    )
                                                    startActivityForResult(intent, 200)
                                                }
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_NEGATIVE,
                                                    getString(R.string.cancel)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()
                                                }
                                                alertDialog.show()
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }.start()
                    }
                    println("working errpr \t Value: " + response.body!!.string())
                } catch (e: Exception) {
                    try {
                        println("response1122334455:   " + "Failed1 " + e.message)
                        dismissMyDialogFrag()
                    } catch (_: Exception) {

                    }
                }
            }
        }.start()
    }

    @Keep
    fun downloadInstagramImageOrVideodata(URL: String?, Coookie: String?) {

        val j = iUtils.getRandomNumber(iUtils.UserAgentsList.size)
        var Cookie = Coookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        val apiService: RetrofitApiInterface = RetrofitClient.getClient()

        val callResult: Call<JsonObject> = apiService.getInstagramData(
            URL, Cookie, iUtils.UserAgentsList[j]
        )
        callResult.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>, response: retrofit2.Response<JsonObject?>
            ) {
                println("response1122334455 ress :   " + response.body())
                try {


//                                val userdata = response.body()!!.getAsJsonObject("graphql")
//                                    .getAsJsonObject("shortcode_media")
//                                binding!!.profileFollowersNumberTextview.setText(
//                                    userdata.getAsJsonObject(
//                                        "edge_followed_by"
//                                    )["count"].asString
//                                )
//                                binding!!.profileFollowingNumberTextview.setText(
//                                    userdata.getAsJsonObject(
//                                        "edge_follow"
//                                    )["count"].asString
//                                )
//                                binding!!.profilePostNumberTextview.setText(userdata.getAsJsonObject("edge_owner_to_timeline_media")["count"].asString)
//                                binding!!.profileLongIdTextview.setText(userdata["username"].asString)
//


                    val listType = object : TypeToken<ModelInstagramResponse?>() {}.type
                    val modelInstagramResponse: ModelInstagramResponse? =
                        GsonBuilder().create().fromJson<ModelInstagramResponse>(
                            response.body().toString(), listType
                        )
                    if (modelInstagramResponse != null) {
                        if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                            val modelGetEdgetoNode: ModelGetEdgetoNode =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children
                            myInstaUsername =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                            val modelEdNodeArrayList: List<ModelEdNode> =
                                modelGetEdgetoNode.modelEdNodes
                            for (i in modelEdNodeArrayList.indices) {
                                if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                    myVideoUrlIs = modelEdNodeArrayList[i].modelNode.video_url
                                    DownloadFileMain.startDownloading(
                                        myselectedActivity!!,
                                        myVideoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    // etText.setText("")
                                    try {
                                        dismissMyDialogFrag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                    DownloadFileMain.startDownloading(
                                        myselectedActivity!!,
                                        myPhotoUrlIs,
                                        myInstaUsername + iUtils.getImageFilenameFromURL(
                                            myPhotoUrlIs
                                        ),
                                        ".png"
                                    )
                                    myPhotoUrlIs = ""
                                    try {
                                        dismissMyDialogFrag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    // etText.setText("")
                                }
                            }
                        } else {
                            val isVideo =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                            myInstaUsername =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                            if (isVideo) {
                                myVideoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                DownloadFileMain.startDownloading(
                                    myselectedActivity!!,
                                    myVideoUrlIs,
                                    myInstaUsername + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                    ".mp4"
                                )
                                try {
                                    dismissMyDialogFrag()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                myVideoUrlIs = ""
                            } else {
                                myPhotoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                DownloadFileMain.startDownloading(
                                    myselectedActivity!!,
                                    myPhotoUrlIs,
                                    myInstaUsername + iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                    ".png"
                                )
                                try {
                                    dismissMyDialogFrag()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                myPhotoUrlIs = ""
                            }
                        }
                    } else {
                        myselectedActivity!!.runOnUiThread {
                            iUtils.ShowToastError(
                                myselectedActivity,
                                myselectedActivity!!.resources.getString(R.string.somthing)
                            )
                        }
                        try {
                            dismissMyDialogFrag()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: java.lang.Exception) {
                    try {
                        try {
                            System.err.println("workkkkkkkkk 4")

                            downloadInstagramImageOrVideodata(
                                URL, ""
                            )
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            println("response1122334455 exe 1:   " + e.localizedMessage)
                            try {
                                dismissMyDialogFrag()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            System.err.println("workkkkkkkkk 5.1")
                            e.printStackTrace()
                            ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        myselectedActivity!!.runOnUiThread {
                            dismissMyDialogFrag()
                            if (!myselectedActivity!!.isFinishing) {
                                e.printStackTrace()
                                myselectedActivity!!.runOnUiThread {
                                    iUtils.ShowToastError(
                                        myselectedActivity,
                                        myselectedActivity!!.resources.getString(R.string.somthing)
                                    )
                                }
                                println("response1122334455 exe 1:   " + e.localizedMessage)
                                try {
                                    dismissMyDialogFrag()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                val alertDialog = AlertDialog.Builder(myselectedActivity!!).create()
                                alertDialog.setTitle(getString(R.string.logininsta))
                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(
                                        myselectedActivity!!, InstagramLoginActivity::class.java
                                    )
                                    startActivityForResult(intent, 200)
                                }
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                alertDialog.show()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                try {
                    println("response1122334455:   " + "Failed0" + t.message)
                    dismissMyDialogFrag()
                    myselectedActivity!!.runOnUiThread {
                        iUtils.ShowToastError(
                            myselectedActivity,
                            myselectedActivity!!.resources.getString(R.string.somthing)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun DownloadVideo(url: String) {
        //hide keyboard before progress
        hideKeyboard(this)
        progressDralogGenaratinglink.setMessage(resources.getString(R.string.genarating_download_link))
        Log.e("myhdasbdhf urlis frag  ", url)
        if (url == "" && iUtils.checkURL(url)) {
            ShowToast(this, getString(R.string.enter_valid))
        } else {
            uploadDownloadedUrl(url)
            val rand_int1 = iUtils.getRandomNumber(2)
            println("randonvalueis = $rand_int1")

            Log.d("mylogissssss", "The interstitial wasn't loaded yet.")
            if (url.contains("instagram.com")) {
                if (!myselectedActivity!!.isFinishing) {
                    if (!iUtils.isSocialMediaOn("instagram.com")) {
                        ShowToast(
                            myselectedActivity, getString(R.string.somthing_webiste_panele_block)
                        )
                        return
                    }
                    progressDralogGenaratinglink.show()
                    startInstaDownload(url)
                    //                    DownloadVideosMain.Start(myselectedActivity, url.trim(), false);
                }
            } else if (url.contains("myjosh.in")) {
                var myurl = url
                try {
                    myurl = iUtils.extractUrls(myurl)[0]
                } catch (ignored: Exception) {
                }
                DownloadVideosMain.Start(myselectedActivity, myurl.trim { it <= ' ' }, false)
                Log.e("downloadFileName12", myurl.trim { it <= ' ' })
            } else if (url.contains("audiomack")) {
                if (!iUtils.isSocialMediaOn("audiomack")) {
                    ShowToast(
                        myselectedActivity,
                        myselectedActivity?.getString(R.string.somthing_webiste_panele_block)
                    )
                    return
                }
                dismissMyDialogFrag()
                val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)
            } else if (url.contains("ok.ru")) {
                if (!iUtils.isSocialMediaOn("ok.ru")) {
                    ShowToast(
                        myselectedActivity,
                        myselectedActivity?.getString(R.string.somthing_webiste_panele_block)
                    )
                    return
                }
                dismissMyDialogFrag()
                val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)
            } else if (url.contains("zili")) {
                if (!iUtils.isSocialMediaOn("zili")) {
                    ShowToast(
                        myselectedActivity,
                        myselectedActivity?.getString(R.string.somthing_webiste_panele_block)
                    )
                    return
                }
                dismissMyDialogFrag()
                val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)
            } else if (url.contains("tiki")) {
                if (!iUtils.isSocialMediaOn("tiki")) {
                    ShowToast(
                        myselectedActivity,
                        myselectedActivity?.getString(R.string.somthing_webiste_panele_block)
                    )
                    return
                }
                dismissMyDialogFrag()
                val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)
            } else if (url.contains("vidlit")) {
                if (!iUtils.isSocialMediaOn("vidlit")) {
                    ShowToast(
                        myselectedActivity,
                        myselectedActivity?.getString(R.string.somthing_webiste_panele_block)
                    )
                    return
                }
                dismissMyDialogFrag()
                val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)
            } else if (url.contains("byte.co")) {
                if (!iUtils.isSocialMediaOn("byte.co")) {
                    ShowToast(
                        myselectedActivity,
                        myselectedActivity?.getString(R.string.somthing_webiste_panele_block)
                    )
                    return
                }
                dismissMyDialogFrag()
                val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)
            } else if (url.contains("fthis.gr")) {
                if (!iUtils.isSocialMediaOn("fthis.gr")) {
                    ShowToast(
                        myselectedActivity,
                        myselectedActivity?.getString(R.string.somthing_webiste_panele_block)
                    )
                    return
                }
                dismissMyDialogFrag()
                val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)
            } else if (url.contains("fw.tv") || url.contains("firework.tv")) {
                if (!iUtils.isSocialMediaOn("fw.tv") || !iUtils.isSocialMediaOn("firework.tv")) {
                    ShowToast(
                        myselectedActivity,
                        myselectedActivity?.getString(R.string.somthing_webiste_panele_block)
                    )
                    return
                }
                dismissMyDialogFrag()
                val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)
            } else if (url.contains("traileraddict")) {
                if (!iUtils.isSocialMediaOn("traileraddict")) {
                    ShowToast(
                        myselectedActivity,
                        myselectedActivity?.getString(R.string.somthing_webiste_panele_block)
                    )
                    return
                }
                dismissMyDialogFrag()
                val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)
            } else if (url.contains("bemate")) {
                if (!iUtils.isSocialMediaOn("bemate")) {
                    ShowToast(
                        myselectedActivity,
                        myselectedActivity?.getString(R.string.somthing_webiste_panele_block)
                    )
                    return
                }
                dismissMyDialogFrag()
                var myurl: String? = url
                try {
                    myurl = iUtils.extractUrls(myurl)[0]
                } catch (ignored: Exception) {
                }
                val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", myurl)
                startActivityForResult(intent, 2)
            } else if (url.contains("chingari")) {
                var myurl = url
                try {
                    myurl = iUtils.extractUrls(myurl)[0]
                } catch (ignored: Exception) {
                }
                DownloadVideosMain.Start(myselectedActivity, myurl.trim { it <= ' ' }, false)
                Log.e("downloadFileName12", myurl.trim { it <= ' ' })
            } else if (url.contains("sck.io") || url.contains("snackvideo")) {
                var myurl = url
                try {
                    myurl = iUtils.extractUrls(myurl)[0]
                } catch (ignored: Exception) {
                }
                DownloadVideosMain.Start(myselectedActivity, myurl.trim { it <= ' ' }, false)
                Log.e("downloadFileName12", myurl.trim { it <= ' ' })
            } else {
                var myurl = url
                try {
                    myurl = iUtils.extractUrls(myurl)[0]
                } catch (ignored: Exception) {
                }
                DownloadVideosMain.Start(myselectedActivity, myurl.trim { it <= ' ' }, false)
                Log.e("downloadFileName12", myurl.trim { it <= ' ' })
            }
        }
    }

}
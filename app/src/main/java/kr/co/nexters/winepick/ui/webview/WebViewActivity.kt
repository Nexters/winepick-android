package kr.co.nexters.winepick.ui.webview

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import dagger.hilt.android.AndroidEntryPoint
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.constant.Constant
import kr.co.nexters.winepick.databinding.ActivityWebViewBinding
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.base.BaseViewModel
import timber.log.Timber

/**
 * Kotlin 에서 사용하는 일반 액티비티 예
 *
 * @since v1.0.0 / 2021.01.28
 */
@AndroidEntryPoint
class WebViewActivity : BaseActivity<ActivityWebViewBinding>(
    R.layout.activity_web_view
) {
    override val viewModel: BaseViewModel?
        get() = null

    private val urlParams: String
        get() = intent.getStringExtra(Constant.STRING_EXTRA_WEB_URL_PARAMS) ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSwipeRefresh()

        binding.apply {
            // 텍스트 값 세팅
            binding.wvWebView.apply {
                //웹뷰의 설정을 다음과 같이 맞춰주시기 바랍니다.
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.javaScriptCanOpenWindowsAutomatically = true

                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                settings.loadsImagesAutomatically = true
                settings.builtInZoomControls = true
                settings.setSupportZoom(true)
                settings.setSupportMultipleWindows(true)
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true

                webViewClient = WineImageWebViewClient()
            }

            // 스크롤 업 대신에 리프레쉬 이벤트가 트리거 되는걸 방지
            binding.wvWebView.viewTreeObserver.addOnScrollChangedListener {
                binding.srWebViewSwipeRefresh.isEnabled = binding.wvWebView.scrollY == 0
            }
        }

        reload()
    }

    private fun reload(){
        val parsedUrl = resources.getString(R.string.web_view_images_search_url, urlParams)
        binding.wvWebView.loadUrl(parsedUrl)
        setSwipeRefresh(false)
    }

    private fun initSwipeRefresh() {
        binding.srWebViewSwipeRefresh.setOnRefreshListener { reload() }
        binding.srWebViewSwipeRefresh.setColorSchemeResources(R.color.colorPink)
        binding.srWebViewSwipeRefresh.setDistanceToTriggerSync(250)
        setSwipeRefresh(false)
    }

    fun setSwipeRefresh(enable: Boolean) {
        binding.srWebViewSwipeRefresh.isRefreshing = enable
    }

    inner class WineImageWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Timber.i("shouldOverrideUrlLoading url : $url")

            return !url.isNullOrEmpty()
        }
    }
}

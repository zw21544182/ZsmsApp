package ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.zhongwang.zsmsapp.R;

import base.BaseActivity;

/**
 * Created by zhongwang on 2018/3/22.
 */

public class DetailActivity extends BaseActivity {
    private WebView wvDetail;
    private String messageUrl;
    private TextView tvGoBack;

    @Override
    public int getLayoutId() {
        setFullScreen();
        return R.layout.activity_detail;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        messageUrl = getIntent().getExtras().getString("messageUrl");
        setWebViewStyle();
        wvDetail.loadUrl(messageUrl);
    }

    private void setWebViewStyle() {
        wvDetail.setWebViewClient(new WebViewClient());
        //网页显示图片解决
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            wvDetail.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wvDetail.getSettings().setBlockNetworkImage(false);
        //支持javascript
        wvDetail.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        wvDetail.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        wvDetail.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        wvDetail.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        wvDetail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvDetail.getSettings().setLoadWithOverviewMode(true);


        //如果不设置WebViewClient，请求会跳转系统浏览器
        wvDetail.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showDialog();
           }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(sweetAlertDialog.isShowing()){
                    sweetAlertDialog.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

                if (url.toString().contains("sina.cn")) {
                    view.loadUrl("http://ask.csdn.net/questions/178242");
                    return true;
                }

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("sina.cn")) {
                        view.loadUrl("http://ask.csdn.net/questions/178242");
                        return true;
                    }
                }

                return false;
            }

        });
    }

    @Override
    protected void initView() {
        wvDetail = findViewById(R.id.wvDetail);
        tvGoBack = findViewById(R.id.tvGoBack);
        tvGoBack.setOnClickListener(this);
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wvDetail.canGoBack()) {
                wvDetail.goBack();   //返回上一页面
                return true;
            } else {
                finish();
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        if (wvDetail.canGoBack()) {
            wvDetail.goBack();   //返回上一页面
        } else {
            finish();
        }
    }
}

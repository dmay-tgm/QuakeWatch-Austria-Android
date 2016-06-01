package tgm.shakeit.quakewatchaustria;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


/**
 * Creates a new Fragment and loads the correct WebPage.
 *
 * @author Daniel May
 * @version 2016-06-01.1
 */
public class WebPage extends Fragment {

    private ProgressBar mPbar = null;

    /**
     * Gets called on inflating the view
     *
     * @param inflater           the inflater
     * @param container          the container that contains the fragment
     * @param savedInstanceState the saved instance state
     * @return the fragment's view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.web_views, container, false);
        mPbar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        WebView wv = (WebView) rootView.findViewById(R.id.webView);
        wv.getSettings().setAppCachePath(container.getContext().getCacheDir().getAbsolutePath());
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.setWebViewClient(new WebViewClient() {
            /**
             * Gets called when loading a page starts
             * @param view the webview
             * @param url the url
             * @param favicon tge icon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mPbar.setVisibility(View.VISIBLE);
            }

            /**
             * Gets called when loading a page finishes
             * @param view the webview
             * @param url the url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                mPbar.setVisibility(View.GONE);
            }
        });
        wv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default

        if (!isNetworkAvailable(container.getContext())) { // loading offline
            wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        int toLoad = getArguments().getInt("toLoad");
        String url = null;
        switch (toLoad) {
            case R.id.behavior_advisor:
                url = "https://www.zamg.ac.at/cms/de/geophysik/erdbeben/verhaltensratgeber";
                break;
            case R.id.lexicon:
                url = "https://www.zamg.ac.at/cms/de/geophysik/lexikon";
                break;
            case R.id.faq:
                url = "https://www.zamg.ac.at/cms/de/geophysik/erdbeben/lehrmaterialien/faqs-zu-erdbeben";
                break;
            case R.id.overview:
                url = "https://www.zamg.ac.at/cms/de/geophysik/erdbeben/erdbeben-in-oesterreich/uebersicht_neu";
                break;
            case R.id.quake_hazard:
                url = "https://www.zamg.ac.at/cms/de/geophysik/erdbeben/erdbeben-in-oesterreich/erdbebengefaehrdungzonen-in-oesterreich";
                break;
            case R.id.strongest_quakes:
                url = "https://www.zamg.ac.at/cms/de/geophysik/erdbeben/erdbeben-in-oesterreich/copy3_of_die-staerksten-erdbeben-in-oesterreich";
                break;
        }
        wv.loadUrl(url);
        getActivity().setTitle(getArguments().getString("title"));
        return rootView;
    }

    /**
     * Checks if the network is available
     *
     * @param context the application's context
     * @return if the network is available
     */
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
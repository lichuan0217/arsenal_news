package top.lemonsoda.gunners.newsdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.data.module.NewsDetail;
import top.lemonsoda.gunners.utils.Constants;

/**
 * Created by chuanl on 2/24/17.
 */

public class NewsDetailFragment extends Fragment implements NewsDetailContract.View {
    private static final String TAG = NewsDetailFragment.class.getCanonicalName();

    private NewsDetailContract.Presenter presenter;
    private String articleId;
    private String header;

    @BindView(R.id.tv_article_date)
    TextView tvArticleDate;

    @BindView(R.id.tv_article_editor)
    TextView tvArticleEditor;

    @BindView(R.id.tv_article_source)
    TextView tvArticleSource;

    @BindView(R.id.wv_article)
    WebView wvArticle;

    public NewsDetailFragment() {
    }

    public static NewsDetailFragment newInstance(String id, String header) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(Constants.INTENT_EXTRA_ARTICLE_ID, id);
        args.putString(Constants.INTENT_EXTRA_HEADER, header);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.articleId = getArguments().getString(Constants.INTENT_EXTRA_ARTICLE_ID);
            this.header = getArguments().getString(Constants.INTENT_EXTRA_HEADER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
        ButterKnife.bind(this, view);

        initWebView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    @Override
    public void setPresenter(NewsDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "Loading data...");
    }

    @Override
    public void stopLoading() {
        Log.d(TAG, "Stop loading data...");
    }

    @Override
    public void showError() {
        Log.d(TAG, "Error when loading data...");
        Toast.makeText(
                getActivity(),
                getString(R.string.info_load_error),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNewsDetail(NewsDetail newsDetail) {
        wvArticle.loadData(buildHtmlContent(newsDetail.getContent()), "text/html; charset=uft-8", "utf-8");
        tvArticleSource.setText("来源 " + newsDetail.getSource());
        tvArticleDate.setText(newsDetail.getDate());
        tvArticleEditor.setText(newsDetail.getEditor());
        Glide.with(getActivity())
                .load(newsDetail.getPicture_src())
                .centerCrop()
                .thumbnail(0.5f)
                .into(((NewsDetailActivity) getActivity()).ivArticleHeader);
    }

    private void initWebView() {
        WebSettings webSettings = wvArticle.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        // Show data after loading page finish
        wvArticle.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                tvArticleDate.setVisibility(View.VISIBLE);
                tvArticleEditor.setVisibility(View.VISIBLE);
                tvArticleSource.setVisibility(View.VISIBLE);
                wvArticle.setVisibility(View.VISIBLE);
            }
        });
    }

    private String buildHtmlContent(String content) {
        String head = "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "<title>" + header + "</title>\n" +
                "<meta name=\"viewport\" content=\"user-scalable=no, width=device-width\">\n" +
                "<style type=\"text/css\">" +
                "img{" +
                "display: inline;" +
                "max-width:100%;" +
                "height: auto" +
                "}" +
                "</style>\n" +
                "<base target=\"_blank\">\n" +
                "</head>";
        String bodyStart = "<body>";
        String bodyEnd = "</body>";

        return head + bodyStart + content.replaceAll("<div class=\"img-place-holder\"></div>", "") + bodyEnd;
    }
}

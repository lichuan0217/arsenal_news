package top.lemonsoda.gunners.utils.ui;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.gunners.R;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.utils.Utils;

/**
 * Created by chuanl on 2/28/17.
 * <p>
 * Header view in NewsIndexFragment.
 */

public class NewsPagerView extends FrameLayout {
    private static final String TAG = NewsPagerView.class.getCanonicalName();

    private static final int DELAY_TIME = 5000;
    private static final int DELAY_AFTER_DRAG_TIME = 3000;

    private Context context;
    private View view;
    private List<News> newsList;
    private List<View> contentViews;
    private List<ImageView> ivDots;
    private int currentItem = 0;
    private Handler handler = new Handler();

    @BindView(R.id.vp_news)
    ViewPager vp;

    @BindView(R.id.ll_news_dot)
    LinearLayout llDots;

    public NewsPagerView(Context context) {
        this(context, null);
    }

    public NewsPagerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsPagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
        reset();
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.layout_pager, this, true);
        ButterKnife.bind(view);

        contentViews = new ArrayList<>();
        ivDots = new ArrayList<>();
    }

    private void reset() {
        contentViews.clear();
        ivDots.clear();
        initUI();
    }

    private void initUI() {

        llDots.removeAllViews();
        int len = newsList.size();
        for (int i = 0; i < len; ++i) {
            ImageView ivDot = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.leftMargin = 5;
            params.rightMargin = 5;
            llDots.addView(ivDot, params);
            ivDots.add(ivDot);
        }

        for (int i = 0; i < len; ++i) {
            View content = LayoutInflater.from(context).inflate(R.layout.layout_pager_content, null);
            ImageView ivTitle = ButterKnife.findById(content, R.id.iv_pager_title);
            TextView tvTitle = ButterKnife.findById(content, R.id.tv_pager_title);

            tvTitle.setText(newsList.get(i).getHeader());
            Glide.with(context)
                    .load(Utils.getImageFromThumbnailUrl(newsList.get(i).getThumbnail()))
                    .asBitmap()
                    .centerCrop()
                    .into(ivTitle);

            contentViews.add(content);
        }

        vp.setAdapter(new NewsPagerAdapter());
        vp.addOnPageChangeListener(new OnNewsPagerChangeListener());
        vp.setFocusable(true);
        vp.setCurrentItem(0);
        dotsChange(0);
        currentItem = 0;
        startAutoPlay();
    }

    private void startAutoPlay() {
        handler.removeCallbacks(autoScrollTask);
        handler.postDelayed(autoScrollTask, DELAY_TIME);
    }

    public void stopAutoPlay() {
        handler.removeCallbacks(autoScrollTask);
    }

    Runnable autoScrollTask = new Runnable() {
        @Override
        public void run() {
            currentItem = (currentItem + 1) % contentViews.size();
            vp.setCurrentItem(currentItem);
            handler.postDelayed(autoScrollTask, DELAY_TIME);
        }
    };

    Runnable startAutoAfterDrag = new Runnable() {
        @Override
        public void run() {
            startAutoPlay();
        }
    };

    private void dotsChange(int pos) {
        for (int i = 0; i < ivDots.size(); ++i) {
            if (i == pos) {
                ivDots.get(i).setImageResource(R.mipmap.dot_focus);
            } else {
                ivDots.get(i).setImageResource(R.mipmap.dot_blur);
            }
        }
    }

    class NewsPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return contentViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "NewsPagerAdapter: instantiateItem position --> " + position);
            container.addView(contentViews.get(position));
            return contentViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class OnNewsPagerChangeListener implements ViewPager.OnPageChangeListener {
        private int previousState = ViewPager.SCROLL_STATE_IDLE;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            dotsChange(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (previousState == ViewPager.SCROLL_STATE_DRAGGING
                    && state == ViewPager.SCROLL_STATE_SETTLING) {
                // When user drags viewpager, stop the autoScrollTask
                // If user doesn't drag after DELAY_AFTER_DRAG_TIME, start autoScrollTask again
                handler.removeCallbacks(autoScrollTask);
                handler.removeCallbacks(startAutoAfterDrag);
                handler.postDelayed(startAutoAfterDrag, DELAY_AFTER_DRAG_TIME);
            }
        }
    }
}

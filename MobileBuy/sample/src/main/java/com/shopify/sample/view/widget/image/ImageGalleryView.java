package com.shopify.sample.view.widget.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.shopify.sample.R;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ImageGalleryView extends FrameLayout {
  @BindView(R.id.image_pager) ViewPager imagePagerView;
  @BindView(R.id.thumbnail_list) RecyclerView thumbnailListView;
  @BindView(R.id.thumbnail_selection_frame) View thumbnailSelectionFrameView;

  private OnImageClickListener onImageClickListener;
  private ImageGalleryViewPageAdapter.OnItemClickListener onItemClickListener = new ImageGalleryViewPageAdapter.OnItemClickListener() {
    @Override public void onImageClick(final Bitmap image) {
      if (onImageClickListener != null) {
        onImageClickListener.onImageClick(image);
      }
    }
  };

  public ImageGalleryView(final Context context) {
    super(context);
  }

  public ImageGalleryView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public ImageGalleryView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void renderImages(@NonNull final List<String> images) {
    checkNotNull(images, "images == null");
    imagePagerView.setAdapter(new ImageGalleryViewPageAdapter(getContext(), images, onItemClickListener));

    RecyclerViewAdapter adapter = new RecyclerViewAdapter(it -> imagePagerView.setCurrentItem(it.position()));
    List<ListItemViewModel> items = new ArrayList<>();
    for (String image : images) {
      items.add(new ImageGalleryPagerListItemModel(image));
    }
    adapter.addItems(items);
    thumbnailListView.setAdapter(adapter);

  }

  public View anchorPreview() {
    return imagePagerView;
  }

  public void onImageClickListener(@NonNull final OnImageClickListener onImageClickListener) {
    checkNotNull(onImageClickListener, "onImageClickListener == null");
    this.onImageClickListener = onImageClickListener;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    initPagerView();
    initPagerIndicator();
  }

  private void initPagerView() {
    imagePagerView.setPageTransformer(true, new DepthPageTransformer());

    imagePagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) thumbnailListView.getLayoutManager();
        if (thumbnailSelectionFrameView != null) {
          View view = layoutManager.findViewByPosition(position);
          if (view != null) {
            float distance = 0;

            if (layoutManager.getItemCount() > position + 1) {
              distance = layoutManager.findViewByPosition(position + 1).getLeft() - view.getLeft();
            }

            thumbnailSelectionFrameView.setTranslationX(view.getLeft() + distance * positionOffset);
          }
        }
      }

      @Override
      public void onPageSelected(int position) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) thumbnailListView.getLayoutManager();
        if (position <= layoutManager.findFirstCompletelyVisibleItemPosition()) {
          thumbnailListView.smoothScrollToPosition(Math.max(position, 0));
        } else if (position >= layoutManager.findLastCompletelyVisibleItemPosition()) {
          thumbnailListView.smoothScrollToPosition(Math.min(position, thumbnailListView.getAdapter().getItemCount()));
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    });
  }

  private void initPagerIndicator() {
    LinearLayoutManager layoutManager = (LinearLayoutManager) thumbnailListView.getLayoutManager();
    thumbnailListView.setHasFixedSize(true);
    thumbnailListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        View view = layoutManager.findViewByPosition(imagePagerView.getCurrentItem());
        if (view != null) {
          thumbnailSelectionFrameView.setTranslationX(view.getLeft());
        } else {
          thumbnailSelectionFrameView.setTranslationX(thumbnailListView.getWidth());
        }
      }
    });

    int defaultPadding = getResources().getDimensionPixelOffset(R.dimen.default_padding_half);
    thumbnailListView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
          return;
        }
        outRect.left = position == 0 ? defaultPadding / 2 : defaultPadding / 4;
        outRect.right = position == parent.getAdapter().getItemCount() ? defaultPadding / 2 : defaultPadding / 4;
      }
    });
  }

  private final static class DepthPageTransformer implements ViewPager.PageTransformer {
    static final float MIN_SCALE = 0.8f;

    @Override
    public void transformPage(View view, float position) {
      int pageWidth = view.getWidth();

      if (position < -1) { // [-Infinity,-1)
        // This page is way off-screen to the left.
        view.setAlpha(0);

      } else if (position <= 0) { // [-1,0]
        // Use the default slide transition when moving to the left page
        view.setAlpha(1);
        view.setTranslationX(0);
        view.setScaleX(1);
        view.setScaleY(1);

      } else if (position <= 1) { // (0,1]
        // Fade the page out.
        view.setAlpha(1 - position);

        // Counteract the default slide transition
        view.setTranslationX(pageWidth * -position);

        // Scale the page down (between MIN_SCALE and 1)
        float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
      } else { // (1,+Infinity]
        // This page is way off-screen to the right.
        view.setAlpha(0);
      }
    }
  }

  interface OnImageClickListener {
    void onImageClick(Bitmap image);
  }
}

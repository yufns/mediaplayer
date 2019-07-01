package com.example.gsyvideoplayer.video;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gsyvideoplayer.R;
import com.example.gsyvideoplayer.model.SwitchVideoModel;
import com.example.gsyvideoplayer.view.SwitchVideoTypeDialog;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuyu on 2016/12/7.
 * 注意
 * 这个播放器的demo配置切换到全屏播放器
 * 这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
 */

public class SampleVideo extends StandardGSYVideoPlayer {

    private static final String TAG = SampleVideo.class.getSimpleName();
    private TextView mMoreScale;

    private TextView mSwitchSize;

    private TextView mChangeRotate;

    private TextView mChangeTransform;

    private TextView mTvSpeed;

    private RecyclerView mRvThumb;

    private List<SwitchVideoModel> mUrlList = new ArrayList<>();

    /**
     * 记住切换数据源类型
     */
    private int mType = 0;

    private int mTransformSize = 0;

    /**
     * 数据源
     */
    private int mSourcePosition = 0;

    private String mTypeText = "标准";
    private ArrayList<Bitmap> mBitmapArrayList;
    private Disposable mDisposable;
    private Adapter mAdapter;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public SampleVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleVideo(Context context) {
        super(context);
    }

    public SampleVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void init(Context context) {
        super.init(context);
        initView();
    }

    private void initView() {
        mMoreScale = findViewById(R.id.moreScale);
        mSwitchSize = findViewById(R.id.switchSize);
        mChangeRotate = findViewById(R.id.change_rotate);
        mChangeTransform = findViewById(R.id.change_transform);
        mRvThumb = findViewById(R.id.rv_thumb);
        mTvSpeed = findViewById(R.id.tv_speed);
        mTvSpeed.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                showSwitchSpeedDialog();
            }
        });
        //切换清晰度
        mMoreScale.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (!mHadPlay) {
                    return;
                }
                if (mType == 0) {
                    mType = 1;
                } else if (mType == 1) {
                    mType = 2;
                } else if (mType == 2) {
                    mType = 3;
                } else if (mType == 3) {
                    mType = 4;
                } else if (mType == 4) {
                    mType = 0;
                }
                resolveTypeUI();
            }
        });
        //切换视频清晰度
        mSwitchSize.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                showSwitchDialog();
            }
        });
        //旋转播放角度
        mChangeRotate.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (!mHadPlay) {
                    return;
                }
                if ((mTextureView.getRotation() - mRotate) == 270) {
                    mTextureView.setRotation(mRotate);
                    mTextureView.requestLayout();
                } else {
                    mTextureView.setRotation(mTextureView.getRotation() + 90);
                    mTextureView.requestLayout();
                }
            }
        });
        //镜像旋转
        mChangeTransform.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (!mHadPlay) {
                    return;
                }
                if (mTransformSize == 0) {
                    mTransformSize = 1;
                } else if (mTransformSize == 1) {
                    mTransformSize = 2;
                } else if (mTransformSize == 2) {
                    mTransformSize = 0;
                }
                resolveTransform();
            }
        });
        initThumbnail();
    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        if (fromUser) {
            scrollThumbnail(progress);
        }
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        mRvThumb.setAlpha(0f);
        mRvThumb.setVisibility(View.VISIBLE);
        mRvThumb.animate()
            .alpha(1f)
            .setDuration(400)
            .setListener(null);
    }

    @Override public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        mRvThumb.setAlpha(1f);
        mRvThumb.animate()
            .alpha(0f)
            .setDuration(400)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRvThumb.setVisibility(View.GONE);
                }
            });
    }

    private void scrollThumbnail(int progress) {
        mRvThumb.scrollToPosition(getPosition(progress));
    }

    private int getPosition(int progress) {
        return (int) (mAdapter.getItemCount() * (progress / 100F));
    }

    private void initThumbnail() {
        mBitmapArrayList = new ArrayList<>();
        mRvThumb.setHasFixedSize(true);
        final LinearLayoutManager layoutManager =
            new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRvThumb.setLayoutManager(layoutManager);
        final LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRvThumb);
        mRvThumb
            .getViewTreeObserver()
            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override public void onGlobalLayout() {
                    for (int i = layoutManager.findFirstVisibleItemPosition();
                        i <= layoutManager.findLastVisibleItemPosition(); i++) {
                        View viewByPosition = layoutManager.findViewByPosition(i);
                        if (viewByPosition != null) {
                            viewByPosition
                                .findViewById(R.id.iv_thumb)
                                .setSelected(false);
                        }
                    }
                    View centerView = snapHelper.findSnapView(layoutManager);
                    if (centerView != null) {
                        centerView
                            .findViewById(R.id.iv_thumb)
                            .setSelected(true);
                    }
                }
            });
        mAdapter = new Adapter(getContext(), R.layout.item_thumb, mBitmapArrayList);
        mRvThumb.setAdapter(mAdapter);
        disposable();
        mDisposable = Observable
            //这里使用的是本地的地址
            .just("01.png")
            .subscribeOn(Schedulers.computation())
            .flatMap(new Function<String, ObservableSource<Bitmap>>() {
                @Override public ObservableSource<Bitmap> apply(String s) throws Exception {
                    InputStream open = getContext()
                        .getAssets()
                        .open(s);
                    return Observable.just(BitmapFactory.decodeStream(open));
                }
            })
            .flatMap(new Function<Bitmap, ObservableSource<ArrayList<Bitmap>>>() {
                @Override public ObservableSource<ArrayList<Bitmap>> apply(Bitmap bitmap)
                    throws Exception {
                    return Observable.just(splitImage(bitmap, 10, 10));
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<ArrayList<Bitmap>>() {
                @Override public void accept(ArrayList<Bitmap> bitmaps) throws Exception {
                    mBitmapArrayList.clear();
                    mBitmapArrayList.addAll(bitmaps);
                    mAdapter.notifyDataSetChanged();
                }
            });
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        disposable();
    }

    private void disposable() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    /**
     * @param rawBitmap 原来的Bitmap
     * @param row 切成几行
     * @param column 切成几列
     */
    private ArrayList<Bitmap> splitImage(Bitmap rawBitmap, int row, int column) {
        ArrayList<Bitmap> partImagesArrayList = new ArrayList<>(row * column);
        int rawBitmapWidth = rawBitmap.getWidth();
        int rawBitmapHeight = rawBitmap.getHeight();
        int perPartWidth = rawBitmapWidth / column;
        int perPartHeight = rawBitmapHeight / row;
        Bitmap perBitmap;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                int x = j * perPartWidth;
                int y = i * perPartHeight;
                perBitmap = Bitmap.createBitmap(rawBitmap, x, y, perPartWidth, perPartHeight);
                partImagesArrayList.add(perBitmap);
            }
        }
        return partImagesArrayList;
    }

    private void showSwitchSpeedDialog() {
        final List<SwitchVideoModel> data = new ArrayList<>();
        data.add(new SwitchVideoModel("1倍", "1.0F"));
        data.add(new SwitchVideoModel("1.5倍", "1.5F"));
        data.add(new SwitchVideoModel("2倍", "2.0F"));
        SwitchVideoTypeDialog switchVideoTypeDialog = new SwitchVideoTypeDialog(getContext());
        switchVideoTypeDialog.initList(data, new SwitchVideoTypeDialog.OnListItemClickListener() {
            @Override public void onItemClick(int position) {
                SwitchVideoModel videoModel = data.get(position);
                mTvSpeed.setText(videoModel.getName());
                setSpeed(Float.parseFloat(videoModel.getUrl()), true);
            }
        });
        switchVideoTypeDialog.show();
    }

    /**
     * 需要在尺寸发生变化的时候重新处理
     */
    @Override public void onSurfaceSizeChanged(Surface surface, int width, int height) {
        super.onSurfaceSizeChanged(surface, width, height);
        resolveTransform();
    }

    @Override public void onSurfaceAvailable(Surface surface) {
        super.onSurfaceAvailable(surface);
        resolveRotateUI();
        resolveTransform();
    }

    /**
     * 处理镜像旋转
     * 注意，暂停时
     */
    protected void resolveTransform() {
        switch (mTransformSize) {
            case 1: {
                Matrix transform = new Matrix();
                transform.setScale(-1, 1, mTextureView.getWidth() / 2, 0);
                mTextureView.setTransform(transform);
                mChangeTransform.setText("左右镜像");
                mTextureView.invalidate();
            }
            break;
            case 2: {
                Matrix transform = new Matrix();
                transform.setScale(1, -1, 0, mTextureView.getHeight() / 2);
                mTextureView.setTransform(transform);
                mChangeTransform.setText("上下镜像");
                mTextureView.invalidate();
            }
            break;
            case 0: {
                Matrix transform = new Matrix();
                transform.setScale(1, 1, mTextureView.getWidth() / 2, 0);
                mTextureView.setTransform(transform);
                mChangeTransform.setText("旋转镜像");
                mTextureView.invalidate();
            }
            break;
            default:
                break;
        }
    }

    /**
     * 设置播放URL
     *
     * @param url 播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param title title
     */
    public boolean setUp(List<SwitchVideoModel> url, boolean cacheWithPlay, String title) {
        mUrlList = url;
        return setUp(url
            .get(mSourcePosition)
            .getUrl(), cacheWithPlay, title);
    }

    /**
     * 设置播放URL
     *
     * @param url 播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath 缓存路径，如果是M3U8或者HLS，请设置为false
     * @param title title
     */
    public boolean setUp(List<SwitchVideoModel> url, boolean cacheWithPlay, File cachePath,
        String title) {
        mUrlList = url;
        return setUp(url
            .get(mSourcePosition)
            .getUrl(), cacheWithPlay, cachePath, title);
    }

    @Override public int getLayoutId() {
        return R.layout.simple_video;
    }

    /**
     * 全屏时将对应处理参数逻辑赋给全屏播放器
     */
    @Override public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar,
        boolean statusBar) {
        SampleVideo sampleVideo =
            (SampleVideo) super.startWindowFullscreen(context, actionBar, statusBar);
        sampleVideo.mSourcePosition = mSourcePosition;
        sampleVideo.mType = mType;
        sampleVideo.mTransformSize = mTransformSize;
        sampleVideo.mUrlList = mUrlList;
        sampleVideo.mTypeText = mTypeText;
        //sampleVideo.resolveTransform();
        sampleVideo.resolveTypeUI();
        //sampleVideo.resolveRotateUI();
        //这个播放器的demo配置切换到全屏播放器
        //这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
        //比如已旋转角度之类的等等
        //可参考super中的实现
        return sampleVideo;
    }

    /**
     * 退出全屏时将对应处理参数逻辑返回给非播放器
     * @param oldF
     * @param vp
     * @param gsyVideoPlayer 播放器
     */
    @Override protected void resolveNormalVideoShow(View oldF, ViewGroup vp,
        GSYVideoPlayer gsyVideoPlayer) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
        if (gsyVideoPlayer != null) {
            SampleVideo sampleVideo = (SampleVideo) gsyVideoPlayer;
            mSourcePosition = sampleVideo.mSourcePosition;
            mType = sampleVideo.mType;
            mTransformSize = sampleVideo.mTransformSize;
            mTypeText = sampleVideo.mTypeText;
            setUp(mUrlList, mCache, mCachePath, mTitle);
            resolveTypeUI();
        }
    }

    /**
     * 旋转逻辑
     */
    private void resolveRotateUI() {
        if (!mHadPlay) {
            return;
        }
        mTextureView.setRotation(mRotate);
        mTextureView.requestLayout();
    }

    /**
     * 显示比例
     * 注意，GSYVideoType.setShowType是全局静态生效，除非重启APP。
     */
    private void resolveTypeUI() {
        if (!mHadPlay) {
            return;
        }
        if (mType == 1) {
            mMoreScale.setText("16:9");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9);
        } else if (mType == 2) {
            mMoreScale.setText("4:3");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3);
        } else if (mType == 3) {
            mMoreScale.setText("全屏");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        } else if (mType == 4) {
            mMoreScale.setText("拉伸全屏");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        } else if (mType == 0) {
            mMoreScale.setText("默认比例");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        }
        changeTextureViewShowType();
        if (mTextureView != null) {
            mTextureView.requestLayout();
        }
        mSwitchSize.setText(mTypeText);
    }

    /**
     * 弹出切换清晰度
     */
    private void showSwitchDialog() {
        if (!mHadPlay) {
            return;
        }
        SwitchVideoTypeDialog switchVideoTypeDialog = new SwitchVideoTypeDialog(getContext());
        switchVideoTypeDialog.initList(mUrlList,
            new SwitchVideoTypeDialog.OnListItemClickListener() {
                @Override public void onItemClick(int position) {
                    final String name = mUrlList
                        .get(position)
                        .getName();
                    if (mSourcePosition != position) {
                        if ((mCurrentState == GSYVideoPlayer.CURRENT_STATE_PLAYING
                            || mCurrentState == GSYVideoPlayer.CURRENT_STATE_PAUSE)) {
                            final String url = mUrlList
                                .get(position)
                                .getUrl();
                            onVideoPause();
                            final long currentPosition = mCurrentPosition;
                            getGSYVideoManager().releaseMediaPlayer();
                            cancelProgressTimer();
                            hideAllWidget();
                            new Handler().postDelayed(new Runnable() {
                                @Override public void run() {
                                    setUp(url, mCache, mCachePath, mTitle);
                                    setSeekOnStart(currentPosition);
                                    startPlayLogic();
                                    cancelProgressTimer();
                                    hideAllWidget();
                                }
                            }, 500);
                            mTypeText = name;
                            mSwitchSize.setText(name);
                            mSourcePosition = position;
                        }
                    } else {
                        Toast
                            .makeText(getContext(), "已经是 " + name, Toast.LENGTH_LONG)
                            .show();
                    }
                }
            });
        switchVideoTypeDialog.show();
    }

    class Adapter extends CommonAdapter<Bitmap> {

        Adapter(Context context, int layoutId, List<Bitmap> datas) {
            super(context, layoutId, datas);
        }

        @Override public void convert(ViewHolder viewHolder, Bitmap bitmap) {
            ImageView imageView = viewHolder.getView(R.id.iv_thumb);
            imageView.setImageBitmap(bitmap);
        }
    }
}

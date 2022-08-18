package org.sheedon.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by youzehong on 16/4/19.
 */
public class ArcProgressBar extends View {

    private Paint mArcPaint;
    private Paint mEmptyPaint;
    private Paint mTextPaint;
    private RectF mArcRect;
    private Rect mBitmapRect;
    private Bitmap mBitmap;

    private Matrix matrix = new Matrix();


    /**
     * 圆弧宽度
     */
    private float mArcWidth = 20.0f;
    /**
     * 圆弧颜色
     */
    private int mArcBgColor = 0xFFFF916D;
    /**
     * 填充颜色
     */
    private int mFillingColor = 0xFFFF916D;
    private int textColor = 0xFFFF916D;
    /**
     * 圆弧两边的距离
     */
    private int mPdDistance = 50;
    /**
     * 圆弧跟虚线之间的距离
     */
    private int mLineDistance = 20;
    /**
     * 进度条最大值
     */
    private int mProgressMax = 100;
    // icon 资源
    private int iconRes = R.drawable.img_empty;

    private int mProgress;
    private int mArcRadius; // 圆弧半径
    private double bDistance;
    private boolean isRestart = false;
    private int mRealProgress = 90;

    public ArcProgressBar(Context context) {
        this(context, null, 0);
    }

    public ArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intiAttributes(context, attrs);
        initView();
    }

    private void intiAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar);
        mPdDistance = a.getInteger(R.styleable.ArcProgressBar_arcDistance, mPdDistance);
        mArcBgColor = a.getColor(R.styleable.ArcProgressBar_arcBgColor, mArcBgColor);
        mFillingColor = a.getColor(R.styleable.ArcProgressBar_fillingColor, mFillingColor);
        mArcWidth = a.getFloat(R.styleable.ArcProgressBar_arcWidth, mArcWidth);
        mLineDistance = a.getInteger(R.styleable.ArcProgressBar_lineDistance, mLineDistance);
        mProgressMax = a.getInteger(R.styleable.ArcProgressBar_progressMax, mProgressMax);
//        iconRes = a.getResourceId(R.styleable.ArcProgressBar_iconRes, iconRes);
        textColor = a.getResourceId(R.styleable.ArcProgressBar_textColor, textColor);
        a.recycle();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initView() {
        // 外层圆弧的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setColor(mFillingColor);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mEmptyPaint = new Paint();
        mEmptyPaint.setAntiAlias(true);
        mEmptyPaint.setStyle(Paint.Style.STROKE);
        mEmptyPaint.setStrokeWidth(mArcWidth);
        mEmptyPaint.setColor(mArcBgColor);
        mEmptyPaint.setStrokeCap(Paint.Cap.ROUND);

        // 底部文字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(dp2px(getResources(), 16));
        mTextPaint.setColor(textColor);

        if (iconRes != -1) {
            mBitmap = drawableToBitmap(getResources().getDrawable(iconRes));
            mBitmapRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mArcRect = new RectF();
        mArcRect.top = 0;
        mArcRect.left = 0;
        mArcRect.right = w;
        mArcRect.bottom = h;

        mArcRect.inset(mArcWidth / 2, mArcWidth / 2);
        mArcRadius = (int) (mArcRect.width() / 2);

        double sqrt = Math.sqrt(mArcRadius * mArcRadius + mArcRadius * mArcRadius);
        bDistance = Math.cos(Math.PI * 45 / 180) * mArcRadius;
    }

    private float progressRect = 1;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        progressRect = 2.7F * mRealProgress;
        canvas.drawArc(mArcRect, 135 + progressRect, 270 - progressRect, false, mEmptyPaint);
        canvas.drawArc(mArcRect, 135, progressRect, false, mArcPaint);

        canvas.drawText("0", mArcRadius / 2F - mTextPaint.measureText("0") / 2,
                (float) (mArcRadius + bDistance) - 2 * (mTextPaint.descent() + mTextPaint.ascent()), mTextPaint);
        canvas.drawText("100", mArcRadius / 2F * 3 - mTextPaint.measureText("100") / 2,
                (float) (mArcRadius + bDistance) - 2 * (mTextPaint.descent() + mTextPaint.ascent()), mTextPaint);

        if (mBitmap != null) {
            drawBitmap(canvas);
        }
    }

    private void drawBitmap(Canvas canvas) {
        //将角度转换为弧度
        float calcProgress = (progressRect + 225) % 360;
        float arcAngle = (float) (Math.PI * calcProgress / 180.0);
        float bitmapX;
        float bitmapY;
        if (calcProgress < 90) {
            bitmapX = mArcRadius + (float) (Math.cos(arcAngle)) * mArcRadius + mBitmap.getWidth() / 3F;
            bitmapY = mArcRadius - (float) (Math.sin(arcAngle)) * mArcRadius + mBitmap.getHeight() / 3F;
        } else if (calcProgress == 90) {
            bitmapX = mArcRadius + mArcRadius + mBitmap.getWidth() / 3F;
            bitmapY = mArcRadius + mBitmap.getWidth() / 3F;
        } else if (calcProgress > 90 && calcProgress < 180) {
            arcAngle = (float) (Math.PI * (180 - calcProgress) / 180.0);
            bitmapX = mArcRadius * 2 - (float) (Math.cos(arcAngle)) * mArcRadius + mBitmap.getWidth() / 3F;
            bitmapY = mArcRadius * 2 - (float) (Math.sin(arcAngle)) * mArcRadius + mBitmap.getHeight() / 3F;
        } else if (calcProgress == 180) {
            bitmapX = mArcRadius;
            bitmapY = mArcRadius * 2;
        } else if (calcProgress > 180 && calcProgress < 270) {
            arcAngle = (float) (Math.PI * (calcProgress - 180) / 180.0);
            bitmapX = mArcRadius - (float) (Math.sin(arcAngle)) * mArcRadius - mBitmap.getHeight() / 3F;
            bitmapY = 2 * mArcRadius - (float) (Math.sin(arcAngle)) * mArcRadius + mBitmap.getWidth() / 3F;
        } else if (calcProgress == 270) {
            bitmapX = 0;
            bitmapY = mArcRadius;
        } else {
            arcAngle = (float) (Math.PI * (360 - calcProgress) / 180.0);
            bitmapX = mArcRadius - (float) (Math.sin(arcAngle)) * mArcRadius - mBitmap.getHeight() / 3F;
            bitmapY = mArcRadius - (float) (Math.cos(arcAngle)) * mArcRadius - mBitmap.getHeight() / 3F;
        }
        matrix.setRotate((calcProgress + 90) % 360, mBitmap.getWidth() / 2F, mBitmap.getHeight() / 2F);
        Bitmap bitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
        canvas.drawBitmap(bitmap, bitmapX, bitmapY, mEmptyPaint);
    }


    public void restart() {
        isRestart = true;
        this.mRealProgress = 0;
        invalidate();
    }

    /**
     * 设置最大进度
     *
     * @param max
     */
    public void setMaxProgress(int max) {
        this.mProgressMax = max;
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        // 进度100% = 控件的75%
        this.mRealProgress = progress;
        isRestart = false;
        postInvalidate();
    }

    private float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }
}

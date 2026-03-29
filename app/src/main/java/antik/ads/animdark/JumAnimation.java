package antik.ads.animdark;


/*
 * Created by aantik
 * 3/29/2026 1:10 PM
 *
 *   ⋆    ႔ ႔
 *     ᠸ^ ^ ⸝⸝
 *       |、˜〵
 *      じしˍ,)⁐̤ᐷ
 *
 * Fox Mode 🍺
 */


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;

@SuppressLint("ViewConstructor")
public class JumAnimation extends View {

    private Bitmap mBackground;
    private Paint mPaint;
    private int mMaxRadius, mStartRadius, mCurrentRadius;
    private boolean isStarted;
    private long mDuration = 500;
    private final float mStartX;
    private final float mStartY;
    private final ViewGroup mRootView;

    public static JumAnimation create(View onClickView) {
        Context context = onClickView.getContext();
        int w = onClickView.getWidth() / 2;
        int h = onClickView.getHeight() / 2;
        float x = getAbsoluteX(onClickView) + w;
        float y = getAbsoluteY(onClickView) + h;
        return new JumAnimation(context, x, y, Math.max(w, h));
    }

    private JumAnimation(Context context, float startX, float startY, int radius) {
        super(context);
        mRootView = (ViewGroup) getActivity(context).getWindow().getDecorView();
        mStartX = startX;
        mStartY = startY;
        mStartRadius = radius;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        updateMaxRadius();
    }

    private Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
        if (context instanceof Activity) return (Activity) context;
        context = ((ContextWrapper) context).getBaseContext();
        }
        throw new RuntimeException("activity not found..!!");
    }

    public void setDuration(long duration) {
    mDuration = duration;
    }

    public void start() {
        if (isStarted) return;
        isStarted = true;
        setLayerType(LAYER_TYPE_HARDWARE, null);
        updateBackground();
        attach();
        ValueAnimator anim = ValueAnimator.ofFloat(0, mMaxRadius);
        anim.setDuration(mDuration);
        anim.addUpdateListener(animation -> {
        mCurrentRadius = (int) (float) animation.getAnimatedValue() + mStartRadius;
        invalidate();
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            isStarted = false;
            detach();
            }
         });
        anim.start();
    }

    private void attach() {
    setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    mRootView.addView(this);
    }

    private void detach() {
        if (mRootView != null) {
        mRootView.removeView(this);
        }
        if (mBackground != null && !mBackground.isRecycled()) {
        mBackground.recycle();
        }
        mBackground = null;
    }

    private void updateBackground() {
        if (mBackground != null && mBackground.getWidth() == mRootView.getWidth() && mBackground.getHeight() == mRootView.getHeight()) {
        Canvas canvas = new Canvas(mBackground);
        mRootView.draw(canvas);
        return;
        }
        if (mBackground != null && !mBackground.isRecycled()) {
        mBackground.recycle();
        }
        mBackground = Bitmap.createBitmap(mRootView.getWidth(), mRootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBackground);
        mRootView.draw(canvas);
    }

    private void updateMaxRadius() {
        float dx = Math.max(mStartX, mRootView.getWidth() - mStartX);
        float dy = Math.max(mStartY, mRootView.getHeight() - mStartY);
        mMaxRadius = (int) Math.hypot(dx, dy);
    }

    private static float getAbsoluteX(View v) {
        float x = v.getX();
        ViewParent p = v.getParent();
        if (p instanceof View) x += getAbsoluteX((View) p);
        return x;
    }

    private static float getAbsoluteY(View v) {
        float y = v.getY();
        ViewParent p = v.getParent();
        if (p instanceof View) y += getAbsoluteY((View) p);
        return y;
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int layer;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        } else {
        layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        }
        canvas.drawBitmap(mBackground, 0, 0, null);
        canvas.drawCircle(mStartX, mStartY, mCurrentRadius, mPaint);
        canvas.restoreToCount(layer);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    return true;
    }
}
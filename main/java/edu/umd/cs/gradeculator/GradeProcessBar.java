package edu.umd.cs.gradeculator;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//    Credits
//    This application uses Open Source components.
//    You can find the source code of their open source projects along with license information below.
//    We acknowledge and are grateful to these developers for their contributions to open source.
//
//    Project: ArcProgressStackView https://github.com/Devlight/ArcProgressStackView/
//    Copyright (c) 2015 Basil Miller
//    License (MIT) https://github.com/Devlight/ArcProgressStackView/blob/master/LICENSE.txt
//
//    The following code is a modification of the ArcProgressStackView above with less features.

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GradeProcessBar extends View {

    // Default values
    private final static float DEFAULT_START_ANGLE = 180.0F;
    private final static float DEFAULT_SWEEP_ANGLE = 180.0F;
    private final static float DEFAULT_DRAW_WIDTH_FRACTION = 0.7F;
    private final static float DEFAULT_MODEL_OFFSET = 5.0F;
    private final static int DEFAULT_ANIMATION_DURATION = 350;
    private final static int DEFAULT_ACTION_MOVE_ANIMATION_DURATION = 150;

    // Max and min progress values
    private final static float MAX_PROGRESS = 100.0F;
    private final static float MIN_PROGRESS = 0.0F;

    // Max and min fraction values
    private final static float MAX_FRACTION = 1.0F;
    private final static float MIN_FRACTION = 0.0F;

    // Max and min end angle
    private final static float MAX_ANGLE = 180.0F;
    private final static float MIN_ANGLE = 0.0F;


    // Action move constants
    private final static float POSITIVE_ANGLE = 90.0F;
    private final static float NEGATIVE_ANGLE = 270.0F;
    private final static int POSITIVE_SLICE = 1;
    private final static int NEGATIVE_SLICE = -1;
    private final static int DEFAULT_SLICE = 0;
    private final static int ANIMATE_ALL_INDEX = -2;
    private final static int DISABLE_ANIMATE_INDEX = -1;


    // Start and end angles
    private float mStartAngle;
    private float mSweepAngle;

    // Progress models
    private List<Model> mModels = new ArrayList<>();

    // Progress and text paints
    private final Paint mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setDither(true);
            setStyle(Style.STROKE);
            setStrokeCap(Paint.Cap.ROUND);
            setStrokeJoin(Paint.Join.ROUND);
        }
    };
    private final TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG) {
        {
            setDither(true);
            setTextAlign(Align.LEFT);
        }
    };

    // ValueAnimator and interpolator for progress animating
    private final ValueAnimator mProgressAnimator = new ValueAnimator();
    private ValueAnimator.AnimatorListener mAnimatorListener;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    //private Interpolator mInterpolator;
    private int mAnimationDuration;
    private float mAnimatedFraction;

    // Square size of view
    private int mSize;

    // Offsets for handling and radius of progress models
    private float mProgressModelSize;
    private float mProgressModelOffset;
    private float mDrawWidthFraction;
    private float mDrawWidthDimension;

    // Boolean variables
    private boolean mIsAnimated;

    // Colors
    //private int mShadowColor;
    private int mTextColor;
    private int mPreviewModelBgColor;

    // Action move variables
    private int mActionMoveModelIndex = DISABLE_ANIMATE_INDEX;
    private int mActionMoveLastSlice = 0;
    private int mActionMoveSliceCounter;

    // Is >= VERSION_CODES.HONEYCOMB
    private boolean mIsFeaturesAvailable;

    public GradeProcessBar(final Context context) {
        this(context, null);
    }

    public GradeProcessBar(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradeProcessBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Init CPSV

        // Always draw
        setWillNotDraw(false);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, null);

        // Detect if features available
        mIsFeaturesAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

        // Retrieve attributes from xml
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressStackView);
        try {
            setIsAnimated(
                    typedArray.getBoolean(R.styleable.ArcProgressStackView_apsv_animated, true)
            );
            setTypeface(
                    typedArray.getString(R.styleable.ArcProgressStackView_apsv_typeface)
            );
            setTextColor(
                    typedArray.getColor(
                            R.styleable.ArcProgressStackView_apsv_text_color,
                            Color.WHITE
                    )
            );
            setAnimationDuration(
                    typedArray.getInteger(
                            R.styleable.ArcProgressStackView_apsv_animation_duration,
                            DEFAULT_ANIMATION_DURATION
                    )
            );
            setStartAngle(
                    typedArray.getInteger(
                            R.styleable.ArcProgressStackView_apsv_start_angle,
                            (int) DEFAULT_START_ANGLE
                    )
            );
            setSweepAngle(
                    typedArray.getInteger(
                            R.styleable.ArcProgressStackView_apsv_sweep_angle,
                            (int) DEFAULT_SWEEP_ANGLE
                    )
            );
            setProgressModelOffset(
                    typedArray.getDimension(
                            R.styleable.ArcProgressStackView_apsv_model_offset,
                            DEFAULT_MODEL_OFFSET
                    )
            );

            // Set animation info if is available
            if (mIsFeaturesAvailable) {
                mProgressAnimator.setFloatValues(MIN_FRACTION, MAX_FRACTION);
                mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(final ValueAnimator animation) {
                        mAnimatedFraction = (float) animation.getAnimatedValue();
                        if (mAnimatorUpdateListener != null)
                            mAnimatorUpdateListener.onAnimationUpdate(animation);

                        postInvalidate();
                    }
                });
            }

            // Check whether draw width dimension or fraction

            setDrawWidthFraction(DEFAULT_DRAW_WIDTH_FRACTION);

            // Set preview models
            if (isInEditMode()) {
                String[] preview = null;
                try {
                    final int previewId = typedArray.getResourceId(
                            R.styleable.ArcProgressStackView_apsv_preview_colors, 0
                    );
                    preview = previewId == 0 ? null : typedArray.getResources().getStringArray(previewId);
                } catch (Exception exception) {
                    preview = null;
                    exception.printStackTrace();
                } finally {
                    if (preview == null)
                        preview = typedArray.getResources().getStringArray(R.array.default_preview);

                    final Random random = new Random();
                    for (String previewColor : preview)
                        mModels.add(
                                new Model("", random.nextInt((int) MAX_PROGRESS), Color.parseColor(previewColor))
                        );
                    measure(mSize, mSize);
                }

                // Set preview model bg color
                mPreviewModelBgColor = typedArray.getColor(
                        R.styleable.ArcProgressStackView_apsv_preview_bg,
                        Color.LTGRAY
                );
            }
        } finally {
            typedArray.recycle();
        }
    }

    public ValueAnimator getProgressAnimator() {
        return mProgressAnimator;
    }

    public long getAnimationDuration() {
        return mAnimationDuration;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setAnimationDuration(final long animationDuration) {
        mAnimationDuration = (int) animationDuration;
        mProgressAnimator.setDuration(animationDuration);
    }

    public ValueAnimator.AnimatorListener getAnimatorListener() {
        return mAnimatorListener;
    }

    public void setAnimatorListener(final ValueAnimator.AnimatorListener animatorListener) {
        if (mAnimatorListener != null) mProgressAnimator.removeListener(mAnimatorListener);

        mAnimatorListener = animatorListener;
        mProgressAnimator.addListener(animatorListener);
    }

    public ValueAnimator.AnimatorUpdateListener getAnimatorUpdateListener() {
        return mAnimatorUpdateListener;
    }

    public void setAnimatorUpdateListener(final ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        mAnimatorUpdateListener = animatorUpdateListener;
    }

    public float getStartAngle() {
        return mStartAngle;
    }

    @SuppressLint("SupportAnnotationUsage")
    @FloatRange
    public void setStartAngle(@FloatRange(from = MIN_ANGLE, to = MAX_ANGLE) final float startAngle) {
        mStartAngle = Math.max(MIN_ANGLE, Math.min(startAngle, MAX_ANGLE));
        postInvalidate();
    }

    public float getSweepAngle() {
        return mSweepAngle;
    }

    @SuppressLint("SupportAnnotationUsage")
    @FloatRange
    public void setSweepAngle(@FloatRange(from = MIN_ANGLE, to = MAX_ANGLE) final float sweepAngle) {
        mSweepAngle = Math.max(MIN_ANGLE, Math.min(sweepAngle, MAX_ANGLE));
        postInvalidate();
    }

    public List<Model> getModels() {
        return mModels;
    }

    public void setModels(final List<Model> models) {
        mModels.clear();
        mModels = models;
        requestLayout();
    }

    public int getSize() {
        return mSize;
    }

    public float getProgressModelSize() {
        return mProgressModelSize;
    }

    public boolean isAnimated() {
        return mIsAnimated;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setIsAnimated(final boolean isAnimated) {
        mIsAnimated = mIsFeaturesAvailable && isAnimated;
    }

    public float getProgressModelOffset() {
        return mProgressModelOffset;
    }

    public void setProgressModelOffset(final float progressModelOffset) {
        mProgressModelOffset = progressModelOffset;
        requestLayout();
    }

    public float getDrawWidthFraction() {
        return mDrawWidthFraction;
    }

    @SuppressLint("SupportAnnotationUsage")
    @FloatRange
    public void setDrawWidthFraction(@FloatRange(from = MIN_FRACTION, to = MAX_FRACTION) final float drawWidthFraction) {
        // Divide by half for radius and reset
        mDrawWidthFraction = Math.max(MIN_FRACTION, Math.min(drawWidthFraction, MAX_FRACTION)) * 0.5F;
        mDrawWidthDimension = MIN_FRACTION;
        requestLayout();
    }

    public float getDrawWidthDimension() {
        return mDrawWidthDimension;
    }

    public void setDrawWidthDimension(final float drawWidthDimension) {
        mDrawWidthFraction = MIN_FRACTION;
        mDrawWidthDimension = drawWidthDimension;
        requestLayout();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(final int textColor) {
        mTextColor = textColor;
        mTextPaint.setColor(textColor);
        postInvalidate();
    }

    public void setTypeface(final String typeface) {
        Typeface tempTypeface;
        try {
            if (isInEditMode()) return;
            tempTypeface = Typeface.createFromAsset(getContext().getAssets(), typeface);
        } catch (Exception e) {
            tempTypeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
            e.printStackTrace();
        }

        setTypeface(tempTypeface);
    }

    public void setTypeface(final Typeface typeface) {
        mTextPaint.setTypeface(typeface);
        postInvalidate();
    }

    // Animate progress
    public void animateProgress() {
        if (!mIsAnimated || mProgressAnimator == null) return;
        if (mProgressAnimator.isRunning()) {
            if (mAnimatorListener != null) mProgressAnimator.removeListener(mAnimatorListener);
            mProgressAnimator.cancel();
        }
        // Set to animate all models
        mActionMoveModelIndex = ANIMATE_ALL_INDEX;
        mProgressAnimator.setDuration(mAnimationDuration);
        //mProgressAnimator.setInterpolator(mInterpolator);
        if (mAnimatorListener != null) {
            mProgressAnimator.removeListener(mAnimatorListener);
            mProgressAnimator.addListener(mAnimatorListener);
        }
        mProgressAnimator.start();
    }

    // Animate progress
    private void animateActionMoveProgress() {
        if (!mIsAnimated || mProgressAnimator == null) return;
        if (mProgressAnimator.isRunning()) return;

        mProgressAnimator.setDuration(DEFAULT_ACTION_MOVE_ANIMATION_DURATION);
        mProgressAnimator.setInterpolator(null);
        if (mAnimatorListener != null) mProgressAnimator.removeListener(mAnimatorListener);
        mProgressAnimator.start();
    }

    // Get the angle of action move model
    private float getActionMoveAngle(final float x, final float y) {
        //Get radius
        final float radius = mSize * 0.5F;

        // Get degrees without offset
        float degrees = (float) ((Math.toDegrees(Math.atan2(y - radius, x - radius)) + 360.0F) % 360.0F);
        if (degrees < 0) degrees += 2.0F * Math.PI;

        // Get point with offset relative to start angle
        final float newActionMoveX =
                (float) (radius * Math.cos((degrees - mStartAngle) / 180.0F * Math.PI));
        final float newActionMoveY =
                (float) (radius * Math.sin((degrees - mStartAngle) / 180.0F * Math.PI));

        // Set new angle with offset
        degrees = (float) ((Math.toDegrees(Math.atan2(newActionMoveY, newActionMoveX)) + 360.0F) % 360.0F);
        if (degrees < 0) degrees += 2.0F * Math.PI;

        return degrees;
    }

    private void handleActionMoveModel(final MotionEvent event) {
        if (mActionMoveModelIndex == DISABLE_ANIMATE_INDEX) return;

        // Get current move angle
        float currentAngle = getActionMoveAngle(event.getX(), event.getY());

        // Check if angle in slice zones
        final int actionMoveCurrentSlice;
        if (currentAngle > MIN_ANGLE && currentAngle < POSITIVE_ANGLE)
            actionMoveCurrentSlice = POSITIVE_SLICE;
        else if (currentAngle > NEGATIVE_ANGLE && currentAngle < MAX_ANGLE)
            actionMoveCurrentSlice = NEGATIVE_SLICE;
        else actionMoveCurrentSlice = DEFAULT_SLICE;

        // Check for handling counter
        if (actionMoveCurrentSlice != 0 &&
                ((mActionMoveLastSlice == NEGATIVE_SLICE && actionMoveCurrentSlice == POSITIVE_SLICE) ||
                        (actionMoveCurrentSlice == NEGATIVE_SLICE && mActionMoveLastSlice == POSITIVE_SLICE))) {
            if (mActionMoveLastSlice == NEGATIVE_SLICE) mActionMoveSliceCounter++;
            else mActionMoveSliceCounter--;

            // Limit counter for 1 and -1, we don`t need take the race
            if (mActionMoveSliceCounter > 1) mActionMoveSliceCounter = 1;
            else if (mActionMoveSliceCounter < -1) mActionMoveSliceCounter = -1;
        }
        mActionMoveLastSlice = actionMoveCurrentSlice;

        // Set total traveled angle
        float actionMoveTotalAngle = currentAngle + (MAX_ANGLE * mActionMoveSliceCounter);
        final Model model = mModels.get(mActionMoveModelIndex);

        // Check whether traveled angle out of limit
        if (actionMoveTotalAngle < MIN_ANGLE || actionMoveTotalAngle > MAX_ANGLE) {
            actionMoveTotalAngle =
                    actionMoveTotalAngle > MAX_ANGLE ? MAX_ANGLE + 1.0F : -1.0F;
            currentAngle = actionMoveTotalAngle;
        }

        // Set model progress and invalidate
        float touchProgress = Math.round(MAX_PROGRESS / mSweepAngle * currentAngle);
        model.setProgress(touchProgress);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        // Get measured sizes
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        // Get sie for square dimension
        if (width > height) mSize = height;
        else mSize = width;

        // Get progress offsets
        final float divider = mDrawWidthFraction == 0 ? mDrawWidthDimension : mSize * mDrawWidthFraction;
        mProgressModelSize = divider / mModels.size();
        final float paintOffset = mProgressModelSize * 0.5F;

        // Set bound with offset for models
        for (int i = 0; i < mModels.size(); i++) {
            final Model model = mModels.get(i);
            final float modelOffset = (mProgressModelSize * i) +
                    (paintOffset) - (mProgressModelOffset * i);

            // Set bounds to progress
            model.mBounds.set(
                    modelOffset, modelOffset,
                    mSize - modelOffset, mSize - modelOffset
            );
        }

        // Set square measured dimension
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        // Save and rotate to start angle
        canvas.save();
        final float radius = mSize * 0.5F;
        canvas.rotate(mStartAngle, radius, radius);

        // Draw all of progress
        for (int i = 0; i < mModels.size(); i++) {
            final Model model = mModels.get(i);
            // Get progress for current model
            float progressFraction = mIsAnimated && !isInEditMode() ? (model.mLastProgress + (mAnimatedFraction *
                    (model.getProgress() - model.mLastProgress))) / MAX_PROGRESS :
                    model.getProgress() / MAX_PROGRESS;
            if (i != mActionMoveModelIndex && mActionMoveModelIndex != ANIMATE_ALL_INDEX)
                progressFraction = model.getProgress() / MAX_PROGRESS;
            final float progress = progressFraction * mSweepAngle;

            mProgressPaint.setStrokeWidth(mProgressModelSize);

            // Set model arc progress
            model.mPath.reset();
            model.mPath.addArc(model.mBounds, 0.0F, progress);

            // Draw gradient progress or solid
            //resetShadowLayer();
            mProgressPaint.setShader(null);
            mProgressPaint.setStyle(Paint.Style.STROKE);

            // BACKGROUNDED
            //noinspection ResourceAsColor
            mProgressPaint.setColor(isInEditMode() ? mPreviewModelBgColor : model.getBgColor());
            canvas.drawArc(model.mBounds, 0.0F, mSweepAngle, false, mProgressPaint);
            if (!isInEditMode()) mProgressPaint.clearShadowLayer();
            mProgressPaint.setColor(model.getColor());

            // Here we draw main progress
            mProgressPaint.setAlpha(255);
            canvas.drawPath(model.mPath, mProgressPaint);

            // Preview mode
            if (isInEditMode()) continue;

            // Get model title bounds
            mTextPaint.setTextSize(mProgressModelSize * 0.45F);
            mTextPaint.getTextBounds(
                    model.getTitle(),
                    0, model.getTitle().length(),
                    model.mTextBounds
            );

            // Draw title at start with offset
            final float titleHorizontalOffset = model.mTextBounds.height() * 0.45F;
            final float progressLength =
                    (float) (Math.PI / 180.0F) * progress * model.mBounds.width() * 0.45F;
            final String title = (String) TextUtils.ellipsize(
                    model.getTitle(), mTextPaint,
                    progressLength - titleHorizontalOffset * 2, TextUtils.TruncateAt.END
            );
            canvas.drawTextOnPath(
                    title,
                    model.mPath,
                    0.0F, titleHorizontalOffset,
                    mTextPaint
            );

            // Get pos and tan at final path point
            model.mPathMeasure.setPath(model.mPath, false);
            model.mPathMeasure.getPosTan(model.mPathMeasure.getLength(), model.mPos, model.mTan);

            // Get title width
            final float titleWidth = model.mTextBounds.width();

            // Create model progress like : 23%
            final String percentProgress = String.format("%d%%", (int) model.getProgress());
            // Get progress text bounds
            mTextPaint.setTextSize(mProgressModelSize * 0.45f);
            mTextPaint.getTextBounds(
                    percentProgress, 0, percentProgress.length(), model.mTextBounds
            );

            // Get pos tan with end point offset and check whether the rounded corners for offset
            final float progressHorizontalOffset = model.mTextBounds.width() * 0.5F;
            final float indicatorProgressOffset = (progressFraction) *
                    (-progressHorizontalOffset - titleHorizontalOffset
                            - (model.mTextBounds.height() * 2.0F));
            model.mPathMeasure.getPosTan(
                    model.mPathMeasure.getLength() + indicatorProgressOffset, model.mPos,
                    model.mTan
            );

            // Check if there available place for indicator
            if ((titleWidth + model.mTextBounds.height() + titleHorizontalOffset * 2.0F) -
                    indicatorProgressOffset < progressLength) {
                // Get rotate indicator progress angle for progress value
                float indicatorProgressAngle =
                        (float) (Math.atan2(model.mTan[1], model.mTan[0]) * (180.0F / Math.PI));
                // Get arc angle of progress indicator
                final float indicatorLengthProgressAngle = ((progressLength + indicatorProgressOffset) /
                        (model.mBounds.width() * 0.5F)) * (float) (180.0F / Math.PI);

                final float y = (float) (model.mBounds.height() * 0.5F *
                        (Math.sin((indicatorLengthProgressAngle + mStartAngle) *
                                Math.PI / 180.0F))) + model.mBounds.centerY();
                indicatorProgressAngle += (y > radius) ? 180.0F : 0.0F;


                // Draw progress value
                canvas.save();
                canvas.rotate(indicatorProgressAngle, model.mPos[0], model.mPos[1]);
                canvas.drawText(
                        percentProgress,
                        model.mPos[0] - model.mTextBounds.exactCenterX(),
                        model.mPos[1] - model.mTextBounds.exactCenterY(),
                        mTextPaint
                );
                canvas.restore();
            }
        }

        // Restore after drawing
        canvas.restore();
    }

    public static class Model {

        private String mTitle;
        private float mLastProgress;
        private float mProgress;

        private int mColor;
        private int mBgColor;
        private int[] mColors;

        private final RectF mBounds = new RectF();
        private final Rect mTextBounds = new Rect();

        private final Path mPath = new Path();
        //private SweepGradient mSweepGradient;

        private final PathMeasure mPathMeasure = new PathMeasure();
        private final float[] mPos = new float[2];
        private final float[] mTan = new float[2];

        public Model(final String title, final float progress, final int color) {
            setTitle(title);
            setProgress(progress);
            setColor(color);
        }

        public Model(final String title, final float progress, final int[] colors) {
            setTitle(title);
            setProgress(progress);
            setColors(colors);
        }

        public Model(final String title, final float progress, final int bgColor, final int color) {
            setTitle(title);
            setProgress(progress);
            setColor(color);
            setBgColor(bgColor);
        }

        public Model(final String title, final float progress, final int bgColor, final int[] colors) {
            setTitle(title);
            setProgress(progress);
            setColors(colors);
            setBgColor(bgColor);
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(final String title) {
            mTitle = title;
        }

        public float getProgress() {
            return mProgress;
        }

        @FloatRange
        public void setProgress(@FloatRange(from = MIN_PROGRESS, to = MAX_PROGRESS) final float progress) {
            mLastProgress = mProgress;
            mProgress = (int) Math.max(MIN_PROGRESS, Math.min(progress, MAX_PROGRESS));
        }

        public int getColor() {
            return mColor;
        }

        public void setColor(final int color) {
            mColor = color;
        }

        public int getBgColor() {
            return mBgColor;
        }

        public void setBgColor(final int bgColor) {
            mBgColor = bgColor;
        }

        public int[] getColors() {
            return mColors;
        }

        public void setColors(final int[] colors) {
            if (colors != null && colors.length >= 2) mColors = colors;
            else mColors = null;
        }
    }

}
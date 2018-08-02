package com.lgb.myfitness.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 弧形进度条
 * 
 * @author Eric
 * 
 */
public class GoalProgressbar extends View {

	public GoalProgressbar(Context context) {
		super(context);
	}

	public GoalProgressbar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Log.d("ArcProgress", "(" + this.getWidth() + ", " + this.getHeight()
		// + ")");

		diameter = ((this.getWidth() + this.getHeight()) / 2);
		instance = diameter / 30;
		barStrokeWidth = (float) (instance * 1);
		init(canvas);
	}

	float mPointX = 0; // 中心点X
	float mPointY = 0; // 中心店Y
	float instance = 0; // 距离，距离太小会显示不全
	/**
	 * 直徑。
	 */
	private float diameter = 0;
	private float barStrokeWidth = 15; // 宽度
	
	private int color_progress = Color.WHITE; // 进度条颜色
	private int color_background = Color.GRAY; // 背景颜色
	
	private int progress = 0;
	
	private int startAngle = 100; // 起始角度
	private int sweepAngle = 340; // 展开角度
	
	private Paint mPaintProgress = null;
	private Paint mPaintBackground = null;
//	private Paint mPaintBall = null;
	
	private RectF rectDraw = null; // 绘制区域
	private boolean showProgressBackground = true;// 是否显示小背景。
//	private int angleOfMoveCircle = startAngle;

	private void init(Canvas canvas) {
		// 画弧形的矩阵区域。
		rectDraw = new RectF(instance, instance, diameter - instance, diameter
				- instance);

		// 计算弧形的圆心和半径。
		mPointX = (diameter) / 2;
		mPointY = (diameter) / 2;
		// Log.d("ArcProgress", "(" + mPointX + ", " + mPointY + "), " +
		// arcRadius);s

		// 背景
		if (showProgressBackground) {
			mPaintBackground = new Paint();
			mPaintBackground.setAntiAlias(true);
			mPaintBackground.setStyle(Style.STROKE);
			mPaintBackground.setStrokeWidth(barStrokeWidth);
			mPaintBackground.setColor(color_background);
			canvas.drawArc(rectDraw, startAngle, sweepAngle, false,
					mPaintBackground);
		}

		// 进度
		mPaintProgress = new Paint();
		mPaintProgress.setAntiAlias(true);
		mPaintProgress.setStyle(Style.STROKE);
		mPaintProgress.setStrokeWidth(barStrokeWidth);
		mPaintProgress.setColor(color_progress);
		canvas.drawArc(rectDraw, startAngle, progress * sweepAngle / 100, false, mPaintProgress);

//		mPaintBall = new Paint();
//		mPaintBall.setAntiAlias(true);
//		mPaintBall.setColor(Color.WHITE);
//		canvas.drawCircle(
//				(float) (mPointX + (diameter - 2 * instance) / 2
//						* Math.cos(angleOfMoveCircle * 3.14 / 180)),
//				(float) (mPointY + (diameter - 2 * instance) / 2
//						* Math.sin(angleOfMoveCircle * 3.14 / 180)),
//				(int) (barStrokeWidth), mPaintBall);
		
		invalidate();
	}

	public void setProgress(int progress) {
		this.progress = progress;
//		angleOfMoveCircle = startAngle + progress;
		invalidate();
	}

	public int getProgress() {
		return this.progress;
	}

	/**
	 * 设置弧形ProgressBar的颜色。
	 */
	public void setBarColor(int barColor) {
		this.color_progress = barColor;
	}

	/**
	 * 设置弧形小背景的颜色。
	 */
	public void setSmallBgColor(int smallBgColor) {
		this.color_background = smallBgColor;
	}

	/**
	 * 是否显示小背景。
	 */
	public void setShowSmallBg(boolean showSmallBg) {
		this.showProgressBackground = showSmallBg;
	}

}

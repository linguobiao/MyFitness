package com.lgb.myfitness.view;

import com.lgb.myfitness.helper.ChartHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class BPMLineValue  extends View{
	
	public static int TEXT_SIZE = 18;
	
	public BPMLineValue(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	private float x = 0;
	private String value = "0";
	private String xLabel = "2015-02-10";
	private String xLabel2 = "16:59:30";
	public void setDrawData(float x, String value, String xLabel, String xLabel2) {
		this.x = x;
		this.value = value;
		this.xLabel = xLabel;
		this.xLabel2 = xLabel2;
		
		invalidate();
	}
	

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		int interval = (width - ChartHelper.LEFT - ChartHelper.RIGHT) / 24;
		
		
		if (x != 0) {
			// 红色竖线
			Paint paint_line = new Paint();
			paint_line.setAntiAlias(true);
			paint_line.setStyle(Style.FILL_AND_STROKE);
			paint_line.setColor(Color.RED);
			canvas.drawLine(x, height - height / 5, x, 0, paint_line);
			
			// 红色方框
			Paint paint_rect = new Paint();
			paint_rect.setAntiAlias(true);
			paint_rect.setStyle(Style.FILL_AND_STROKE);
			paint_rect.setColor(Color.RED);
			canvas.drawRect(x - interval - 20, 0, x + interval + 20, 85, paint_rect);
			
			// 数值
			Paint paint_text_value = new Paint();
			paint_text_value.setColor(Color.WHITE);
			paint_text_value.setTextSize(TEXT_SIZE);
			paint_text_value.setTextAlign(Align.CENTER);
			canvas.drawText(value, x, 25, paint_text_value);
			
			// 时间
			Paint paint_text_label = new Paint();
			paint_text_label.setColor(Color.WHITE);
			paint_text_label.setTextSize(TEXT_SIZE);
			paint_text_label.setTextAlign(Align.CENTER);
			canvas.drawText(xLabel, x, 50, paint_text_label);
			
			// 时间2
			Paint paint_text_label2 = new Paint();
			paint_text_label2.setColor(Color.WHITE);
			paint_text_label2.setTextSize(TEXT_SIZE);
			paint_text_label2.setTextAlign(Align.CENTER);
			canvas.drawText(xLabel2, x, 75, paint_text_label2);
		}
	}

}

package com.lgb.myfitness.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewShow extends TextView{
	 private int m_rotate;  
     public TextViewShow (Context context) {
             super(context);                        
     }
     
     public TextViewShow(Context context, AttributeSet attrs) {
             super(context, attrs);
//             TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.LabelView);
//             this.m_rotate = array.getInt(0, 90);
     }
     
     public TextViewShow(Context context, AttributeSet attrs,int defStyle) {
             super(context, attrs,defStyle);                        
     }
     
     public int getMrotate(){
             return m_rotate;
     }
     public void setMrotate(int mrotate){
             m_rotate=mrotate;
     }
     
     protected void onDraw(Canvas canvas){
             canvas.translate(0, 0);
             canvas.rotate(-22, 0, getHeight());
             super.onDraw(canvas);
     }
     
     protected void onMeasury(int widthMeasureSpec,int heightMeasureSpec){
             super.measure(widthMeasureSpec, heightMeasureSpec);
             setMeasuredDimension(400, 400);
     }

}

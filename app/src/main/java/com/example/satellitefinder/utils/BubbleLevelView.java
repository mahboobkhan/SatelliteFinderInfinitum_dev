package com.example.satellitefinder.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.satellitefinder.R;

public final class BubbleLevelView extends View {

	public BubbleLevelView(Context context) {
		super(context);
		init();
	}

	public BubbleLevelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BubbleLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	//endregion

	private static final int PAINT_FLAGS = Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG;

	private final Paint paintOutline = new Paint(PAINT_FLAGS) {{
		setStyle(Style.STROKE);
		setColor(0xFFFFFFFF);
		setStrokeWidth(0);
	}};
	private final Paint paintBubble = new Paint(PAINT_FLAGS) {{
		//setStyle(Style.FILL);
		//setColor(0xFF57C61A);
		setBackgroundResource(R.drawable.water_drop);
	}};

	private final RectF bounds = new RectF();
	private final PointF center = new PointF();
	private float bubbleStepMultiplier;
	private float bubbleRadius;
	private final Path outlinePath = new Path();

	private float x;
	private float y;

	private void init() {
		setBackgroundResource(R.drawable.level_bg);
		//setBackgroundColor(0xFF57C61A); // Set the background color to white
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		float halfMinSize = (Math.min(w, h) * 0.9f) / 2.0f;
		center.set(w / 2.0f, h / 2.0f);
		bounds.set(center.x - halfMinSize, center.y - halfMinSize, center.x + halfMinSize, center.y + halfMinSize);
		bubbleStepMultiplier = halfMinSize;
		bubbleRadius = bounds.width() * 0.05f;

		updateOutline();
	}

	private void updateOutline() {
		float bubbleOutlineSize = bubbleRadius * 1.2f;

		outlinePath.reset();

		outlinePath.addRect(bounds, Path.Direction.CW);
		outlinePath.addCircle(center.x, center.y, bubbleOutlineSize, Path.Direction.CW);

		outlinePath.moveTo(bounds.left, center.y - bubbleOutlineSize);
		outlinePath.lineTo(bounds.left + bubbleOutlineSize / 2.0f, center.y - bubbleOutlineSize);
		outlinePath.moveTo(bounds.left, center.y + bubbleOutlineSize);
		outlinePath.lineTo(bounds.left + bubbleOutlineSize / 2.0f, center.y + bubbleOutlineSize);

		outlinePath.moveTo(center.x - bubbleOutlineSize, bounds.top);
		outlinePath.lineTo(center.x - bubbleOutlineSize, bounds.top + bubbleOutlineSize / 2.0f);
		outlinePath.moveTo(center.x + bubbleOutlineSize, bounds.top);
		outlinePath.lineTo(center.x + bubbleOutlineSize, bounds.top + bubbleOutlineSize / 2.0f);

		outlinePath.moveTo(bounds.right, center.y - bubbleOutlineSize);
		outlinePath.lineTo(bounds.right - bubbleOutlineSize / 2.0f, center.y - bubbleOutlineSize);
		outlinePath.moveTo(bounds.right, center.y + bubbleOutlineSize);
		outlinePath.lineTo(bounds.right - bubbleOutlineSize / 2.0f, center.y + bubbleOutlineSize);

		outlinePath.moveTo(center.x - bubbleOutlineSize, bounds.bottom);
		outlinePath.lineTo(center.x - bubbleOutlineSize, bounds.bottom - bubbleOutlineSize / 2.0f);
		outlinePath.moveTo(center.x + bubbleOutlineSize, bounds.bottom);
		outlinePath.lineTo(center.x + bubbleOutlineSize, bounds.bottom - bubbleOutlineSize / 2.0f);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawCircle(
				center.x + (x * bubbleStepMultiplier),
				center.y + (y * bubbleStepMultiplier),
				bubbleRadius, paintBubble
		);
		canvas.drawPath(outlinePath, paintOutline);
	}

	public void setData(float x, float y) {
		this.x = x;
		this.y = y;

		invalidate();
	}
}

package com.example.vp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Scroller;

import com.example.vp.Zhang_ViewPager.onPageScrollStateChanged;
import com.example.vp.Zhang_ViewPager.onPagerClick;
import com.example.vp.Zhang_ViewPager.onPagerScrolled;

/**
 * @author 张小康 E-mail:wudkj@163.com
 * @version 3.1
 *          <p>
 *          创建时间：2015-1-7 下午15:58:16
 *          <p>
 *          完成时间：2014-1-7 下午9:40:15
 *          <p>
 *          使用介绍：通过构造函数将上下文对象和要显示的ImageViews传入，
 *          然后调用内部除setViewPagerAndPoint之外的其他方法
 *          配置图下面要显示的点的位置，大小以及它的图标参数（支持从drawable获取），最后调用setViewPagerAndPoint方法，
 *          最后会返回一个FrameLayout，将这个返回的layout加入到你要显示的页面的布局中就可以了。
 *          <p>
 *          注：如果要使用viewpager的点击事件，调用setPagerClick方法。
 *          <p>
 *          调用举例： 1：找到要加入的布局 li=(LinearLayout) findViewById(R.id.li);
 *          2：实例化一个Arraylist并给他传入你要显示的imageview
 *          （这个要自己生成，因为我不知道你是用的是drawable还是其他什么别的） ArrayList<ImageView>
 *          imageViews=new ArrayList<ImageView>(); imageViews.add(ImageView);
 *          3：通过构造函数传入上下文与imageViews Zhang_ViewPagerAndPoint vp=new
 *          Zhang_ViewPagerAndPoint(MainActivity.this, imageViews);
 *          4:调用方法生成整体的layout FrameLayout ly=vp.setViewPagerAndPoint();
 *          5:将生成的layout加入到你要用的布局中 li.addView(ly);
 * 
 */
public class Zhang_ViewPagerAndPoint {
	private Context context;
	private View[] dots;
	private MyVPAdapter adapter;
	private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
	/**
	 * 默认右边距为0
	 */
	private int mRightMargin = 0;
	/**
	 * 默认左边距为0
	 */
	private int mLeftMargin = 0;
	/**
	 * 默认为{@link #GRAVITY_LINE_CENTER}显示，其他还有{@link #GRAVITY_LINE_LEFT},
	 * {@link #GRAVITY_LINE_RIGHT}
	 */
	private int mGravityType = 0;
	private onPagerClick pagerClick;
	private onPageScrollStateChanged pagerScrollStateChanged;
	private onPagerScrolled pagerScrolled;
	private int dot_focus = android.R.drawable.presence_online;
	private int dot_normal = android.R.drawable.presence_invisible;
	/**
	 * 点的高度，默认为15
	 */
	private int dotHeight = 15;
	/**
	 * 向下的边距，默认35
	 */
	private int bottomMargings = 35;
	/**
	 * 图片滑动到下一张需要的时间，默认为1000毫秒
	 */
	private int scrollDuration = 1000;
	/**
	 * 居中显示
	 */
	public static final int GRAVITY_LINE_CENTER = 0;
	/**
	 * 靠右显示
	 */
	public static final int GRAVITY_LINE_RIGHT = 1;
	/**
	 * 靠左显示
	 */
	public static final int GRAVITY_LINE_LEFT = 2;
	/**
	 * 默认为不滚动
	 */
	private boolean startRoll=false;
	public static class Builder {
		private Context context;
		private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
		private int mRightMargin = 0;
		private int mGravityType = 0;
		private int mLeftMargin = 0;
		public static final int GRAVITY_LINE_CENTER = 0;
		public static final int GRAVITY_LINE_RIGHT = 1;
		public static final int GRAVITY_LINE_LEFT = 2;
		private int dot_focus = android.R.drawable.presence_online;
		private int dot_normal = android.R.drawable.presence_invisible;
		private onPagerClick pagerClick;
		private int dotHeight = 15;
		private int bottomMargings = 35;
		private onPageScrollStateChanged pagerScrollStateChanged;
		private onPagerScrolled pagerScrolled;
		private int scrollDuration = 1000;
		private int time;
		private int animationType;
		private boolean startRoll=false;

		/**
		 * @param animationType
		 *            设置滑动的动画效果
		 */
		public Builder setAnimationType(int animationType) {
			this.animationType = animationType;
			return this;
		}

		/**
		 * @param context
		 *            上下文
		 * @param imageViews
		 *            要显示的imageView的集合
		 */
		public Builder(Context context, ArrayList<ImageView> imageViews) {
			this.context = context;
			this.imageViews = imageViews;
		}

		/**
		 * @param scrollDuration
		 *            设置动画滑动的时间，默认为1秒
		 */
		public Builder scrollDuration(int scrollDuration) {
			this.scrollDuration = scrollDuration;
			return this;
		}

		/**
		 * @param handler
		 *            new一个handler就可以 是否要开启定时滑动
		 */
		public Builder startRoll() {
			startRoll=true;
			return this;
		}

		/**
		 * @param time
		 *            滚动的间隔
		 */
		public Builder setTime(int time) {
			this.time = time;
			return this;
		}

		/**
		 * @param mGravityType
		 *            包含有点的那一行的显示位置 GRAVITY_LINE_CENTER GRAVITY_LINE_RIGHT
		 *            GRAVITY_LINE_LEFT
		 */
		public Builder dotMarginType(int mGravityType) {
			this.mGravityType = mGravityType;
			return this;
		}

		/**
		 * @param mRightMargin
		 *            靠右的距离
		 */
		public Builder dotRightMargin(int mRightMargin) {
			this.mRightMargin = mRightMargin;
			return this;
		}

		/**
		 * @param mLeftMargin
		 *            靠左的距离
		 */
		public Builder dotLeftMargin(int mLeftMargin) {
			this.mLeftMargin = mLeftMargin;
			return this;
		}

		/**
		 * @param clickListener
		 *            每个页面点击后的事件
		 */
		public Builder OnPageClickListener(onPagerClick clickListener) {
			this.pagerClick = clickListener;
			return this;
		}

		/**
		 * @param dot_focus
		 *            被选中的点的ResId
		 */
		public Builder dotFocus(int dot_focus) {
			this.dot_focus = dot_focus;
			return this;
		}

		/**
		 * @param dot_normal
		 *            没被选中的点的ResId
		 */
		public Builder dotNormal(int dot_normal) {
			this.dot_normal = dot_normal;
			return this;
		}

		/**
		 * @param dotHeight
		 *            点的高度
		 */
		public Builder dotHeight(int dotHeight) {
			this.dotHeight = dotHeight;
			return this;
		}

		/**
		 * @param bottomMargings
		 *            距下边的高度
		 */
		public Builder bottomMarging(int bottomMargings) {
			this.bottomMargings = bottomMargings;
			return this;
		}

		/**
		 * @param pagerScrollStateChanged
		 *            滚动状态的监听事件
		 */
		public void setOnPagerScrollStateChanged(onPageScrollStateChanged pagerScrollStateChanged) {
			this.pagerScrollStateChanged = pagerScrollStateChanged;
		}

		/**
		 * @param pagerScrolled
		 *            滚动的监听事件
		 */
		public void setOnPagerScrolled(onPagerScrolled pagerScrolled) {
			this.pagerScrolled = pagerScrolled;
		}

		public FrameLayout build() {
			Zhang_ViewPagerAndPoint vp = new Zhang_ViewPagerAndPoint(context, imageViews);
			vp.setBottomMargings(bottomMargings);
			vp.setDot_focus(dot_focus);
			vp.setDot_normal(dot_normal);
			vp.setDotHeight(dotHeight);
			vp.setmGravityType(mGravityType);
			vp.setmRightMargin(mRightMargin);
			vp.setOnPagerClick(pagerClick);
			vp.setmLeftMargin(mLeftMargin);
			vp.setOnPagerScrolled(pagerScrolled);
			vp.setOnPagerScrollStateChanged(pagerScrollStateChanged);
			vp.startRoll(startRoll);
			vp.setTIME(time);
			vp.setScrollDuration(scrollDuration);
			vp.setAnimationType(animationType);
			return vp.setViewPagerAndPoint();
		}
	}

	/**
	 * @param pagerScrollStateChanged
	 *            滚动监听的监听事件
	 */
	public void setOnPagerScrollStateChanged(onPageScrollStateChanged pagerScrollStateChanged) {
		this.pagerScrollStateChanged = pagerScrollStateChanged;
	}

	/**
	 * @param pagerScrolled
	 *            滚动的监听事件
	 */
	public void setOnPagerScrolled(onPagerScrolled pagerScrolled) {
		this.pagerScrolled = pagerScrolled;
	}

	/**
	 * @param scrollDuration
	 *            设置动画滑动的时间，默认为1秒
	 */
	public void setScrollDuration(int scrollDuration) {
		this.scrollDuration = scrollDuration;
	}

	/**
	 * @param mLeftMargin
	 *            设置靠左的距离
	 */
	public void setmLeftMargin(int mLeftMargin) {
		this.mLeftMargin = mLeftMargin;
	}

	/**
	 * @param mGravityType
	 *            包含有点的那一行的显示位置 可使用{@link #GRAVITY_LINE_CENTER}来表示居中，或者
	 *            {@link #GRAVITY_LINE_LEFT}来表示 靠左，或者{@link #GRAVITY_LINE_RIGHT}
	 *            来表示靠右
	 */
	public void setmGravityType(int mGravityType) {
		this.mGravityType = mGravityType;
	}

	/**
	 * @param mRightMargin
	 *            设置相对于右的距离
	 */
	public void setmRightMargin(int mRightMargin) {
		this.mRightMargin = mRightMargin;
	}

	public Zhang_ViewPagerAndPoint(Context context, ArrayList<ImageView> imageViews) {
		this.imageViews = imageViews;
		this.context = context;
	}
	private Zhang_ViewPager viewPager;
	/**
	 * 程序入口,在此之前请对圆点进行设置，并将返回的FrameLayout加入到你要显示的页面的布局中
	 */
	public FrameLayout setViewPagerAndPoint() {
		viewPager = new Zhang_ViewPager(context);
		LinearLayout vpl = setViewPager(viewPager);
		LinearLayout dotl = setDots(viewPager);
		startInit(viewPager);
		ViewPagerScroller scroller = new ViewPagerScroller(context);
		scroller.setScrollDuration(scrollDuration);
		scroller.initViewPagerScroll(viewPager);
		if (startRoll) {
			initRunnable(viewPager, imageViews);
		}
		return setMainLinearLayout(vpl, dotl);
	}

	private FrameLayout setMainLinearLayout(LinearLayout vpl, LinearLayout dotl) {
		FrameLayout main_frame = new FrameLayout(context);
		main_frame.addView(vpl);
		FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, bottomMargings);
		param.gravity = Gravity.BOTTOM;
		param.rightMargin = mRightMargin;
		param.leftMargin = mLeftMargin;
		main_frame.addView(dotl, param);
		return main_frame;
	}

	private void startInit(Zhang_ViewPager viewPager) {
		adapter = new MyVPAdapter(imageViews);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(imageViews.size() * 10000);
	}

	/**
	 * @param bottomMargings
	 *            点到下面的距离
	 */
	public void setBottomMargings(int bottomMargings) {
		this.bottomMargings = bottomMargings;
	}

	/**
	 * @param {@link #dotHeight} 点的大小，是正方形的
	 */
	public void setDotHeight(int dotHeight) {
		this.dotHeight = dotHeight;
	}

	private LinearLayout setDots(Zhang_ViewPager viewPager) {
		setVPonChange(viewPager);
		dots = new View[imageViews.size()];
		LinearLayout dot_line = setLinearLayout();
		for (int i = 0; i < imageViews.size(); i++) {
			View dot = new View(context);
			LinearLayout.LayoutParams fl = new LinearLayout.LayoutParams(dotHeight, dotHeight);
			fl.leftMargin = 2;
			fl.rightMargin = 2;
			fl.gravity = Gravity.CENTER;
			dot.setLayoutParams(fl);
			if (i == 0) {
				dot.setBackgroundResource(dot_focus);
			} else {
				dot.setBackgroundResource(dot_normal);
			}
			dots[i] = dot;
			dot_line.addView(dot);
		}
		LinearLayout wDot = new LinearLayout(context);
		wDot.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		param.bottomMargin = bottomMargings;
		wDot.setLayoutParams(param);
		wDot.addView(dot_line);
		return wDot;
	}

	/**
	 * @param dot_normal
	 *            未选中状态下的原点图标
	 */
	public void setDot_normal(int dot_normal) {
		this.dot_normal = dot_normal;
	}

	/**
	 * @param dot_focus
	 *            选中状态下的原点图标
	 */
	public void setDot_focus(int dot_focus) {
		this.dot_focus = dot_focus;
	}

	/**
	 * @param pagerClick
	 *            设置页面被点击后的触发事件
	 */
	public void setOnPagerClick(onPagerClick pagerClick) {
		this.pagerClick = pagerClick;
	}

	private boolean isContinue = true;

	private void setVPonChange(final Zhang_ViewPager viewPager) {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(final int arg0) {
				for (int i = 0; i < dots.length; i++) {
					dots[i].setBackgroundResource(dot_normal);
				}
				dots[arg0 % imageViews.size()].setBackgroundResource(dot_focus);
				imageViews.get(viewPager.getCurrentItem() % imageViews.size()).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (pagerClick != null) {
							pagerClick.pagerDoSomething(v, viewPager.getCurrentItem() % imageViews.size());
						}
					}
				});
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (pagerScrolled != null) {
					pagerScrolled.pagerScrolled(viewPager.getCurrentItem() % imageViews.size(), arg1, arg2);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (pagerScrollStateChanged != null) {
					pagerScrollStateChanged.pagerScrollStateChanged(viewPager.getCurrentItem());
				}
			}
		});
		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					isContinue = false;
					break;
				case MotionEvent.ACTION_UP:
					isContinue = true;
					break;
				default:
					isContinue = true;
					break;
				}
				return false;
			}
		});
	}

	private LinearLayout setLinearLayout() {
		LinearLayout dot_line = new LinearLayout(context);
		dot_line.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		switch (mGravityType) {
		case GRAVITY_LINE_RIGHT:
			param2.gravity = Gravity.RIGHT;
			break;
		case GRAVITY_LINE_LEFT:
			param2.gravity = Gravity.LEFT;
			break;
		case GRAVITY_LINE_CENTER:
			param2.gravity = Gravity.CENTER;
			break;
		default:
			param2.gravity = Gravity.CENTER;
			break;
		}
		dot_line.setLayoutParams(param2);
		return dot_line;
	}

	private LinearLayout setViewPager(Zhang_ViewPager viewPager) {
		LinearLayout line = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		line.addView(viewPager, params);
		return line;
	}

	/** 定时更新任务 */
	private Runnable viewpagerRunnable;
	private int TIME = 3000;
	// 滑动的动画的效果
	private int animationType = 0;
	public static final int ANIMATION_DepthPageTransformer = 1;
	public static final int ANIMATION_ZoomOutPageTransformer = 2;

	/**
	 * @param tIME
	 *            设置滚动间隔
	 */
	public void setTIME(int tIME) {
		this.TIME = tIME;
	}

	/**
	 * @param animationType
	 *            滑动的动画的效果，通过常量来选择 默认无效果 即平滑滑动 使用
	 *            {@link #ANIMATION_DepthPageTransformer} 或者
	 *            {@link #ANIMATION_ZoomOutPageTransformer}
	 */
	public void setAnimationType(int animationType) {
		this.animationType = animationType;
	}

	/**
	 * 定时切换
	 */
	protected void initRunnable(final ViewPager vp, final ArrayList<ImageView> imageViews) {
		switch (animationType) {
		case ANIMATION_DepthPageTransformer:
			vp.setPageTransformer(true, new DepthPageTransformer());
			break;
		case ANIMATION_ZoomOutPageTransformer:
			vp.setPageTransformer(true, new ZoomOutPageTransformer());
			break;
		default:
			break;
		}
		// viewpagerRunnable = new Runnable() {
		// @Override
		// public void run() {
		// int nowIndex = vp.getCurrentItem();
		// vp.setCurrentItem(nowIndex + 1, true);
		// if (isContinue) {
		// vp.postDelayed(viewpagerRunnable, TIME);
		// }
		// }
		// };
		// handler.postDelayed(viewpagerRunnable, TIME);
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (isContinue) {
						Message message = Message.obtain();
						message.arg1 = vp.getCurrentItem()+1;
						mHandler.sendMessage(message);
						try {
							Thread.sleep(TIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			viewPager.setCurrentItem(msg.arg1);
		};
	};
	/**
	 * @param handler
	 *            new一个handler就可以,开启自动滑动
	 */
	public void startRoll(boolean startRoll) {
		this.startRoll = startRoll;
	}

	public void addImageViews(ArrayList<ImageView> lists) {
		imageViews.addAll(lists);
	}

	public void removeAllImageViews() {
		imageViews.clear();
	}

	public void addImageInHead(ImageView iv) {
		imageViews.add(0, iv);
	}

	public void addImageInBottom(ImageView iv) {
		imageViews.add(imageViews.size(), iv);
	}

}

class DepthPageTransformer implements PageTransformer {
	private static float MIN_SCALE = 0.75f;

	@SuppressLint("NewApi")
	@Override
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		if (position < -1) { // [-Infinity,-1)
								// This page is way off-screen to the left.
			view.setAlpha(0);
		} else if (position <= 0) { // [-1,0]
									// Use the default slide transition when
									// moving to the left page
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

/**
 * ViewPager 滚动速度设置
 * 
 */
class ViewPagerScroller extends Scroller {
	private int mScrollDuration = 1000; // 滑动速度

	/**
	 * 设置速度速度
	 * 
	 * @param duration
	 */
	public void setScrollDuration(int duration) {
		this.mScrollDuration = duration;
	}

	public ViewPagerScroller(Context context) {
		super(context);
	}

	public ViewPagerScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
		super(context, interpolator, flywheel);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, mScrollDuration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy, mScrollDuration);
	}

	public void initViewPagerScroll(ViewPager viewPager) {
		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			mScroller.set(viewPager, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/**
 * @author wudkj滑动的动画
 * 
 */
class ZoomOutPageTransformer implements PageTransformer {
	private static final float MIN_SCALE = 0.85f;

	private static final float MIN_ALPHA = 0.5f;

	@Override
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();

		if (position < -1) { // [-Infinity,-1)
								// This page is way off-screen to the left.
			view.setAlpha(0);
		} else if (position <= 1) { // [-1,1]
									// Modify the default slide transition to
									// shrink the page as well
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			if (position < 0) {
				view.setTranslationX(horzMargin - vertMargin / 2);
			} else {
				view.setTranslationX(-horzMargin + vertMargin / 2);
			}
			// Scale the page down (between MIN_SCALE and 1)
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);
			// Fade the page relative to its size.
			view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
		} else { // (1,+Infinity]
					// This page is way off-screen to the right.
			view.setAlpha(0);
		}
	}
}

class MyVPAdapter extends PagerAdapter {
	private List<ImageView> imageViews = new ArrayList<ImageView>();

	public MyVPAdapter(List<ImageView> imageViews) {
		this.imageViews = imageViews;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
		// return imageViews.size();
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public void startUpdate(View arg0) {
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// if (position == imageViews.size()) {
		// return;
		// }
		((ViewPager) container).removeView(imageViews.get(position % imageViews.size()));
	}

	@Override
	public Object instantiateItem(final View container, final int position) {
		((ViewPager) container).addView(imageViews.get(position % imageViews.size()), 0);
		ImageView iv = imageViews.get(position % imageViews.size());
		// ((ViewPager) container).addView(imageViews.get(position));
		// ImageView iv = imageViews.get(position);
		return iv;
	}
}

/**
 * @author wudkj viewPager的创建类
 */
class Zhang_ViewPager extends ViewPager {

	int mLastMotionY;
	int mLastMotionX;

	public Zhang_ViewPager(Context context) {
		super(context);
	}

	public Zhang_ViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}

	public interface onPagerClick {
		/**
		 * 想用就用，不想用也没事
		 * 
		 * @param v
		 *            你点击的图片的view
		 * @param positon
		 *            点击的图片是第几个
		 */
		public void pagerDoSomething(View imageView, int positon);
	}

	public interface onPagerScrolled {
		/**
		 * @param arg0
		 *            arg0:当前页面，及你点击滑动的页面
		 * @param arg1
		 *            arg1:当前页面偏移的百分比
		 * @param arg2
		 *            arg2:当前页面偏移的像素位置
		 */
		public void pagerScrolled(int arg0, float arg1, int arg2);
	}

	public interface onPageScrollStateChanged {
		/**
		 * @param arg0
		 *            当前滑动的状态
		 */
		public void pagerScrollStateChanged(int arg0);
	}
}
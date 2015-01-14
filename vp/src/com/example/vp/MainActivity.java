package com.example.vp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vp.Zhang_ViewPager.onPagerClick;
import com.example.vp.Zhang_ViewPager.onPagerScrolled;


public class MainActivity extends Activity {
	private LinearLayout l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l=(LinearLayout) findViewById(R.id.sdss);
        ArrayList<ImageView> lists=getImages();
        ArrayList<ImageView> lists2=getImages();
		final Zhang_ViewPagerAndPoint vp=new Zhang_ViewPagerAndPoint(MainActivity.this,lists);
		vp.startRoll(new Handler());
		vp.removeAllImageViews();
		vp.addImageViews(lists2);
		ImageView iv=new ImageView(MainActivity.this);
		iv.setBackgroundResource(R.drawable.item05);
		final ImageView iv2=new ImageView(MainActivity.this);
		iv2.setBackgroundResource(R.drawable.item06);
		vp.addImageInHead(iv);
		vp.addImageInBottom(iv2);
		vp.setOnPagerClick(new onPagerClick() {
			@Override
			public void pagerDoSomething(View imageView, int positon) {
				Toast.makeText(MainActivity.this, ""+positon, Toast.LENGTH_SHORT).show();
			}
		});
		vp.setOnPagerScrolled(new onPagerScrolled() {
			
			@Override
			public void pagerScrolled(int arg0, float arg1, int arg2) {
//				Toast.makeText(MainActivity.this, ""+arg0, Toast.LENGTH_SHORT).show();
			}
		});
		vp.setmGravityType(Zhang_ViewPagerAndPoint.GRAVITY_LINE_LEFT);
		vp.setAnimationType(Zhang_ViewPagerAndPoint.ANIMATION_DepthPageTransformer);
		//所有的操作都应该在setViewPagerAndPoint之前完成
		FrameLayout fl=vp.setViewPagerAndPoint();
		l.addView(fl);
    }
	private ArrayList<ImageView> getImages() {
		ArrayList<ImageView> ivs=new ArrayList<ImageView>();
			ImageView iv1=new ImageView(MainActivity.this);
			iv1.setBackgroundResource(R.drawable.item01);
			ivs.add(iv1);
			ImageView iv2=new ImageView(MainActivity.this);
			iv2.setBackgroundResource(R.drawable.item02);
			ivs.add(iv2);
			ImageView iv3=new ImageView(MainActivity.this);
			iv3.setBackgroundResource(R.drawable.item03);
			ivs.add(iv3);
			ImageView iv4=new ImageView(MainActivity.this);
			iv4.setBackgroundResource(R.drawable.item04);
			ivs.add(iv4);
		return ivs;
	}


    
}

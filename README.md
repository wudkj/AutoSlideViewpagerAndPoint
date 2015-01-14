# AutoSlideViewpagerAndPoint
可以无缝自动滑动的viewpager，适合做首页上方的广告条

使用了ViewPager来实现，脱离xml文件

AutoSlideViewPagerAndPoint使用示例

Zhang_ViewPagerAndPoint vp=new Zhang_ViewPagerAndPoint(MainActivity.this,lists);//实例化出一个对象
    通过其中的方法来实现对于下方的点的控制以及自动滑动的开启（包括滑动的动画，内置两种动画，以及一种默认）
    vp.startRoll(new Handler());//自动滑动的开启
    vp.setOnPage...Listener();//用来对viewpager的滑动事件进行控制
    vp.setmGravityType();//设置下方的点的对齐方式     使用常量
    vp.setAnimationType();//设置动画种类     使用常量
FrameLayout fl=vp.setViewPagerAndPoint();//通过调用最终方法获得一个生成包含viewpager和点的FrameLayout，将
这个layout加入到xml中的某个layout中就可以
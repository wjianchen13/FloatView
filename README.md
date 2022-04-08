# 悬浮窗相关
# Activity退至后台



# 悬浮窗显示
1.直接添加View到WindowManager，这种方式缩放View的时候严重卡顿
2.直接添加一个全屏的View到WindowManager，在全屏View里面添加控件，通过对控件的拖动和缩放达到目的


# SharedElement
1. 首先在 App 的主题中设置允许启用窗口内容转换效果
```Java
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>
    <!--允许启用窗口内容转换效果-->
    <item name="android:windowContentTransitions">true</item>
</style>
```

2.在xml文件中，将两个界面中相同视图组件的定义都加上 android:transitionName 属性

3.启动Activity 
```Java
Intent i = new Intent(mContext, DetailActivity.class);
i.putExtra("pos",position + "");
android.support.v4.util.Pair<View, String> image = new android.support.v4.util.Pair(imageView, "image");
android.support.v4.util.Pair<View, String> text = new android.support.v4.util.Pair(textView, "text");
android.support.v4.util.Pair<View, String> longtext = new android.support.v4.util.Pair(textViewLong, "longtext");
ActivityOptionsCompat optionsCompat =
        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, image,text,longtext);
startActivity(i,optionsCompat.toBundle());
```

# 实现方案
1.初步实现方案使用singleInstance或者singleTask+taskAffinity，单独使直播间处于独立的Activity 栈。
但是需要解决跳转问题，比如大厅进入直播间再进入其他页面返回直播间，再返回大厅
直播间直接跳转直播间，需要处理直播间的onNewIntent()方法
这种方法问题太多，单单处理跳转逻辑都比较复杂，还有桌面启动，最近任务列表启动
这种方案有问题，直播间内跳转直播间用的是同一个Activity

2.考虑退出直播间重新连接直播间的socket，用于更新视频最新状态

3.逻辑和activity分离

4.问下老师看看有没有其他方案


singleInstance的Activity跳转到标准的Activity再跳转到singleInstance的Activity效果是怎样
黑边问题，没有试过
https://blog.csdn.net/yaya_soft/article/details/38387789?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~aggregatepage~first_rank_ecpm_v1~rank_v31_ecpm-3-38387789.pc_agg_new_rank&utm_term=activity%E5%88%87%E6%8D%A2+android+%E9%BB%91%E8%BE%B9&spm=1000.2123.3001.4430

关于Activity退到后台再点击列表的主播进入直播间，可以考虑把当前的直播间结束了，再重新进入，看看是否可行

# Service 使用例子



# 出现问题
1. C:\Users\Administrator\.gradle\caches\transforms-2\files-2.1\f243f87f287fb4f4052bd069a9b71980\core-1.7.0\res\values\values.xml:105:5-114:25: AAPT: error: resource android:attr/lStar not found.
因为compileSdkVersion和targetSdkVersion 从31改成了29 buildToolsVersion 从30.0.3改成29.0.3，重新编译就会报错
把implementation 'androidx.appcompat:appcompat:1.4.1'改为implementation 'androidx.appcompat:appcompat:1.3.1'就行了

2.当跳转的Activity设置成SingleInstance模式时，发现在一些华为手机上跳转返回的时候，2个Activity之间会出现一条黑边
处理方式：
把跳转目标Activity的theme修改如下：
android:theme="@style/Theme.TitleBar"

styles添加如下代码：
    <style name="Theme.TitleBar" parent="AppTheme">
        <item name="android:windowIsTranslucent">true</item>
    </style>


# 参考资料  
Android音视频通话过程中最小化成悬浮框的实现（类似Android8.0画中画效果）  
https://www.cnblogs.com/cxk1995/p/7824375.html  

android view 实现双指平移、缩放、旋转
https://blog.csdn.net/zxq614/article/details/88873729

Android支持手势缩放布局
https://www.jianshu.com/p/a27cc06c0dc8

Android应用内悬浮窗的实现方案
https://www.jianshu.com/p/c0d4c23089cd

Android之手势的识别与处理(双击onDoubleTap、滑动onFling、拖动onScroll)
https://blog.csdn.net/nizhuanxingheit/article/details/48716769

Android ViewDragHelper完全解析 自定义ViewGroup神器
https://blog.csdn.net/lmj623565791/article/details/46858663

Android中悬浮小窗播放视频的实现方案(未验证)
https://www.cnblogs.com/chitanta/p/9714436.html

Android 悬浮窗的小结
https://www.liaohuqiu.net/cn/posts/android-windows-manager/
https://github.com/liaohuqiu/android-UCToast

最全面的 Android 悬浮窗功能实现 [转]
https://zhuanlan.zhihu.com/p/265982292

SharedElement  
https://blog.csdn.net/huachao1001/article/details/51659963  

使用Arouter
https://blog.csdn.net/sqf251877543/article/details/85221587

Android中Activity切换时共享视图元素的切换动画（4.x兼容方案）
https://blog.csdn.net/cnzx219/article/details/47157603

Android启动模式之singleinstance的坑
https://blog.csdn.net/Mr_JingFu/article/details/79077613

github搜索Float Video         Float

Activity启动模式、Intent Flags、taskAffinity、task和back stack总结
https://blog.csdn.net/cui130/article/details/80687993?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-4.pc_relevant_default&spm=1001.2101.3001.4242.3&utm_relevant_index=7

类似微信语音聊天界面，SingleInstance、SingleTask实际遇到的问题
https://www.jianshu.com/p/06abdf71dec3

Android基于腾讯云实时音视频实现类似微信视频通话最小化悬浮
https://blog.csdn.net/oYuDaBaJiao/article/details/99998985

Android仿优酷视频的悬浮窗播放
https://blog.csdn.net/why931022/article/details/107229849

EasyFloat：Android悬浮窗框架
https://github.com/princekin-f/EasyFloat

悬浮窗显示在屏幕外 FLAG_LAYOUT_NO_LIMITS
https://blog.csdn.net/u013034413/article/details/83506884

Android 把APP从后台调到前台
https://blog.csdn.net/lxd_love_lgc/article/details/112579667

Android 悬浮窗权限各机型各系统适配大全（总结）
https://blog.csdn.net/cw2004100021124/article/details/78109816?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~aggregatepage~first_rank_ecpm_v1~rank_v31_ecpm-12-78109816.pc_agg_new_rank&utm_term=android+%E6%82%AC%E6%B5%AE%E7%AA%97%E6%94%BE%E5%A4%A7%E7%BC%A9%E5%B0%8F&spm=1000.2123.3001.4430


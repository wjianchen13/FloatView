# 悬浮窗相关
# Activity退至后台



# 悬浮窗显示


singleInstance的Activity跳转到标准的Activity再跳转到singleInstance的Activity效果是怎样

# 出现问题
1. C:\Users\Administrator\.gradle\caches\transforms-2\files-2.1\f243f87f287fb4f4052bd069a9b71980\core-1.7.0\res\values\values.xml:105:5-114:25: AAPT: error: resource android:attr/lStar not found.
因为compileSdkVersion和targetSdkVersion 从31改成了29 buildToolsVersion 从30.0.3改成29.0.3，重新编译就会报错
把implementation 'androidx.appcompat:appcompat:1.4.1'改为implementation 'androidx.appcompat:appcompat:1.3.1'就行了


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

github搜索Float Video






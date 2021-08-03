package com.wendjia.base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 应用工具类
 */
public class AppUtil {
    private static final Handler mUiHandler = new Handler(Looper.getMainLooper());

    private final static int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private final static int CORE_POOL_SIZE = CPU_COUNT * 2;
    private final static int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE + 1;
    private final static long KEEP_ALIVE = 10L;


    public static void OpenBrowser(Context context,String url){
        if (url.startsWith("intent://")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                intent.setSelector(null);
                List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
                if(resolveInfos.size() > 0){
                    ((Activity)context).startActivityIfNeeded(intent, -1);
                }else{

                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        }

    }

    private final static ThreadFactory mBackgroundThreadFactory = new ThreadFactory() {
        //AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减;
        //使用场景: AtomicInteger提供原子操作来进行Integer的使用，因此十分适合高并发情况下的使用。
        final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "Background Thread#" + count.getAndIncrement());
        }
    };

    private static final Executor BACKGROUND_THREAD_POOL = new ThreadPoolExecutor(
            CORE_POOL_SIZE,  //核心线程数
            MAXIMUM_POOL_SIZE,  //最大线程数
            KEEP_ALIVE,  //空闲等待时间
            TimeUnit.SECONDS,  //等待时间单位
            new LinkedBlockingQueue<Runnable>(),  //阻塞队列
            mBackgroundThreadFactory);

    /**
     * 提供整个ApplicationContext引用
     *
     * @return Context
     */
//    public static Application getApplicationContext() {
//        return IBaseApplication.getApp();
//    }

    /**
     * 该方法废弃，请使用runOnBgThread()方法
     * 对外提供后台处理Executor
     *
     * @return 后台Executor
     */
    @Deprecated
    public static Executor getBackgroundExecutor() {
        return BACKGROUND_THREAD_POOL;
    }

//    @Nullable
//    public static Activity getCurTopActivity() {
//        return BaseApplication.getCurTopActivity();
//    }

    public static Handler getUiHandler() {
        return mUiHandler;
    }

    /**
     * 在后台线程中运行
     *
     * @param action
     */
    public static void runOnBgThread(Runnable action) {
        BACKGROUND_THREAD_POOL.execute(action);
    }

    /**
     * 在主线程中运行
     *
     * @param action Runnable
     */
    public static void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            mUiHandler.post(action);
        } else {
            action.run();
        }
    }

    /**
     * 抛入主线程进行延迟操作任务
     *
     * @param action
     * @param time
     */
    public static void postDelayed(Runnable action, long time) {
        mUiHandler.postDelayed(action, time);
    }

//    public static View getContentView() {
//        return getCurTopActivity().getWindow().getDecorView().findViewById(android.R.id.content);
//    }


    public static String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    public static String getTopActivity(Context context) {
        try{
            android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

            if (runningTaskInfos != null) {
                return runningTaskInfos.get(0).topActivity.getClassName();
            }
        }catch (Exception e){}
       return "";
    }

}

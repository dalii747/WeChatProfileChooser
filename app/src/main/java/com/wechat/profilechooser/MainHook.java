package com.wechat.profilechooser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import java.lang.reflect.Method;

public class MainHook {
    public static void handleLoadPackage(Object lpparam) throws Throwable {
        Class<?> lppClass = lpparam.getClass();
        String pkgName = (String) lppClass.getField("packageName").get(lpparam);

        if (!"com.tencent.mm".equals(pkgName)) return;

        ClassLoader cl = (ClassLoader) lppClass.getField("classLoader").get(lpparam);
        Class<?> wxEntry = cl.loadClass("com.tencent.mm.plugin.base.stub.WXEntryActivity");

        Class<?> xposedHelpers = Class.forName("de.robv.android.xposed.XposedHelpers");
        Class<?> methodHook = Class.forName("de.robv.android.xposed.XC_MethodHook");

        Object hook = java.lang.reflect.Proxy.newProxyInstance(
            methodHook.getClassLoader(),
            new Class[]{methodHook},
            (proxy, method, args) -> {
                if ("beforeHookedMethod".equals(method.getName())) {
                    Object param = args[0];
                    Activity activity = (Activity) param.getClass().getField("thisObject").get(param);
                    Intent intent = activity.getIntent();

                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                            .setTitle("选择微信")
                            .setItems(new String[]{"主空间微信", "Island 微信"}, (dialog, which) -> {
                                try {
                                    if (which == 0) {
                                        Method onCreate = wxEntry.getMethod("onCreate", Bundle.class);
                                        onCreate.invoke(activity, (Bundle) null);
                                    } else {
                                        String uri = intent.toUri(Intent.URI_INTENT_SCHEME);
                                        Runtime.getRuntime().exec(new String[]{"su", "-c", "am start --user 12 '" + uri + "'"});
                                        activity.finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            })
                            .setCancelable(false)
                            .show();
                    });

                    param.getClass().getMethod("setResult", Object.class).invoke(param, null);
                }
                return null;
            }
        );

        Method findAndHook = xposedHelpers.getMethod("findAndHookMethod", String.class, ClassLoader.class,
            String.class, Object[].class);
        findAndHook.invoke(null, "com.tencent.mm.plugin.base.stub.WXEntryActivity", cl,
            "onCreate", new Object[]{Bundle.class, hook});
    }
}

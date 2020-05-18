package com.grass.changeappicon;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class ChangeIconManager {
    private static final String TAG = "ChangeIconManager";
    public static final int CHANGEE_ICON_CMD_FAKE_BAGDER = 1;
    public static final int CHANGEE_ICON_CMD_GAME = 2;
    public static final int CHANGEE_ICON_CMD_WALFARE = 3;
    public static final int CHANGEE_ICON_CMD_DEFAULT = 0;
    public static final int CHANGEE_ICON_CMD_INVALID = -1;

    private volatile static ChangeIconManager sInstance;
    private Context mContext;

    public static ChangeIconManager getInstance() {
        if (sInstance == null) {
            synchronized (ChangeIconManager.class) {
                if (sInstance == null) {
                    sInstance = new ChangeIconManager();
                }
            }
        }
        return sInstance;
    }

    public void bindContext(Context context) {
        this.mContext = context;
    }
    public boolean showFakeBagder(Context context, int num) {
        if (getSdkVersion() >= 29)
            return false;

        ComponentName defaultName = new ComponentName(context, "com.grass.changeappicon.MainActivity");
        ComponentName dotName = new ComponentName(context, "com.tencent.mtt.BadgerActivityAliasNum1");
        ComponentName game = new ComponentName(context, "com.grass.changeappicon.GameActivity");
        ComponentName welfare = new ComponentName(context, "com.grass.changeappicon.WelfareActivity");
        PackageManager pm = context.getPackageManager();

        try {
            if (num == CHANGEE_ICON_CMD_DEFAULT) {
                if (enableComponent(pm, defaultName)) {
                    disableComponent(pm, dotName);
                    disableComponent(pm, game);
                    disableComponent(pm, welfare);
                } else
                    return false;
            } else if (num == CHANGEE_ICON_CMD_FAKE_BAGDER) {
                if (enableComponent(pm, dotName)) {
                    disableComponent(pm, game);
                    disableComponent(pm, defaultName);
                    disableComponent(pm, welfare);
                } else
                    return false;
            } else if (num == CHANGEE_ICON_CMD_GAME) {
                if (enableComponent(pm, game)) {
                    disableComponent(pm, dotName);
                    disableComponent(pm, defaultName);
                    disableComponent(pm, welfare);
                } else
                    return false;
            } else if (num == CHANGEE_ICON_CMD_WALFARE) {
                if (enableComponent(pm, welfare)) {
                    disableComponent(pm, dotName);
                    disableComponent(pm, defaultName);
                    disableComponent(pm, game);
                } else
                    return false;
            } else {
                if (enableComponent(pm, defaultName)) {
                    disableComponent(pm, dotName);
                    disableComponent(pm, game);
                    disableComponent(pm, welfare);
                } else
                    return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static boolean enableComponent(PackageManager pm, ComponentName componentName) {
        if (componentName == null) {
            return false;
        }

        if (pm.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            return false;
        }

        try {
            pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean disableComponent(PackageManager pm, ComponentName componentName) {
        if (componentName == null) {
            return false;
        }
        if (pm.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            return false;
        }

        try {
            pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeAppIcon(int iconId, String profileID) {
        boolean ret;
        synchronized (this) {
            ret = iconId >= 0 && showFakeBagder(mContext, iconId);
        }
//        if (ret) {
//            TwatchPreferenceHelper.setFakeRedDotState(iconId+"", profileID,false);
//            BeaconReportUtils.statFakeRedDotEvent(profileID, "" + iconId, BeaconReportUtils.FAKE_RED_DOT_EVENT_SHOW);
//        }
        return ret;
    }

    public void cancelChangeAppIcon() {
        boolean ret;
        synchronized (this) {
            ret = ChangeIconManager.getInstance().showFakeBagder(mContext, 0);
        }
//        String profileID = TwatchPreferenceHelper.getFakeRedDotProfileStr();
//        if (ret) {
//            if (!TextUtils.isEmpty(profileID)) {
//                //随下一次心跳上报
//                TwatchPreferenceHelper.setFakeRedDotNeedReportRecover(true);
//                TwatchPreferenceHelper.setFakeRedDotState(CHANGEE_ICON_CMD_DEFAULT+"",profileID,false);
//            }
//        }
        Log.d(TAG, "RedDot FakeRedDot 桌面icon恢复 result = " + ret);
    }

    public static int getSdkVersion() {
        int sdkVersion = -1;
        if (-1 == sdkVersion) {
            sdkVersion = Integer.parseInt(Build.VERSION.SDK);
        }
        return sdkVersion;
    }
}


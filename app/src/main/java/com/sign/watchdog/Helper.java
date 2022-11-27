package com.sign.watchdog;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

public class Helper
{

	public static final String TAG = Helper.class.getSimpleName();

	/** Singleton object of Helper */
	private static volatile Helper helper;

	static 
	{
		helper = new Helper();
	}

	/**
	 * Method to get the instance of helper
	 * 
	 * @return refineManager
	 */
	public static Helper getInstance() 
	{
		if (helper == null) {
			helper = new Helper();
		}
		return helper;
	}

	/**
	 * Hidden constructor.
	 */
	private Helper() 
	{
	}

	public boolean isAppActive(Context context, String packageName) 
	{
		boolean result = false;
		if (context != null && !TextUtils.isEmpty(packageName)) 
		{
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> processInfoLost = activityManager
					.getRunningAppProcesses();
			if (processInfoLost != null && processInfoLost.size() > 0) 
			{
				for (RunningAppProcessInfo info : processInfoLost) 
				{
					if (info.processName.equals(packageName)) 
					{
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	public boolean isAppInstalled(Context context, String packageName) 
	{
		boolean result = false;
		if (context != null && !TextUtils.isEmpty(packageName)) 
		{
			PackageManager packageManager = context.getPackageManager();
			List<ApplicationInfo> packages = packageManager
					.getInstalledApplications(PackageManager.GET_META_DATA);
			for (ApplicationInfo packageInfo : packages) 
			{
				if (packageInfo.packageName.equals(packageName)) 
				{
					result = true;
					break;
				}
			}
		}
		return result;
	}

	public int getAppPid(Context context, String packageName) 
	{
		int pid = 0;
		if (context != null && !TextUtils.isEmpty(packageName)) 
		{
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> processInfoLost = activityManager
					.getRunningAppProcesses();
			if (processInfoLost != null && processInfoLost.size() > 0) 
			{
				for (RunningAppProcessInfo info : processInfoLost) 
				{
					if (info.processName.equals(packageName)) 
					{
						pid = info.pid;
						break;
					}
				}
			}
		}
		return pid;
	}

	public boolean killAppProcess(Context context, int pid, String packageName) 
	{
		android.os.Process.killProcess(pid);
		return true;
	}
	

	public void invokeInstallation(Context context, String appPackageName) 
	{
		if (context != null && !TextUtils.isEmpty(appPackageName)) {
			Uri marketUri = Uri.parse("market://details?id=" + appPackageName);
			Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
			marketIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				context.startActivity(marketIntent);
			} catch (ActivityNotFoundException err) 
			{
				//Toast.makeText(context, context.getResources().getString(R.string.app_not_found), Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void launchApp(Context context, String appPackageName) 
	{
		if (context != null && !TextUtils.isEmpty(appPackageName)) 
		{
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
			if (intent != null) 
			{
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				try 
				{
					context.startActivity(intent);
				} 
				catch (ActivityNotFoundException err) 
				{
					//Toast.makeText(context, context.getResources().getString(R.string.package_not_found), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public void launchHome(Context context) 
	{
		if (context != null) 
		{
			Intent homeIntent = new Intent(Intent.ACTION_MAIN);
			homeIntent.addCategory(Intent.CATEGORY_HOME);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(homeIntent);
		}
	}
/*
	public void restartApp(Context context, String appPackageName) 
	{
		if (context != null && !TextUtils.isEmpty(appPackageName)) 
		{
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
			if (intent != null) 
			{
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				try 
				{
					context.startActivity(intent);
				} 
				catch (ActivityNotFoundException err) 
				{
					AppUtils.showToast(context, context.getResources().getString(R.string.restart_package_failed));
				}
			}
		}
	}
	*/
}

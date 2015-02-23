package co.camilosegura.restaurantmenu.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

public class Utilities {
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isInternetConnectionAvaible(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isTablet(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
			return true;
		} else {
			return false;
		}
	}
}

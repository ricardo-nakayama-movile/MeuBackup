package com.betatest.canalkidsbeta.bo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.betatest.canalkidsbeta.billing.util.IabHelper;
import com.betatest.canalkidsbeta.billing.util.IabResult;
import com.betatest.canalkidsbeta.billing.util.Inventory;
import com.betatest.canalkidsbeta.billing.util.Purchase;
import com.betatest.canalkidsbeta.dao.SharedPreferencesDAO;

public class SubscriptionBO {

	// TODO CHANGE THIS (arbitrary) request code for the purchase flow SO WE CAN IDENTIFY THE USER
	static final int RC_REQUEST = 10001;

	private static final String TAG = SubscriptionBO.class.getSimpleName();
	boolean mSubscribedToChannels = false;

	//TODO  SKU for our subscription
	static final String SKU_SUBSCHANNEL = "test_purchase_canal_kids_beta";

	private Activity activity;
	private Context context;

	public SubscriptionBO(Activity activity, Context context) {
		this.activity = activity;
		this.context = context.getApplicationContext();

	}

	public void initSubscriptionBO(final IabHelper mHelper) {

		// TODO SET TO FALSE WHEN IN RELEASE
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					Log.w(TAG, "Problem setting up in-app billing: " + result);
					return;
				}

				Log.d(TAG, "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}

	// Listener that's called when we finish querying the items and
	// subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			
			Log.d(TAG, "Query inventory finished.");
			if (result.isFailure()) {
				Log.w(TAG, "Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// Check if we have the subscription
			Purchase channelPurchase = inventory.getPurchase(SKU_SUBSCHANNEL);
			mSubscribedToChannels = (channelPurchase != null && verifyDeveloperPayload(channelPurchase));
			
			Log.d(TAG, "User "
					+ (mSubscribedToChannels ? "HAS" : "DOES NOT HAVE")
					+ " subscription.");
			
			SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();
			
			//TODO WHAT SHOULD WE WRITE WHEN WE HAVE SUBSCRIPTION?
			if (mSubscribedToChannels) {
				Log.d(TAG, "User has subscription, writing in shared pref.");
				sharedPreferencesDAO.write(context, "subscription", "userNo");
			} else {
				Log.d(TAG, "User doesn't have subscription, writing in shared pref.");
				sharedPreferencesDAO.write(context, "subscription", "none");
			}
			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 * 
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 * 
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */

		return true;
	}

	public void channelSubscription(View arg0, IabHelper mHelper) {
		if (!mHelper.subscriptionsSupported()) {
			Log.w(TAG,
					"Subscriptions not supported on your device yet. Sorry!");
			return;
		}

		/*
		 * TODO: for security, generate your payload here for verification. See
		 * the comments on verifyDeveloperPayload() for more info. Since this is
		 * a SAMPLE, we just use an empty string, but on a production app you
		 * should carefully generate this.
		 */
		String payload = "";
		Log.d(TAG, "Launching purchase flow for channel subscription.");
		mHelper.launchPurchaseFlow(activity, SKU_SUBSCHANNEL,
				IabHelper.ITEM_TYPE_SUBS, RC_REQUEST,
				mPurchaseFinishedListener, payload);
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);
			if (result.isFailure()) {
				Log.w(TAG, "Error purchasing: " + result);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				Log.w(TAG,
						"Error purchasing. Authenticity verification failed.");
				return;
			}

			Log.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(SKU_SUBSCHANNEL)) {
				// bought subscription
				Log.d(TAG, "Channel subscription purchased, writing to shared pref.");
				
				SharedPreferencesDAO sharedPreferencesDAO = new SharedPreferencesDAO();
				sharedPreferencesDAO.write(context, "subscription", "userNo");
	
				mSubscribedToChannels = true;
			}
		}
	};

}

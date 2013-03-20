package com.betatest.canalkidsbeta.controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.betatest.canalkidsbeta.billing.util.IabHelper;
import com.betatest.canalkidsbeta.bo.SubscriptionBO;


public class SubscriptionController {

	private static final String TAG = SubscriptionController.class.getSimpleName();
	
	/*
	 * base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY (that
	 * you got from the Google Play developer console). This is not your
	 * developer public key, it's the *app-specific* public key.
	 * 
	 * Instead of just storing the entire literal string here embedded in
	 * the program, construct the key at runtime from pieces or use bit
	 * manipulation (for example, XOR with some other string) to hide the
	 * actual key. The key itself is not secret information, but we don't
	 * want to make it easy for an attacker to replace the public key with
	 * one of their own and then fake messages from the server.
	 */
	public String getBase64EncodedPublicKey(){
		//TODO FOLLOW THE COMMENTS TO CREATE A PUB KEY
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoi+pXV8KwvdfzDiHuOGRtvpc2RTyvR3dAR1u/2F1cyY7B6hFvWMWtpYAcNUtK9Ehz9cooXNnykOkhiSYzq9KEwq+I1zI1Nac1fnOPkPpWg1rUYXtwZSiKhQxoaIlMwv8BRYUCXLPSu5TjT2h3UyXSecKrdRdalfcwl6HWQh2oI3FRpd60aBfoRigIbe4KVGQ0id8O+WSWqMtBTXjN3/fR+Q1/KJuUqssPjrGgsuuLVYKSnvA5xTV1gwLyZcplHtZzvTy9nfTXg8SFFxIGCf2LO0q6kRdCH9WrUUdxuapuFWKnSdQy2IU4zsVUS8882tryEkC/R/tghRPqIWKhuzgjwIDAQAB";
		
		return base64EncodedPublicKey;
	}
	
	public Void initSubscription(Context context, Activity activity, IabHelper mHelper){
		Log.d(TAG,"Initializing subscription");
		SubscriptionBO subscriptionBO = new SubscriptionBO(activity, context);
		subscriptionBO.initSubscriptionBO(mHelper);
		return null;
	}
	
	public Void buySubscription(Context context, Activity activity, View view, IabHelper mHelper){
		Log.d(TAG,"Buying subscription");
		SubscriptionBO subscriptionBO = new SubscriptionBO(activity, context);
		subscriptionBO.channelSubscription(view, mHelper);
		return null;
	}

}
package org.ekokonnect.reprohealth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.ekokonnect.reprohealth.models.http.UserAuthResponse;
import org.ekokonnect.reprohealth.services.EkokonnectClient;
import org.ekokonnect.reprohealth.services.ServiceClient;
import org.ekokonnect.reprohealth.utils.CommonUtilities;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends AppCompatActivity {
	
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;

	private static final String TAG = "LogInActivity";

	private ProgressDialog mConnectionProgressDialog;;

	// Form values at the time of login attempt
	private String mEmail;
	private String mPassword;
	private String mFirstName;
	private String mLastName;
	private String mDOB;
	private String mSex;
	private String mUID;

	// UI references.

	private LoginButton mFacebookLogin;

	private View mLoginFormView;
	private View mLoginStatusView;
//	private TextView guideText;

    CallbackManager callbackManager;
    AuthCallback authCallback;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(getApplicationContext());

		setContentView(R.layout.activity_login);
		
		mLoginStatusView = (View) findViewById(R.id.login_status);
		mLoginFormView = (View) findViewById(R.id.login_form);

        callbackManager = CallbackManager.Factory.create();
        authCallback = new AuthCallback();

		mFacebookLogin = (LoginButton) findViewById(R.id.facebook_sigin);
		mFacebookLogin.setReadPermissions(Arrays.asList("email"));
        mFacebookLogin.registerCallback(callbackManager, authCallback);


		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in");

		  
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Facebook Auth Callback
     */
    private class AuthCallback implements FacebookCallback<LoginResult> {

        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "Success");
            attemptLogin(loginResult.getAccessToken());

        }

        @Override
        public void onCancel() {
            Log.d(TAG, "cancel");
//            Snackbar.make(mLoginFormView, "Login Cancelled", Snackbar.LENGTH_LONG);
        }

        @Override
        public void onError(FacebookException e) {
            Log.d(TAG, "error");
//            Snackbar.make(mLoginFormView, "Login Error", Snackbar.LENGTH_LONG);
        }
    }

	private void attemptLogin(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.d(TAG, response.toString());

                        try {
                            mEmail = object.getString("email");
                            mFirstName = object.getString("first_name");
                            mLastName = object.getString("last_name");
                            mSex = object.getString("gender");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new NullPointerException();
                        }



                        pushUserToServer();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
	}


	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	
	

	public void toSharedPreferences() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("Email", mEmail);
		editor.putString("Firstname", mFirstName);
		editor.putString("Lastname", mLastName);
		editor.putString("DOB", mDOB);
		editor.putString("password", mPassword);
		editor.putString("Sex", mSex);
		editor.putString("UID", mUID);
		editor.putBoolean("isSignedIn", true);
		editor.apply();
	}

	private void startMainActivity() {
		finish();
		Intent intent = new Intent(getApplicationContext(),
				TipListActivity.class);
		startActivity(intent);
	}


	protected void pushUserToServer() {
		
		if (checkPlayServices()) {
	    	Log.d(TAG, "Play Services Available");
	        // If this check succeeds, proceed with normal processing.
	        // Otherwise, prompt user to get valid Play Services APK.
	    	gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(getApplicationContext());

            if (regid.length() == 0) {
            	Log.d(TAG, "RegID is empty");
                registerInBackground();
            } else
            {
            	Log.d(TAG, "RegID isnt empty");
            	sendRegistrationIdToBackend(regid);
            }
            
            
	    }
		
		
	}


	// GCM Codes
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.length()==0) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
		return PreferenceManager
				.getDefaultSharedPreferences(this);
//	    return getSharedPreferences("ASUU",
//	            Context.MODE_PRIVATE);
	}
	
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            //finish();
	            Toast.makeText(getApplicationContext(), 
	            		"Error Registering with GCM", Toast.LENGTH_LONG).show();
	        }
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
	    new RegisterAsyncTask().execute(null, null, null);
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	
	private class RegisterAsyncTask extends AsyncTask<Void, Void, Void>{		
	        protected Void doInBackground(Void... params) {
	        	Log.d(TAG, "Do in background");
	            String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
	                }
	                regid = gcm.register(CommonUtilities.SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;

	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.
	                // The request to your server should be authenticated if your app
	                // is using accounts.
	                
	                sendRegistrationIdToBackend(regid);

	                // For this demo: we don't need to send it because the device
	                // will send upstream messages to a server that echo back the
	                // message using the 'from' address in the message.

	                // Persist the regID - no need to register again.
	                //storeRegistrationId(context, regid); // Persist Later
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.
	                Log.e(TAG, msg);
	            }
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Void msg) {
	            //mDisplay.append(msg + "\n");
	        }	    
	    
	    
	}
	
	/**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
	
    private void sendRegistrationIdToBackend(final String regid) {
      // Your implementation here.
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("action", "register");
    	params.put("email", mEmail);
    	params.put("firstName", mFirstName);
    	params.put("lastName", mLastName);
    	params.put("gender", mSex);
    	params.put("gcmid", regid);
		
//    	showProgress(true);
    	Log.d(TAG, "Registering User to Backend " + regid);

		EkokonnectClient client = ServiceClient.getInstance()
				.getClient(getApplicationContext(), EkokonnectClient.class);

		client.authenticateUser("register", mEmail, mFirstName, mLastName, mSex, regid,
				new Callback<UserAuthResponse>() {
					@Override
					public void success(UserAuthResponse userAuthResponse, Response response) {
						long uid = userAuthResponse.message.uid;
						Log.d(TAG, "UID: " + uid);
						mUID = String.valueOf(uid);

						storeRegistrationId(getApplicationContext(), regid);
						toSharedPreferences();
						showProgress(false);
						startMainActivity();
					}

					@Override
					public void failure(RetrofitError error) {
						Log.e(TAG, "Volley Error");
						showProgress(false);
						Toast.makeText(getApplicationContext(), "Unexpected Error", Toast.LENGTH_LONG).show();
						error.printStackTrace();
					}
				});

    	
    }
    

}

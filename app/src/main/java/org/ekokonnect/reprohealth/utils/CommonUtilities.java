package org.ekokonnect.reprohealth.utils;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
//    public static final String SERVER_URL = "http://api.gamefriq.com/asuu/gcm_server_php/register.php";
    public static final String SERVER_URL = "http://ekokonnect-reprohealth.appspot.com";
    public static final String SERVER_URL_USER = "http://ekokonnect-reprohealth.appspot.com/user";
    public static final String SERVER_URL_TIPS = "http://ekokonnect-reprohealth.appspot.com/tips";
    public static final String SERVER_URL_MESSAGE = "http://ekokonnect-reprohealth.appspot.com/u/message";

    // Google project id
    public static final String SENDER_ID = "803224873522"; 

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM";

    static final String DISPLAY_MESSAGE_ACTION =
            "org.ekokonnect.reprohealth.utils.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}

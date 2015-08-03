import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by oyewale on 8/1/15.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}

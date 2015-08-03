package org.ekokonnect.reprohealth;

//import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AboutActivity extends Activity {
	private String lolu = "http://ng.linkedin.com/pub/lolu-bodunwa/5b/55b/39b";
	private String stanley = "http://ng.linkedin.com/pub/stanley-obinna-agba/5b/586/a81";
	private String wale = "http://ng.linkedin.com/in/waleoyediran";
	private String ngo ="http://www.wharc-online.org/";
	private String konnect = "http://eko-konnect.org.ng/";
	private String ford = "http://www.fordfoundation.org/";
	
	public static final String REQUESTED_PAGE_KEY = "requested_page_key";
	  public static final String DEFAULT_PAGE = "about.html";
	  public static final String ABOUT_PAGE = "about.html";
	  public static final String TERMS_PAGE = "terms.html";
	  public static final String WHATS_NEW_PAGE = "whatsnew.html";

	  private static final String BASE_URL = "file:///android_asset/html/";
	  private static final String WEBVIEW_STATE_PRESENT = "webview_state_present";

	  private WebView webView;

	  private final View.OnClickListener doneListener = new View.OnClickListener() {
	    @Override
	    public void onClick(View view) {
	      finish();
	    }
	  };

	  @Override
	  protected void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    
	  //Do this for every activity together with the overrided onStop()	  	
//	  	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    setContentView(R.layout.help);

	    webView = (WebView)findViewById(R.id.help_contents);
	    webView.setWebViewClient(new HelpClient(this));

	    Intent intent = getIntent();
	    String page = intent.getStringExtra(REQUESTED_PAGE_KEY);

	    // Show an OK button.
	    View doneButton = findViewById(R.id.done_button);
	    doneButton.setOnClickListener(doneListener);
	    doneButton.setVisibility(View.VISIBLE);
//	    if (page.equals(DEFAULT_PAGE)) {
//	      doneButton.setVisibility(View.VISIBLE);
//	    } else {
//	      doneButton.setVisibility(View.GONE);
//	    }

	    // Froyo has a bug with calling onCreate() twice in a row, which causes the What's New page
	    // that's auto-loaded on first run to appear blank. As a workaround we only call restoreState()
	    // if a valid URL was loaded at the time the previous activity was torn down.
	    if (icicle != null && icicle.getBoolean(WEBVIEW_STATE_PRESENT, false)) {
	      webView.restoreState(icicle);
	    } else if (intent != null && page != null && page.length() > 0) {
	      webView.loadUrl(BASE_URL + page);
	    } else {
	      webView.loadUrl(BASE_URL + DEFAULT_PAGE);
	    }
	  }

	  @Override
	  protected void onSaveInstanceState(Bundle state) {
	    String url = webView.getUrl();
	    if (url != null && url.length() > 0) {
	      webView.saveState(state);
	      state.putBoolean(WEBVIEW_STATE_PRESENT, true);
	    }
	  }

	  @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	      if (webView.canGoBack()) {
	        webView.goBack();
	        return true;
	      }
	    }
	    return super.onKeyDown(keyCode, event);
	  }

	  private final class HelpClient extends WebViewClient {
	    Activity context;
	    public HelpClient(Activity context){
	        this.context = context;
	    }

	    @Override
	    public void onPageFinished(WebView view, String url) {
	      setTitle(view.getTitle());
	    }

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	      if (url.startsWith("file")) {
	        // Keep local assets in this WebView.
	        return false;
	      } else if (url.startsWith("mailto:")) {
	        try {
	              MailTo mt = MailTo.parse(url);
	              Intent i = new Intent(Intent.ACTION_SEND);
	              i.setType("message/rfc822");
	              i.putExtra(Intent.EXTRA_EMAIL, new String[]{mt.getTo()});
	              i.putExtra(Intent.EXTRA_SUBJECT, mt.getSubject());
	              context.startActivity(i);
	              view.reload();
	        }
	        catch (ActivityNotFoundException e) {
	          Log.w("Help", "Problem with Intent.ACTION_SEND", e);
	                  new AlertDialog.Builder(context)
	                    .setTitle("Contact Info")
	                    .setMessage( "Please send your feedback to: app.ocr@gmail.com" )
	                    .setPositiveButton( "Done", new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int which) {
	                            Log.d("AlertDialog", "Positive");
	                        }
	                    })
	                    .show();
	        }
	            return true;
	      } else {
	        // Open external URLs in Browser.
	        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	        return true;
	      }
	    }
	  }
	  
	//Do this for every activity
	  @Override
		protected void onStop(){
			super.onStop();
		}
	
	/*private TextView loluView ;
	private TextView stanView ;
	private TextView waleView ;
	private TextView ngoView ;
	private TextView fordView ;
	private TextView konnectView;
	private Intent i = new Intent(Intent.ACTION_VIEW);
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		loluView = (TextView) findViewById(R.id.lolu);
		stanView = (TextView)findViewById(R.id.stanley);
		waleView = (TextView)findViewById(R.id.wale);
		ngoView = (TextView)findViewById(R.id.appNGO);
		fordView = (TextView)findViewById(R.id.ford);
		konnectView = (TextView)findViewById(R.id.konnect);
		
		loluView.setOnClickListener(this);
		stanView.setOnClickListener(this);
		waleView.setOnClickListener(this);
		fordView.setOnClickListener(this);
		konnectView.setOnClickListener(this);
		ngoView.setOnClickListener(this);
	}
	
	

	@Override
	public void onClick(View v) {
		
		// TODO Auto-generated method stub
		switch (v.getId()){
		
		case R.id.appNGO:
			i.setData(Uri.parse(ngo));
			startActivity(i);
			
			
		case R.id.lolu:
			i.setData(Uri.parse(lolu));
			startActivity(i);
			
		case R.id.wale:
			i.setData(Uri.parse(wale));
			startActivity(i);
			
		case R.id.stanley:
			i.setData(Uri.parse(stanley));
			startActivity(i);
		
		case R.id.ford:
			i.setData(Uri.parse(ford));
			startActivity(i);
			
		case R.id.konnect:
			i.setData(Uri.parse(konnect));
			startActivity(i);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
	*/
	

}

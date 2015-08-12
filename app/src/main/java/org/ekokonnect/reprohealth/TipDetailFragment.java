package org.ekokonnect.reprohealth;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Place detail screen. This fragment is either
 * contained in a {@link PlaceListActivity} in two-pane mode (on tablets) or a
 * {@link PlaceDetailActivity} on handsets.
 */
public class TipDetailFragment extends Fragment {
	
	private ShareActionProvider mShareActionProvider;
	TextView mTipTitle, mTipDate, mTipContent, mTipAuthor;
	private String title, date, content, author, img;
	private ImageView mTipImage;
    private Picasso picasso;
//	public static final String ARG_ITEM_ID = "item_id";
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TipDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		if (getArguments().containsKey(title)) {
			
			title = getArguments().getString("title");
			date = getArguments().getString("date");
			content = getArguments().getString("content");
			author = getArguments().getString("author");
			img = getArguments().getString("img");
//        Log.d("picasso", img);
//		}
			setHasOptionsMenu(true);
        picasso = Picasso.with(getActivity());
        picasso.setLoggingEnabled(true);
        picasso.setIndicatorsEnabled(true);
			
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_viewtip,
				container, false);
		
		/*Bundle extras = getIntent().getExtras();
		title = (String)extras.getString("title");
		date = (String)extras.getString("date");
		content = (String)extras.getString("content");
		author = (String)extras.getString("author");*/
		
		
		mTipTitle = (TextView)rootView.findViewById(R.id.tipTitle);
		mTipDate = (TextView)rootView.findViewById(R.id.tipDate);
		mTipContent = (TextView)rootView.findViewById(R.id.tipContent);
		mTipAuthor = (TextView)rootView.findViewById(R.id.tipAuthor);
		mTipImage = (ImageView)rootView.findViewById(R.id.tipImage);
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Bold.ttf");
		mTipTitle.setTypeface(font, R.style.BigFont);
		Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
		
		mTipContent.setTypeface(font2);
		
		mTipTitle.setText(title);
		mTipDate.setText(date);
		mTipContent.setText(content);
		mTipAuthor.setText(author);
		
		picasso
        .load(img)
		.placeholder(R.drawable.com_facebook_profile_picture_blank_square)
        .error(R.drawable.com_facebook_profile_picture_blank_square)
        .into(mTipImage);

		return rootView;
	}
	
//	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.viewtipactivity, menu);
		MenuItem item = menu.findItem(R.id.menu_item_share);
		
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
		mShareActionProvider.setShareIntent(getDefaultShareIntent());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){			
			case R.id.settings:
				startActivity(new Intent(getActivity().getApplicationContext(), SettingsActivity.class));
				return true;
	
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	//This method shares the content of the tip(plain text) using a ShareActionProvider.
		private Intent getDefaultShareIntent() {
		    Intent intent = new Intent(Intent.ACTION_SEND);
		    intent.setType("text/plain");
		    intent.putExtra(Intent.EXTRA_SUBJECT, title);
		    intent.putExtra(Intent.EXTRA_TEXT, content+ "\n\n-via RepoHealth App for Android");
		    
		    return intent;
		}
	
	
}

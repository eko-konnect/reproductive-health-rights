package org.ekokonnect.reprohealth.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.ekokonnect.reprohealth.R;
import org.ekokonnect.reprohealth.TipListActivity;
import org.ekokonnect.reprohealth.TipListFragment;
import org.ekokonnect.reprohealth.services.EkokonnectClient;
import org.ekokonnect.reprohealth.services.ServiceClient;
import org.ekokonnect.reprohealth.utils.CommonUtilities;
import org.ekokonnect.reprohealth.utils.ConnectionDetector;

import java.util.List;

import models.Tip;
import models.TipDataSource;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



public class TipListAdapter extends BaseAdapter {
//	ArrayList<Tip> tips;
	TipListActivity activity;
	LayoutInflater inflater;
	private ConnectionDetector cd;
	private TipDataSource tipDataSource;
	TipListFragment frag;
	
	public TipListAdapter(TipListFragment frag){
//		this.tips = tips;
		this.frag = frag;
		this.activity = (TipListActivity)frag.getActivity();
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tipDataSource = new TipDataSource(activity);
		
		loadTipsFromDB();
	}
	
	private void loadTipsFromDB(){
		tipDataSource.open();
		activity.tips = tipDataSource.getAllTips();
		tipDataSource.close();
	}
	
	@Override
	public int getCount() {
		return activity.tips.size();
	}

	@Override
	public Tip getItem(int arg0) {
		return activity.tips.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		View v = convertView;
		
		if (convertView == null)
			 v = inflater.inflate(R.layout.row_layout, null);
			
		 TextView title = (TextView)v.findViewById(R.id.title);
		 TextView summary = (TextView)v.findViewById(R.id.summary);
		 TextView author = (TextView)v.findViewById(R.id.author);
		 TextView date = (TextView)v.findViewById(R.id.date);
		 
		 Tip tip = activity.tips.get(position);
		 String shortdescription = tip.getContent().substring(0, Math.min(250, tip.getContent().length()));
		 title.setText(tip.getTitle());
		 summary.setText(shortdescription + "...");
		 author.setText(tip.getAuthor());
		 date.setText(tip.getDate());
//		}
		return v;
	}
	
	// Retrieving Data
	public void refresh(){
		cd = new ConnectionDetector(activity);
		//queue = Volley.newRequestQueue(context);
		// Check if Internet present
		if (!cd.isConnectingToInternet()) {

			Toast.makeText(activity, "Network Unavailable! Pls try again later",
					Toast.LENGTH_LONG).show();
			return;
			// stop executing code by return
			
		}
		frag.setRefreshActionButtonState(true);

		Log.d("ReproHealth", "about to get tip");

		EkokonnectClient client = ServiceClient.getInstance()
				.getClient(activity.getApplicationContext(), EkokonnectClient.class, CommonUtilities.SERVER_URL);
        client.downloadTips(new Callback<List<Tip>>() {
            @Override
            public void success(List<Tip> tips, Response response) {
                parseJSON(tips);
                frag.setRefreshActionButtonState(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(activity, "Network Error. Troble connecting to the Internet",
                        Toast.LENGTH_LONG).show();
                Log.e("ReproHealth", "Volley Error");
                frag.setRefreshActionButtonState(false);
            }
        });

	}

	protected void parseJSON(List<Tip> tips) {
		TipDataSource db = new TipDataSource(activity);
		db.open();
        for (Tip tip : tips) {
            long result = db.insertIntoTable(tip);
            if (result != -1){
                activity.tips.add(tip);
            }
        }
        notifyDataSetChanged();
		db.close();

	}
	

}

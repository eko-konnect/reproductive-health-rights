package org.ekokonnect.reprohealth.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.Message;
import models.MessageDataSource;

import org.ekokonnect.reprohealth.R;
import org.ekokonnect.reprohealth.utils.CommonUtilities;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;



public class ChatActivityAdapter extends BaseAdapter{
	private static final String TAG = "ChatAdapter";
	ArrayList<Message> messages;
	Activity activity;
	LayoutInflater inflater;
	MessageDataSource dataSource;
	public ChatActivityAdapter(Activity activity){
		this.activity = activity;		
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		dataSource = new MessageDataSource(activity);
		dataSource.open();
		messages = dataSource.getAllMessages();
		dataSource.close();
	}
	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = (Message)this.getItem(position);
		View cView = convertView;
		
		if (convertView == null)
			cView = inflater.inflate(R.layout.chat_row_layout, null);
			
		TextView chatmessage;
		ProgressBar progressBar;
		View layout;
		
		
		chatmessage = (TextView)cView.findViewById(R.id.message_text);
		progressBar = (ProgressBar)cView.findViewById(R.id.line_progress_bar);
		layout = (View)cView.findViewById(R.id.layout);
		
		chatmessage.setText(message.getMessage());
		LayoutParams lp = (LayoutParams)chatmessage.getLayoutParams();
		if(message.isMine()){
			layout.setBackgroundResource(R.drawable.bubble_green);
			lp.gravity = Gravity.RIGHT;
		}
	
		else{
			layout.setBackgroundResource(R.drawable.bubble_yellow);
			lp.gravity = Gravity.LEFT;
		}
		
		if (message.statusOK){
			progressBar.setVisibility(View.GONE);
		}else{
			progressBar.setVisibility(View.VISIBLE);
		}
		chatmessage.setLayoutParams(lp);

		
		return cView;
		
		
	}
	
	public void add(Message line) {
		// TODO Auto-generated method stub
		messages.add(line);
		notifyDataSetChanged();
		startRequest(line);
	}
	
	private void startRequest(final Message line) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
	    String uid = prefs.getString("UID", null);	    
		
		String url = CommonUtilities.SERVER_URL_MESSAGE;
		Log.d(TAG, "URL: "+url);
		
		Map<String, String> params = new HashMap<String, String>();
    	params.put("message", line.getMessage());
    	Log.d(TAG, "message: "+line.getMessage());
    	params.put("uid", uid);
    	Log.d(TAG, "uid: "+uid);
    	
//		CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new
//				Response.Listener<JSONObject>() {
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
//				Log.d(TAG, response.toString());
//
//				try {
//					if (response.getInt("status") == 0){
//						JSONObject message = response.getJSONObject("message");
//						line.statusOK = true;
//						line.setId(message.getLong("mid"));
//						line.setDate(message.getJSONObject("msg").getLong("date"));
//
//						dataSource = new MessageDataSource(activity);
//						dataSource.open();
//						dataSource.insertIntoTable(line);
//						dataSource.close();
//						//pd.dismiss();
////						new Date
//						Log.d(TAG, "MessageID: "+line.getId());
//						notifyDataSetChanged();
//					} else if (response.getInt("status") == 1){
//						//Toast.makeText(activity.getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
//						String msg = response.getString("message");
//						Log.d(TAG, "Line-Error: "+msg);
////						line.error = msg;
////						line.score = -1;
//
//						notifyDataSetChanged();
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					Log.d(TAG, "JSON Error");
////					line.error = "App Error";
////					line.score = -1;
//				}
//
//
//			}
//				}, new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						Log.e(TAG, ((error.getMessage()== null)? "no msg" : error.getMessage()));
//						Log.d(TAG, "Volley Error");
////						line.error = "Network Error";
////						line.score = -1;
//						//pd.dismiss();
//						//Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
//					}
//				});

	}
	
	
	private Message parseJSON(JSONObject json){
		Message response = null;
        try{
            response = new Message(json.getLong("mid"), json.getString("text"),
            		json.getBoolean("isMine"), json.getLong("date"));
        }
        catch(Exception e){
        	Log.e(TAG, "Error parsing JSON");
            e.printStackTrace();
        }
        
        return response;

    }
}

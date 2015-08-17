package org.ekokonnect.reprohealth.adapters;

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

import org.ekokonnect.reprohealth.R;
import org.ekokonnect.reprohealth.models.http.SendMessageResponseModel;
import org.ekokonnect.reprohealth.services.EkokonnectClient;
import org.ekokonnect.reprohealth.services.ServiceClient;
import org.ekokonnect.reprohealth.utils.CommonUtilities;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.Message;
import models.MessageDataSource;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



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
		Log.d(TAG, "URL: " + url);
		
		Map<String, String> params = new HashMap<String, String>();
    	params.put("message", line.getMessage());
    	Log.d(TAG, "message: " + line.getMessage());
    	params.put("uid", uid);
    	Log.d(TAG, "uid: " + uid);

		EkokonnectClient client = ServiceClient.getInstance()
				.getClient(activity.getApplicationContext(), EkokonnectClient.class,
						CommonUtilities.SERVER_URL);
		client.sendChatMessage(uid, line.getMessage(), new Callback<SendMessageResponseModel>() {
			@Override
			public void success(SendMessageResponseModel sendMessageResponseModel, Response response) {
                if (sendMessageResponseModel.status == 0){
                    line.statusOK = true;
                    line.setId(sendMessageResponseModel.message.mid);
                    line.setDate(sendMessageResponseModel.message.msg.date);

                    dataSource = new MessageDataSource(activity);
                    dataSource.open();
                    dataSource.insertIntoTable(line);
                    dataSource.close();

                    Log.d(TAG, "MessageID: "+line.getId());
                    notifyDataSetChanged();
                } else if (sendMessageResponseModel.status == 1){
                    Log.d(TAG, "Error: ");

                    notifyDataSetChanged();
                }
			}

			@Override
			public void failure(RetrofitError error) {
                Log.d(TAG, "Error: ");
			}
		});


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

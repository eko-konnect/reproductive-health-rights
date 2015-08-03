package org.ekokonnect.reprohealth;

import java.util.ArrayList;

import models.Message;

import org.ekokonnect.reprohealth.adapters.ChatActivityAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class ChatActivity extends ListActivity{
	ImageButton sendButton;
	EditText chatText;
	ChatActivityAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		sendButton = (ImageButton)findViewById(R.id.sendButton);
		chatText = (EditText)findViewById(R.id.text);
		
		sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage();
			}
		});
		
//		ArrayList<Message> messages = new ArrayList<Message>();
		//Hard coding chat messages
		/*Message a = new Message("Hello", false);
		Message b = new Message("Hi", true);
		messages.add(a);
		messages.add(b);*/
		
		adapter = new ChatActivityAdapter(this);
		setListAdapter(adapter);
	}
	
	private void sendMessage(){
		if (chatText.getText().length() > 0){
			Message msg = new Message(chatText.getText().toString(), true);
			msg.statusOK = false;
			adapter.add(msg);
			chatText.setText("");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	
}

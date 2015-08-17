package org.ekokonnect.reprohealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.splunk.mint.Mint;

import org.ekokonnect.reprohealth.adapters.ChatActivityAdapter;

import models.Message;

public class ChatActivity extends AppCompatActivity{
	ImageButton sendButton;
	EditText chatText;
	ChatActivityAdapter adapter;
	private Toolbar mToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(ChatActivity.this, "51c64c0b");
		setContentView(R.layout.activity_chat);

        setupToolbar();
		sendButton = (ImageButton)findViewById(R.id.sendButton);
		chatText = (EditText)findViewById(R.id.text);
		
		sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage();
			}
		});
		
		adapter = new ChatActivityAdapter(this);
        ListView listView = (ListView)findViewById(android.R.id.list);
		listView.setAdapter(adapter);
	}

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

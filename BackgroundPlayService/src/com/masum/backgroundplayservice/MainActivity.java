package com.masum.backgroundplayservice;

/** Author Md. Nazmul Hasan Masum
 * Background media player using service */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements android.view.View.OnClickListener{
	public static Button btnPlay, btnStop;
	private Intent playerService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        
        playerService = new Intent(MainActivity.this, PlayerInService.class);
		startService(playerService);
    }

	private void initView() {
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnStop = (Button) findViewById(R.id.btnStop);
		btnPlay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPlay:
			
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		if (!PlayerInService.mp.isPlaying()) {
			PlayerInService.mp.stop();
			stopService(playerService);
		}
		super.onDestroy();
	}
    
    

}

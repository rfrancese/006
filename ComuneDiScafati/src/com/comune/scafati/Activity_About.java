package com.comune.scafati;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Activity_About extends Activity {
	
	Bundle savedInstanceStateLocal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		savedInstanceStateLocal = savedInstanceState;
		setContentView(R.layout.activity_about);
		
		WebView aboutWebView = (WebView) findViewById(R.id.aboutView);
		aboutWebView.setVerticalScrollBarEnabled(false);
		aboutWebView.loadUrl("file:///android_asset/html/about.html");
	}
}

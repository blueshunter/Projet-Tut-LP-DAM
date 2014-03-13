package dam.ptut.iutunice;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TwitterActivity extends Activity {

	ProgressDialog chargement;
	WebView twitter;
	NetworkInfo networkInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);

		chargement = new ProgressDialog(TwitterActivity.this);
		chargement.setMessage("Chargement de la page...");
		//chargement.setCancelable(false);
		chargement.show();

		// Connexion Internet en cours ou non
		ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo = connect.getActiveNetworkInfo();

		twitter = (WebView) findViewById(R.id.webViewTwitter);
		String url = "https://twitter.com/Univ_Nice";
		twitter.getSettings().setLoadsImagesAutomatically(true);
		twitter.getSettings().setJavaScriptEnabled(true);
		twitter.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		twitter.loadUrl(url);
		twitter.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				if (networkInfo != null && networkInfo.isConnected()) {
					if (view.isDirty())
						chargement.dismiss();
				} else {
					Toast.makeText(getApplicationContext(),
							"Vous n'�tes pas connect� � Internet",
							Toast.LENGTH_LONG).show();
					finish();
				}
			}
		});
	}
}

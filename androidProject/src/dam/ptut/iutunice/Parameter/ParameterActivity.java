package dam.ptut.iutunice.Parameter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import dam.ptut.iutunice.App;
import dam.ptut.iutunice.R;
import dam.ptut.iutunice.SurveyFragment;
import dam.ptut.iutunice.SurveyItem;
import dam.ptut.iutunice.IutWindows.SurveyIutActivity;
import dam.ptut.iutunice.R.anim;
import dam.ptut.iutunice.R.drawable;
import dam.ptut.iutunice.R.id;
import dam.ptut.iutunice.R.layout;
import dam.ptut.iutunice.R.menu;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;

public class ParameterActivity extends FragmentActivity {

	// client pour connexion HTTP
	private AsyncHttpClient client = new AsyncHttpClient();
	// List sur la classe SondageItem
	private ArrayList<SurveyItem> listSurvey;
	// barre de chargement
	ProgressDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parameter);
		this.setTitle("Param�tres");

		// permet le retour sur la page principale
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// r�cup�re la liste � afficher
		collectListParameter();

		showItemsFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parameter, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void collectListParameter() {
		List<ParameterItem> liste = new ArrayList<ParameterItem>();

		String connexion = "Connexion";
		String infosWifi = "Infos Wifi";
		String aPropos = "A propos";
		String sondage = "Sondage";
		String langue = "Langues";
		String credit = "Cr�dits";
		int imgc = R.drawable.connexion;
		int imgi = R.drawable.wifi;
		int imga = R.drawable.apropos;
		int imgs = R.drawable.sondage;
		int imgl = R.drawable.flag;
		int imgcr = R.drawable.credit;
		ParameterItem i1 = new ParameterItem(connexion, imgc);
		ParameterItem i2 = new ParameterItem(infosWifi, imgi);
		ParameterItem i3 = new ParameterItem(aPropos, imga);
		ParameterItem i4 = new ParameterItem(sondage, imgs);
		ParameterItem i5 = new ParameterItem(langue, imgl);
		ParameterItem i6 = new ParameterItem(credit, imgcr);
		liste.add(i1);
		liste.add(i2);
		liste.add(i3);
		liste.add(i4);
		liste.add(i5);
		liste.add(i6);
		App app = (App) getApplication();
		app.parameterListe = liste;
	}

	private void showItemsFragment() {
		ParameterItemsFragment parameterItemsFragment = new ParameterItemsFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.vgContent, parameterItemsFragment).commit();
	}

	/**
	 * Affiche le d�tail d'un �l�ment de la liste. Est appel�e par le fragment
	 * affichant la liste, quand un �l�ment de la liste est cliqu�
	 */
	void clickItem(int position) {
		App app = (App) getApplication();
		ParameterItem parameterItem = app.parameterListe.get(position);
		switch (parameterItem.image) {
		case R.drawable.connexion:
			break;
		case R.drawable.wifi:
			ParameterWifiFragment wifiFragment = new ParameterWifiFragment();
			getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.anim.in_from_right,
							R.anim.out_to_left, R.anim.in_details,
							R.anim.out_list)
					.replace(R.id.vgContent, wifiFragment).addToBackStack(null)
					.commit();
			break;
		case R.drawable.apropos:
			break;
		case R.drawable.sondage:
				surveyClick();
			break;
		case R.drawable.flag:
			break;
		case R.drawable.credit:
			break;
		}
	}
	private void surveyClick(){
		loading = new ProgressDialog(ParameterActivity.this);
		loading.setTitle("Chargement en cours...");
		loading.setMessage("R�cup�ration du sondage depuis internet...");
		loading.setCancelable(true);
		loading.show();

		// test de la connexion internet
		ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connect.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnected()) {
			loading.dismiss();
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					ParameterActivity.this);
			alertDialog.setTitle("Connexion internet impossible...");
			alertDialog
					.setMessage("Vous n'�tes probablement pas connect� � internet...");
			alertDialog.setPositiveButton("Retour",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							finish();
						}
					});
			AlertDialog dialog = alertDialog.create();
			dialog.show();
		}

		// r�cup�re le sondage
		recoveryXmlSurvey();
	}
	private void recoveryXmlSurvey() {
		client.get(
				this,
				"http://sd-6.archive-host.com/membres/up/4041201007a5bf3f0d5112ca7648a0adc66e3177/IUT_Nice_Cote_dAzur/sondageQuestions.xml",
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(String responseBody) {
						try {
							// parse XML
							parseSurvey(responseBody);
						} catch (XmlPullParserException e) {
							e.printStackTrace();
							loading.setTitle("Erreur !");
							loading.setMessage("Erreur de lecture des donn�es. Vous pouvez retourner sur la page pr�c�dente.");
						} catch (IOException e) {
							e.printStackTrace();
							loading.setTitle("Erreur !");
							loading.setMessage("Erreur de connexion lors de la lecture des donn�es. Vous pouvez retourner sur la page pr�c�dente.");
						}
						loading.dismiss();
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								ParameterActivity.this);
						alertDialog.setTitle("Langue du Sondage.");
						alertDialog
								.setMessage("Choisissez dans quelle langue voulez-vous afficher le sondage. Choose the language of the survey.");
						alertDialog.setPositiveButton("Fran�ais",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										// fran�ais
										SurveyFragment surveyFragment = new SurveyFragment();
										Bundle args = new Bundle(1);
										args.putString(SurveyFragment.LANGUAGE,
												"fr");
										args.putString(SurveyFragment.TYPE,
												"feedback");
										surveyFragment.setArguments(args);
										getSupportFragmentManager()
												.beginTransaction()
												.setCustomAnimations(R.anim.in_from_right,
														R.anim.out_to_left, R.anim.in_details,
														R.anim.out_list)
												.replace(R.id.vgContent,
														surveyFragment).addToBackStack(null)
												.commit();
									}
								});
						alertDialog.setNegativeButton("English",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// anglais
										SurveyFragment surveyFragment = new SurveyFragment();
										Bundle args = new Bundle(1);
										args.putString(SurveyFragment.LANGUAGE,
												"en");
										args.putString(SurveyFragment.TYPE,
												"feedback");
										surveyFragment.setArguments(args);
										getSupportFragmentManager()
												.beginTransaction()
												.setCustomAnimations(R.anim.in_from_right,
														R.anim.out_to_left, R.anim.in_details,
														R.anim.out_list)
												.replace(R.id.vgContent,
														surveyFragment).addToBackStack(null)
												.commit();
									}
								});
						AlertDialog dialog = alertDialog.create();
						dialog.show();
					}

					@Override
					public void onFailure(String responseBody, Throwable error) {
						loading.dismiss();
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								ParameterActivity.this);
						alertDialog.setTitle("Erreur de connexion !");
						alertDialog
								.setMessage("Vous n'�tes probablement pas connect� � internet...");
						alertDialog.setPositiveButton("Retour",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
									}
								});
						AlertDialog dialog = alertDialog.create();
						dialog.show();
					}
				});
	}

	// - M�thode permettant de parser le xml sondage
	private void parseSurvey(String response) throws XmlPullParserException,
			IOException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(response));
		parser.require(XmlPullParser.START_DOCUMENT, null, null); // d�but
		parser.nextTag(); // balise suivante root
		parser.require(XmlPullParser.START_TAG, null, "root"); // v�rif root
		listSurvey = new ArrayList<SurveyItem>(); // nouvelle liste

		String lang = "";
		String type = "";
		while(parser.nextTag() == XmlPullParser.START_TAG || "sondage".equals(parser.getName()) || "questionnaire".equals(parser.getName()) ){
			if("sondage".equals( parser.getName() )){
				switch(parser.getEventType()){
				case XmlPullParser.START_TAG:
					lang = parser.getAttributeValue(null, "lang");
					Log.v("langue : ", ""+lang);
					Log.v("sur balise sondage (d�but)", ""+parser.getName() );
					break;
				case XmlPullParser.END_TAG:
					Log.v("sur balise sondage (fin)", ""+parser.getName() );
					break;
				}

			}else if("questionnaire".equals( parser.getName() )){
				switch(parser.getEventType()){
				case XmlPullParser.START_TAG:
					type = parser.getAttributeValue(null, "type");
					Log.v("type : ", type);
					Log.v("sur balise questionnaire (d�but)", ""+parser.getName() );
					break;
				case XmlPullParser.END_TAG:
					Log.v("sur balise questionnaire (fin)", ""+parser.getName() );
					break;
				}
			
			}else{
				SurveyItem survey = new SurveyItem(parser, lang, type);
				listSurvey.add(survey);
			}
		}
		
		parser.require(XmlPullParser.END_TAG, null, "root");
		parser.next();
		parser.require(XmlPullParser.END_DOCUMENT, null, null);
		
		App app = (App) getApplication();
		app.surveyList = listSurvey;
	}
}

package dam.ptut.iutunice.Parameter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import dam.ptut.iutunice.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ParameterWifiFragment extends Fragment {

	//client pour connexion HTTP
	private AsyncHttpClient client = new AsyncHttpClient();
	//List sur la classe WifiItem
	private ArrayList<WifiItem> listWifi;
	//succ�s r�cup�ration liste wifi depuis internet
	boolean success = false;
		
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View content = inflater.inflate(R.layout.fragment_wifi, container,
				false);
		
		recoveryXmlWifi(content, inflater);	
		
		return content;
	}
	//- M�thode permettant d'afficher les informations de XmlWifi
	private void showXmlWifi(final View content, final LayoutInflater inflater){
		//affichage de la liste
		ListView listView = (ListView) content.findViewById(R.id.listViewWifi);
		listView.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View itemView = inflater.inflate(R.layout.item_list_wifi, parent,
						false);
				
				TextView txtSite = (TextView) itemView.findViewById(R.id.txtSite);
				TextView txtNameWifi = (TextView) itemView.findViewById(R.id.txtNameWifi);
				TextView txtSsid = (TextView) itemView.findViewById(R.id.txtSsid);
				TextView txtClef = (TextView) itemView.findViewById(R.id.txtClef);
				TextView txtLogin = (TextView) itemView.findViewById(R.id.txtLogin);
				TextView txtMdp = (TextView) itemView.findViewById(R.id.txtMdp);
				
				WifiItem wifiItem = getItem(position);
				txtSite.setText(wifiItem.place);
				txtNameWifi.setText(wifiItem.name);
				txtSsid.setText("SSID : "+wifiItem.ssid);
				txtClef.setText("Clef : "+wifiItem.key);
				txtLogin.setText("Login : "+wifiItem.login);
				txtMdp.setText("Mot de passe : "+ wifiItem.password);
				
				return itemView;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public WifiItem getItem(int position) {
				return listWifi.get(position);
			}

			@Override
			public int getCount() {
				return listWifi.size();
			}
		});
	}
	//- M�thode permettant de r�cup�rer le xml Wifi
	private void recoveryXmlWifi(final View content, final LayoutInflater inflater){
		client.get(getActivity(), "http://sd-6.archive-host.com/membres/up/4041201007a5bf3f0d5112ca7648a0adc66e3177/IUT_Nice_Cote_dAzur/infosWifi.xml", 
		new TextHttpResponseHandler() {
			@Override
			public void onFinish() {
				if(success){
					showXmlWifi(content, inflater);
				}
			}

			@Override
			public void onSuccess(String responseBody) {
				try {
				//parse XML
				parseWifi(responseBody);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), "Erreur de lecture des donn�es", Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), "Erreur de connexion lors de la lecture des donn�es", Toast.LENGTH_LONG).show();
				}
				success = true;
				//barre de progression
				ProgressBar bar = (ProgressBar) content.findViewById(R.id.progressBarWifi);
				//rend invisible la barre de chargement
				bar.setVisibility(ProgressBar.GONE);
				TextView txtLoadingWifi = (TextView) content.findViewById(R.id.txtLoadingWifi);
				txtLoadingWifi.setText("");
				
			}

			@Override
			public void onFailure(String responseBody, Throwable error) {
				//Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_LONG).show();
				TextView txtLoadingWifi = (TextView) content.findViewById(R.id.txtLoadingWifi);
				txtLoadingWifi.setText("Erreur de connexion !");
			}
		});
	}
	//- M�thode permettant de parser le xml Wifi
	private void parseWifi(String responseBody)
		throws XmlPullParserException, IOException {
		//cr�ation objet XmlPullParser
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(responseBody));
		parser.require(XmlPullParser.START_DOCUMENT, null, null);	//d�but
		parser.nextTag();	//balise suivante root
		parser.require(XmlPullParser.START_TAG, null, "root");	//v�rif root
		listWifi = new ArrayList<WifiItem>();	//nouvelle liste
		
		//Log.v("sur balise root", ""+parser.getName() );
		String place = "";
		while(parser.nextTag() == XmlPullParser.START_TAG || "site".equals(parser.getName()) ){
			if("site".equals( parser.getName() )){
				int event = parser.getEventType();
				switch(event){
				case XmlPullParser.START_TAG:
					place = parser.getAttributeValue(null,"lieu");
					//Log.v("sur balise site (d�but)", ""+parser.getName() );
					break;
				case XmlPullParser.END_TAG:
					//Log.v("sur balise site (fin)", ""+parser.getName() );
					break;
				}
			}else{
				WifiItem wifi = new WifiItem(parser, place);
				listWifi.add(wifi);
			}
		}
		
		parser.require(XmlPullParser.END_TAG, null, "root");
		parser.next();
		parser.require(XmlPullParser.END_DOCUMENT, null, null);
	}

}

package dam.ptut.iutunice.PostBac;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import dam.ptut.iutunice.R;
import dam.ptut.iutunice.SurveyAnswer;
import dam.ptut.iutunice.SurveyQuestion;

public class PostBacActivity extends Activity {

	ExpandableListAdapter listAdapter;
	ExpandableListView expandableListViewPostBac;
	HashMap<String, String> listDataChild; //formations, description
	ArrayList<String> formations;
	ArrayList<String> description;
	LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_bac);
		
		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.setTitle("Informations Post-Bac");
		// permet le retour sur la page principale
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		expandableListViewPostBac = (ExpandableListView) findViewById(R.id.expandableListViewPostBac);

		//charge les informations qui s'afficheront sur la page
		completeList();
		
		expandableListViewPostBac.setAdapter(new BaseExpandableListAdapter() {
			
			@Override
			public boolean isChildSelectable(int groupPosition, int childPosition) {
				//permet le clic sur l'enfant
				return true;
			}
			
			@Override
			public boolean hasStableIds() {
				return false;
			}
			
			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				String formation = (String) getGroup(groupPosition);
				if(convertView == null){
					convertView = inflater.inflate(R.layout.list_group_postbac, parent,
							false);				
				}
				
				TextView txtFormation = (TextView) convertView.findViewById(R.id.txtFormation);
				txtFormation.setText(formation);
				
				return convertView;
			}
			
			@Override
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}
			
			@Override
			public int getGroupCount() {
				return formations.size();
			}
			
			@Override
			public String getGroup(int groupPosition) {
				return formations.get(groupPosition);
			}
			
			@Override
			public int getChildrenCount(int groupPosition) {
				//String formation = formations.get(groupPosition);
				//String description = listDataChild.get(formation);
				//int nbChildren = surveyAnswer.size();
				//return nbChildren;
				return 1;
			}
			
			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				String description = (String) getChild(groupPosition, childPosition);
				
				if(convertView == null){
					convertView = inflater.inflate(R.layout.item_list_postbac, parent,
							false);				
				}
				WebView webViewPostBac = (WebView) convertView.findViewById(R.id.webViewPostBac);
				String typeMime = "text/html";
				String encoding = "utf-8";
				webViewPostBac.getSettings().setJavaScriptEnabled(true);
				webViewPostBac.loadDataWithBaseURL(null, description, typeMime, encoding, null);
				webViewPostBac.setBackgroundColor(Color.TRANSPARENT);
				
				return convertView;
			}
			
			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return childPosition;
			}
			
			@Override
			public String getChild(int groupPosition, int childPosition) {
				String formation = formations.get(groupPosition);	
				String description = listDataChild.get(formation);
				return description;
			}
		});

	}
	
	private void completeList(){
		formations = new ArrayList<String>();
		formations.add("DUT");
		formations.add("LP");
		formations.add("Ann�es Sp�ciales");
		
		description = new ArrayList<String>();
		String descDut = "<!DOCTYPE html><html  xmlns='http://www.w3.org/1999/xhtml' xml:lang='fr'><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /></head><body><h2>Demande de dossiers d'admission</h2><p>Comment poser sa candidature : <a href='http://www.iut.unice.fr/service-scolarite'> Service de la Scolarit�</a>, <b>Bureau 1</b>.</p><h2>Le Dipl�me Universitaire de Technologie (DUT)</h2><p>Dipl�me national � vocation professionnelle, le DUT est tr�s appr�ci� par les entreprises car il est imm�diatement reconnu sur le march� de l�emploi. Il permet � ses titulaires d�exercer des responsabilit�s techniques et d�encadrement importants tout en �tant capables de s�adapter � l��volution technologique et �conomique.</p><h2>L'organisation des �tudes</h2><p>Le DUT s�inscrit depuis 2005 dans l�espace europ�en de l�Enseignement Sup�rieur et dans le sch�ma LMD (Licence-Master-Doctorat) en adoptant une nouvelle organisation des �tudes.<br /><br /></p><p><span style='font-weight: bold;'>Formation g�n�rale et professionnelle : </span></p><ul><li>de 1600 � 1800 heures d�enseignement dispens�es selon la sp�cialit� ; enseignements d�finis par des Programmes P�dagogiques Nationaux.</li><li>Stages en entreprise et projets tuteur�s effectu�s durant le cursus.</li></ul><span style='font-weight: bold;'>Formation dispens�e sur 4 semestres</span><p>Le contenu des enseignements est organis� sur 4 semestres avec un syst�me de cr�dits capitalisables. Chaque semestre est compos� de 1 � 4 Unit�s d�Enseignement (U.E.), elles-m�mes divis�es en modules d�enseignement.</p><br /><span style='font-weight: bold;'> Projet Personnel et Professionnel (P.P.P.)</span><p>Le Projet Personnel et Professionnel de l��tudiant est un dispositif d�accompagnement des �tudiants dans l��laboration de leur projet personnel et professionnel. Un dispositif d�accueil, de tutorat et de soutien � l�orientation de chaque �tudiant est mis en place afin de favoriser la construction et la r�ussite de son Projet Personnel et Professionnel tout au long de son cursus.</p><h2>Les conditions d'admission</h2><p>Le DUT se pr�pare en formation initiale, <a class='link-iut' href='http://www.iut.unice.fr/formations/type/id/5'>en alternance</a> ou en <a class='link-iut' href='http://www.iut.unice.fr/alternance-apprentissage'>formation continue</a>.</p><ul><li>Pour candidater au DUT en formation initiale, il faut �tre titulaire d�un baccalaur�at ou d�un titre admis en �quivalence.</li><li><a class='link-iut' href='http://www.iut.unice.fr/formations/type/id/5'>L�alternance</a> s�adresse aux jeunes de 16 � 25 ans, titulaires d�un baccalaur�at ou d�une formation �quivalente.</li><li><a class='link-iut' href='http://www.iut.unice.fr/alternance-apprentissage'>En formation continue</a>, les candidats peuvent suivre l�int�gralit� des formations ou bien des modules, ou encore b�n�ficier de la validation des acquis professionnels et de l�exp�rience.</li></ul><h2>L'�valuation</h2><p>Le niveau des connaissances est contr�l� de fa�on r�guli�re et continue au cours des quatre semestres. Le DUT est obtenu apr�s validation de tous les semestres, ce qui donne lieu � l�attribution de 120 cr�dits europ�ens (ECTS), � raison de 30 cr�dits europ�ens par semestre.<br />Les jurys sont compos�s d�enseignants et de repr�sentants de la profession concern�s par le dipl�me.</p><h2>L'ann�e sp�ciale</h2><p>Certains DUT peuvent �tre pr�par�s en une seule ann�e appel�e � ann�e sp�ciale �. Cette possibilit� est accord�e aux �tudiants pouvant justifier d�un niveau Bac + 2 et souhaitant acc�der � la vie professionnelle par une formation dipl�mante technologique courte, gr�ce � un enseignement adapt� et intensif.</p><h2>Apr�s le DUT</h2><strong>Une Insertion professionnelle imm�diate</strong> ou une sp�cialisation (Dipl�me d�Universit� Post-DUT). Certification de niveau III : Technicien Sup�rieur. <br /><strong>Une poursuite d��tudes courtes</strong> en Licence Professionnelle<br />Certification de niveau II : Cadre Interm�diaire.<br /><strong>Une poursuite d��tudes longues</strong> en Master, IUP, �coles d�ing�nieur, �coles de commerce, etc.<br />Certification de niveau I : Cadre Sup�rieur.<br /></body></html>";
		description.add(descDut);
		String descLp = "<!DOCTYPE html><html  xmlns='http://www.w3.org/1999/xhtml' xml:lang='fr'><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /></head><body><h2>Demande de dossiers d'admission</h2><p>Pour poser votre candidature, Connectez vous sur <a href='https://adiut1.iut-candidatures.fr/CiellCommun/' target='_blank' title='demandes de dossier d admission niveau bac +2'>CIELL2</a>.</p><h2>La Licence Professionnelle (LP)</h2><p>Cr��e en 1999 dans le cadre de l'harmonisation europ�enne de l'enseignement sup�rieur, la Licence Professionnelle est un dipl�me qui correspond au premier niveau de cadre (Bac+3). Cette formation est organis�e sur une ann�e (ou deux semestres).</p><p> Comme le DUT, la sp�cificit� de ce dipl�me r�side dans son mode d'�laboration fond� sur la mise en place de partenariats �troits entre Universit�s, �tablissements de formation, entreprises et branches professionnelles.</p><h2>L'insertion professionnelle favoris�e</h2><p>	La Licence Professionnelle est con�ue dans l'objectif d'insertion professionnelle. Elle r�pond aux engagements europ�ens qui pr�voient un cursus licence adapt� aux exigences du march� du travail ainsi qu'� la demande de nouvelles qualifications, entre le niveau technicien sup�rieur et le niveau ing�nieur-cadre sup�rieur.</p><p>Cette formation conduit � l'obtention de connaissances et de comp�tences nouvelles dans les secteurs concern�s et permet l'acc�s aux <strong>emplois de responsabilit� dans les PME-PMI</strong> ou <strong>dans les services de grandes entreprises</strong>.</p><p>Ce dipl�me doit permettre aux �tudiants qui le souhaitent d'acqu�rir rapidement une qualification professionnelle r�pondant � des besoins et � des m�tiers clairement identifi�s.</p><p>La Licence Professionnelle propose des enseignements th�oriques, des enseignements pratiques, l'apprentissage de m�thodes et d'outils. Elle comprend des p�riodes de formation en milieu professionnel, notamment lors du stage (environ 16 semaines en entreprise) et du projet tutor�.</p><p>La Licence Professionnelle r�alise une mise en contact r�elle de l'�tudiant avec le monde du travail de mani�re � lui permettre d'approfondir ses connaissances et son projet professionnel et � faciliter son insertion dans la vie active.</p><h2>Les conditions d'admission</h2><p>La Licence Professionnelle peut se pr�parer en formation initiale et en <a href='http://www.iut.unice.fr/alternance-apprentissage'>formation continue</a>. Ce cursus est ouvert � des publics diversifi�s. Des parcours diff�renci�s permettent de conduire des jeunes issus de fili�res diff�rentes vers les m�mes comp�tences. </p><p>Elle est accessible aux �tudiants pouvant justifier :</p><dl><dt>Soit d'un BAC+2 ce qui permet :</dt><dd>- aux titulaires d'un brevet de technicien sup�rieur (B.T.S) ou d'un Dipl�me d'Universit� de Technologie (D.U.T) d'obtenir un niveau sup�rieur de qualification dans le prolongement de leurs �tudes ant�rieures.</dd><dd>- aux titulaires d'une deuxi�me ann�e de Licence (L2) d'obtenir en un an un dipl�me facilitant leur insertion dans la vie active.</dd><dt>Soit de la validation des �tudes, exp�riences professionnelles ou acquis.</dt></dl><p>Elle offre ainsi aux techniciens en situation d'activit� professionnelle la possibilit� de d�velopper leur carri�re. Elle a notamment recours � la validation des acquis de l'exp�rience professionnelle.</p><h2>Evaluation</h2><p>Le niveau des connaissances est contr�l� de fa�on r�guli�re et continue au cours de la formation. L'�valuation du stage et du projet tuteur� est aussi d�terminante que l'�valuation des unit�s d'enseignement. Le jury comprend, pour au moins un quart et au plus la moiti�, des professionnels des secteurs concern�s par la Licence Professionnelle.</p></body></html>";
		description.add(descLp);
		String descAS = "<!DOCTYPE html><html  xmlns='http://www.w3.org/1999/xhtml' xml:lang='fr'><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /></head><body><h2>Demande de dossiers d'admission</h2><p>Pour poser votre candidature, contacter <a class='link-iut' href='http://www.iut.unice.fr/pages/dossier/id/2'>le Service de la Scolarit�</a>, <strong>Bureau 1</strong>.<h2>Le DUT Ann�e Sp�ciale</h2><p>Cette formation permet de pr�parer un DUT en une seule ann�e, gr�ce � <strong>un enseignement adapt� et intensif</strong>.</p><p>La dur�e de la scolarit� est de 30 � 36 semaines, selon les DUT, dont <strong>10 semaines de stages en entreprise</strong>.</p><p>La pr�sence des �tudiants est obligatoire � tous les cours, travaux dirig�s, conf�rences et stages en entreprises.</p><p>L'Ann�e Sp�ciale est accessible aux �tudiants pouvant justifier d'un niveau Bac + 2 (deuxi�me ann�e de premier cycle universitaire), et aux titulaires d'un Bac + 2 (L2, BTS, DUT ou dipl�me �quivalent) qui souhaitent acqu�rir <strong>une double comp�tence</strong>.</p></body></html>";
		description.add(descAS);
			
		//listDataHeader �gal � formations
		listDataChild = new HashMap<String, String>();
		for(int i=0 ; i<formations.size() ; i++){
			String oneFormation = formations.get(i);
			String oneDescription = description.get(i);
			listDataChild.put(oneFormation, oneDescription);
		}
	}

	// active sur l'action bar le bouton retour
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			overridePendingTransition(R.anim.in_details, R.anim.out_list);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// desactive sur l'action bar le bouton parametre
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setVisible(false);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_bac, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// animation de "retour"
		overridePendingTransition(R.anim.in_details, R.anim.out_list);
	}
}

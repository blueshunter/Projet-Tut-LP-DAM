package dam.ptut.iutunice.Suaps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import dam.ptut.iutunice.App;
import dam.ptut.iutunice.R;

public class SuapsActivity extends Activity {
	private ArrayList<SuapsGroup> SuapsArray;
	HashMap<String, ArrayList<SuapsChild>> listDataChild;
	LayoutInflater inflater;
	App app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suaps);

		this.setTitle("SUAPS");
		// permet le retour sur la page principale
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		app = (App) getApplication();

		try {
			FillList();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ExpandableListView elvSuaps = (ExpandableListView) this
				.findViewById(R.id.elvSuaps);
		elvSuaps.setAdapter(new BaseExpandableListAdapter() {

			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				// String title = (String) getGroup(groupPosition);
				SuapsGroup title = (SuapsGroup) getGroup(groupPosition);
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.list_group_suaps,
							parent, false);
				}
				// Log.v("debug", "title = " + title);
				TextView tvSuaps = (TextView) convertView
						.findViewById(R.id.tvSuaps);
				tvSuaps.setText(title.Title);

				return convertView;
			}

			@Override
			public long getGroupId(int groupPosition) {
				// TODO Auto-generated method stub
				return groupPosition;
			}

			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return SuapsArray.size();
			}

			@Override
			public SuapsGroup getGroup(int groupPosition) {
				// TODO Auto-generated method stub
				return SuapsArray.get(groupPosition);
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				// TODO Auto-generated method stub
				SuapsGroup suapsGroup = SuapsArray.get(groupPosition);
				ArrayList<SuapsChild> suapsChild = listDataChild
						.get(suapsGroup.Title);
				return suapsChild.size();

			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				SuapsChild child = (SuapsChild) getChild(groupPosition,
						childPosition);
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.item_list_suaps,
							parent, false);
				}

				TextView tvChild = (TextView) convertView
						.findViewById(R.id.tvSuapsChild);
				tvChild.setText(child.Name);

				return convertView;
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return childPosition;
			}

			@Override
			public SuapsChild getChild(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub

				SuapsGroup suapsGroup = SuapsArray.get(groupPosition);
				ArrayList<SuapsChild> suapsChildArray = listDataChild
						.get(suapsGroup.Title);
				return suapsChildArray.get(childPosition);
			}
		});

		elvSuaps.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				app.suapsGroupPos = groupPosition;
				app.suapsChildPos = childPosition;
				// int i = 0;

				SuapsGroup group = SuapsArray.get(groupPosition);
				SuapsChild child = group.getSuapsArray().get(childPosition);
				AlertDialog.Builder builder;

				Intent intent = new Intent(getApplicationContext(),
						SuapsDetails.class);
				switch (groupPosition) {
				case 0:
					if (child.daysArray.size() > 1) {
						intent = new Intent(getApplicationContext(),
								SuapsDetails.class);
						startActivity(intent);
					} else {
						builder = new AlertDialog.Builder(SuapsActivity.this);
						builder.setTitle(child.Name);
						builder.setMessage("L'activit� s�lectionn�e n'a pas de s�ance programmer pour le moment");
						builder.setCancelable(true);
						builder.show();

					}

					break;
				case 1:
					intent = new Intent(getApplicationContext(),
							SuapsDetails.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(getApplicationContext(),
							SuapsDetails.class);
					startActivity(intent);

					break;
				case 3:
					int i = 0;
					for (SuapsChildUEL uel : child.uelArray) {
						uel = child.uelArray.get(i);
						i++;
						builder = new AlertDialog.Builder(SuapsActivity.this);
						builder.setTitle(child.Name);
						builder.setMessage("Jour : " + uel.Day
								+ "\nHeure D�but : " + uel.StartTime
								+ "\nHeure Fin : " + uel.EndTime + "\nSite : "
								+ uel.Site);
						builder.setCancelable(true);
						builder.setPositiveButton("OK", null);
						builder.show();
					}
					// SuapsChildUEL uel = child.uelArray.get(i);

					break;

				default:
					break;
				}
				return true;
			}
		});

	}

	private void FillList() throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		SuapsArray = new ArrayList<SuapsGroup>();

		XmlResourceParser xpp = getResources().getXml(R.xml.activites);
		SuapsGroup s1 = new SuapsGroup(0, "Activit�s", xpp);

		xpp = getResources().getXml(R.xml.renseignement);
		SuapsGroup s2 = new SuapsGroup(1, "Renseignements", xpp);

		xpp = getResources().getXml(R.xml.lieux);
		SuapsGroup s3 = new SuapsGroup(2, "Lieux", xpp);

		xpp = getResources().getXml(R.xml.uel);
		SuapsGroup s4 = new SuapsGroup(3, "UEL", xpp);

		// xpp = "";
		// SuapsGroup s5 = new SuapsGroup(4, "Bonus", xpp);

		SuapsArray.add(s1);
		SuapsArray.add(s2);
		SuapsArray.add(s3);
		SuapsArray.add(s4);
		// SuapsArray.add(s5);

		// SuapsArray.get(app.suapsGroupPos).Title;
		app.suapsGroupList = SuapsArray;
		// app.suapsList = SuapsArray.get(app.suapsGroupPos).SuapsArray;

		listDataChild = new HashMap<String, ArrayList<SuapsChild>>();
		for (int i = 0; i < SuapsArray.size(); i++) {
			SuapsGroup suapsGroup = (SuapsGroup) SuapsArray.get(i);
			ArrayList<SuapsChild> childArray = suapsGroup.SuapsArray;
			listDataChild.put(suapsGroup.Title, childArray);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.suaps, menu);
		return true;
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
}

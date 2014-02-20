package dam.ptut.iutunice;

import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.setTitle("IUT Nice C�te d'Azur");
		
		Toast.makeText(getApplicationContext(),"version Android  1= "+Build.DISPLAY, Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(),"version Android  2= "+VERSION.CODENAME, Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(),"version Android  3= "+VERSION.SDK_INT, Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		IconMenuFragment iconMenuFragment = new IconMenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.flContent, iconMenuFragment).commit();
		
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
            openSettings();
			return true;
		case R.id.action_list:
			switchListMenu();
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

	private void switchListMenu() {
		// TODO Auto-generated method stub
		
	}

	private void openSettings() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this,
				ParameterActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}


}

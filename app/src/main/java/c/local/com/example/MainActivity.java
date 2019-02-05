package c.local.com.example;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		ft.replace(R.id.content, new BlankFragment());
		ft.commit();
	}

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
}

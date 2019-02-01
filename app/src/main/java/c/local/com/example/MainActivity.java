package c.local.com.example;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import c.local.com.example.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		ft.replace(R.id.content, new ItemFragment());
		ft.commit();
	}

	@Override
	public void onListFragmentInteraction(DummyContent.DummyItem item) {

	}
}

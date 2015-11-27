package io.github.froger.instamaterial.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.froger.instamaterial.R;

/**
 * Created by PLUUSYSTEM-NEW on 2015-11-23.
 */
public class BaseActivity extends AppCompatActivity {
	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.ivLogo)
	ImageView ivLogo;

	private MenuItem inboxMenuItem;

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ButterKnife.bind(this);
		setupToolbar();
	}

	protected void setupToolbar() {
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setNavigationIcon(R.drawable.ic_menu_white);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		inboxMenuItem = menu.findItem(R.id.action_inbox);
		inboxMenuItem.setActionView(R.layout.menu_item_view);
		return true;
	}

	public Toolbar getToolbar() {
		return toolbar;
	}

	public MenuItem getInboxMenuItem() {
		return inboxMenuItem;
	}

	public ImageView getIvLogo() {
		return ivLogo;
	}
}

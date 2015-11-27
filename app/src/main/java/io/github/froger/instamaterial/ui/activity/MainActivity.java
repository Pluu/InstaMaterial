package io.github.froger.instamaterial.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.froger.instamaterial.R;
import io.github.froger.instamaterial.Utils;
import io.github.froger.instamaterial.ui.adapter.FeedAdapter;
import io.github.froger.instamaterial.ui.view.FeedContextMenu;
import io.github.froger.instamaterial.ui.view.FeedContextMenuManager;

public class MainActivity extends BaseActivity
	implements FeedAdapter.OnFeedItemClickListener,
			   FeedContextMenu.OnFeedContextMenuItemClickListener {

	@Bind(R.id.rvFeed)
	RecyclerView rvFeed;
	@Bind(R.id.btnCreate)
	ImageButton btnCreate;

	private FeedAdapter feedAdapter;

	private boolean pendingIntroAnimation;
	private static final int ANIM_DURATION_TOOLBAR = 300;

	//FAB animation
	private static final int ANIM_DURATION_FAB = 400;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		if (savedInstanceState == null) {
			pendingIntroAnimation = true;
		}

		setupFeed();
	}

	private void setupFeed() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
			@Override
			protected int getExtraLayoutSpace(RecyclerView.State state) {
				return 300;
			}
		};
		rvFeed.setLayoutManager(linearLayoutManager);
		feedAdapter = new FeedAdapter(this);
		feedAdapter.setOnFeedItemClickListener(this);
		rvFeed.setAdapter(feedAdapter);
		rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (pendingIntroAnimation) {
			pendingIntroAnimation = false;
			startIntroAnimation();
		}

		return true;
	}

	private void startIntroAnimation() {

		btnCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

		int actionbarSize = Utils.dpToPx(56);
		getToolbar().setTranslationY(-actionbarSize);
		getIvLogo().setTranslationY(-actionbarSize);
		getInboxMenuItem().getActionView().setTranslationY(-actionbarSize);

		getToolbar().animate()
			   .translationY(0)
			   .setDuration(ANIM_DURATION_TOOLBAR)
			   .setStartDelay(300);
		getIvLogo().animate()
			  .translationY(0)
			  .setDuration(ANIM_DURATION_TOOLBAR)
			  .setStartDelay(400);
		getInboxMenuItem().getActionView().animate()
					 .translationY(0)
					 .setDuration(ANIM_DURATION_TOOLBAR)
					 .setStartDelay(500)
					 .setListener(new AnimatorListenerAdapter() {
						 @Override
						 public void onAnimationEnd(Animator animation) {
							 startContentAnimation();
						 }
					 })
					 .start();
	}

	private void startContentAnimation() {
		btnCreate.animate()
				 .translationY(0)
				 .setInterpolator(new OvershootInterpolator(1.f))
				 .setStartDelay(300)
				 .setDuration(ANIM_DURATION_FAB)
				 .start();
		feedAdapter.updateItems();
	}

	@Override
	public void onCommentsClick(View v, int position) {
		final Intent intent = new Intent(this, CommentsActivity.class);

		//Get location on screen for tapped view
		int[] startingLocation = new int[2];
		v.getLocationOnScreen(startingLocation);
		intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);

		startActivity(intent);
		//Disable enter transition for new Activity
		overridePendingTransition(0, 0);
	}

	@Override
	public void onMoreClick(View v, int position) {
		FeedContextMenuManager.getInstance().toggleContextMenuFromView(v, position, this);
	}

	@Override
	public void onReportClick(int feedItem) {
		FeedContextMenuManager.getInstance().hideContextMenu();
	}

	@Override
	public void onSharePhotoClick(int feedItem) {
		FeedContextMenuManager.getInstance().hideContextMenu();
	}

	@Override
	public void onCopyShareUrlClick(int feedItem) {
		FeedContextMenuManager.getInstance().hideContextMenu();
	}

	@Override
	public void onCancelClick(int feedItem) {
		FeedContextMenuManager.getInstance().hideContextMenu();
	}

	@Override
	public void onProfileClick(View v) {
		int[] startingLocation = new int[2];
		v.getLocationOnScreen(startingLocation);
		startingLocation[0] += v.getWidth() / 2;
		UserProfileActivity.startUserProfileFromLocation(startingLocation, this);
		overridePendingTransition(0, 0);
	}
}

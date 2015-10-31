package io.github.froger.instamaterial.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.froger.instamaterial.R;
import io.github.froger.instamaterial.adapter.CommentsAdapter;

/**
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class CommentsActivity extends AppCompatActivity {

	public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

	@Bind(R.id.toolbar)
	Toolbar toolbar;
	@Bind(R.id.contentRoot)
	LinearLayout contentRoot;
	@Bind(R.id.rvComments)
	RecyclerView rvComments;
	@Bind(R.id.llAddComment)
	LinearLayout llAddComment;

	private CommentsAdapter commentsAdapter;
	private int drawingStartLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		ButterKnife.bind(this);

		setupComments();

		drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
		if (savedInstanceState == null) {
			contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				@Override
				public boolean onPreDraw() {
					contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
					startIntroAnimation();
					return true;
				}
			});
		}
	}

	private void setupComments() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		rvComments.setLayoutManager(linearLayoutManager);
		rvComments.setHasFixedSize(true);

		commentsAdapter = new CommentsAdapter(this);
		rvComments.setAdapter(commentsAdapter);
		rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
		rvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
					commentsAdapter.setAnimationsLocked(true);
				}
			}
		});
	}

	private void startIntroAnimation() {
		contentRoot.setScaleY(0.1f);
		contentRoot.setPivotY(drawingStartLocation);
		llAddComment.setTranslationY(100);

		contentRoot.animate()
				   .scaleY(1)
				   .setDuration(200)
				   .setInterpolator(new AccelerateInterpolator())
				   .setListener(new AnimatorListenerAdapter() {
					   @Override
					   public void onAnimationEnd(Animator animation) {
						   animateContent();
					   }
				   })
				   .start();
	}

	private void animateContent() {
		commentsAdapter.updateItems();
		llAddComment.animate().translationY(0)
					.setInterpolator(new DecelerateInterpolator())
					.setDuration(200)
					.start();
	}
}

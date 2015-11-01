package io.github.froger.instamaterial.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.froger.instamaterial.R;
import io.github.froger.instamaterial.Utils;
import io.github.froger.instamaterial.ui.view.SquaredImageView;

/**
 * Created by PLUUSYSTEM-NEW on 2015-10-19.
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
	implements View.OnClickListener {

	public interface OnFeedItemClickListener {
		public void onCommentsClick(View v, int position);
		public void onMoreClick(View v, int position);
	}

	private static final int ANIMATED_ITEMS_COUNT = 2;

	private Context context;
	private int lastAnimatedPosition = -1;
	private int itemsCount = 0;

	private OnFeedItemClickListener onFeedItemClickListener;

	public FeedAdapter(Context context) {
		this.context = context;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
		return new CellFeedViewHolder(view);
	}

	private void runEnterAnimation(View view, int position) {
		if (position >= ANIMATED_ITEMS_COUNT - 1) {
			return;
		}

		if (position > lastAnimatedPosition) {
			lastAnimatedPosition = position;
			view.setTranslationY(Utils.getScreenHeight(context));
			view.animate()
				.translationY(0)
				.setInterpolator(new DecelerateInterpolator(3.f))
				.setDuration(700)
				.start();
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		runEnterAnimation(viewHolder.itemView, position);
		CellFeedViewHolder holder = (CellFeedViewHolder) viewHolder;
		if (position % 2 == 0) {
			holder.ivFeedCenter.setImageResource(R.drawable.img_feed_center_1);
			holder.ivFeedBottom.setImageResource(R.drawable.img_feed_bottom_1);
		} else {
			holder.ivFeedCenter.setImageResource(R.drawable.img_feed_center_2);
			holder.ivFeedBottom.setImageResource(R.drawable.img_feed_bottom_2);
		}
		holder.btnComments.setOnClickListener(this);
		holder.btnComments.setTag(position);
		holder.btnMore.setOnClickListener(this);
		holder.btnMore.setTag(position);
	}

	@Override
	public int getItemCount() {
		return itemsCount;
	}

	public static class CellFeedViewHolder extends RecyclerView.ViewHolder {
		@Bind(R.id.ivFeedCenter)
		SquaredImageView ivFeedCenter;
		@Bind(R.id.ivFeedBottom)
		ImageView ivFeedBottom;
		@Bind(R.id.btnComments)
		ImageButton btnComments;
		@Bind(R.id.btnMore)
		ImageButton btnMore;

		public CellFeedViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}

	public void updateItems() {
		itemsCount = 10;
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		final int viewId = v.getId();
		if (viewId == R.id.btnComments) {
			if (onFeedItemClickListener != null) {
				onFeedItemClickListener.onCommentsClick(v, (Integer) v.getTag());
			}
		} else if (viewId == R.id.btnMore) {
			if (onFeedItemClickListener != null) {
				onFeedItemClickListener.onMoreClick(v, (Integer) v.getTag());
			}
		}
	}

	public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
		this.onFeedItemClickListener = onFeedItemClickListener;
	}

}
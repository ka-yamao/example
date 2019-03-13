package c.local.com.example;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

import c.local.com.example.ItemFragment.OnListFragmentInteractionListener;
import c.local.com.example.data.Item;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

	private final List<Item> mValues;
	private final OnListFragmentInteractionListener mListener;
	private Animation inAnimation;
	private Animation outAnimation;

	public ItemAdapter(Context context, List<Item> items, OnListFragmentInteractionListener listener) {
		mValues = items;
		mListener = listener;
		inAnimation = (Animation) AnimationUtils.loadAnimation(context, R.anim.animation);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.mItem = mValues.get(position);
		holder.title.setText(mValues.get(position).getTitle());
		holder.udate.setText(mValues.get(position).getUdate());
		holder.timestamp.setText(mValues.get(position).getTimestamp());

		holder.timestamp.startAnimation(inAnimation);

		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mListener) {
					// Notify the active callbacks interface (the activity, if the
					// fragment is attached to one) that an item has been selected.
					mListener.onListFragmentInteraction(holder.mItem);
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return mValues.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View view;
		public final TextView title;
		public final TextView udate;
		public final TextView timestamp;

		public Item mItem;

		public ViewHolder(View view) {
			super(view);
			this.view = view;
			title = (TextView) view.findViewById(R.id.title);
			udate = (TextView) view.findViewById(R.id.udate);
			timestamp = (TextView) view.findViewById(R.id.timestamp);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + udate.getText() + "'";
		}
	}
}

package c.local.com.example;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import c.local.com.example.ItemFragment.OnListFragmentInteractionListener;
import c.local.com.example.data.Item;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

	private final List<Item> mValues;
	private final OnListFragmentInteractionListener mListener;

	public ItemAdapter(List<Item> items, OnListFragmentInteractionListener listener) {
		mValues = items;
		mListener = listener;
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
		holder.body.setText(Html.fromHtml(mValues.get(position).getBody()));

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
		public final TextView body;

		public Item mItem;

		public ViewHolder(View view) {
			super(view);
			this.view = view;
			title = (TextView) view.findViewById(R.id.title);
			body = (TextView) view.findViewById(R.id.body);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + body.getText() + "'";
		}
	}
}

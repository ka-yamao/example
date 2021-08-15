package c.local.com.example.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import c.local.com.example.BasicApp;
import c.local.com.example.R;
import c.local.com.example.data.Pokemon;
import c.local.com.example.databinding.PokemonListItemBinding;
import c.local.com.example.ui.PokemonClickCallback;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ProductViewHolder> {

	public static final String TAG = PokemonAdapter.class.getSimpleName();

	List<? extends Pokemon> mPokemonList;

	@Nullable
	private final PokemonClickCallback mPokemonClickCallback;

	public PokemonAdapter(@Nullable PokemonClickCallback clickCallback) {
		mPokemonClickCallback = clickCallback;
		setHasStableIds(true);
	}

	public void setPokemonList(final List<? extends Pokemon> pokemonList) {
		if (mPokemonList == null) {
			mPokemonList = pokemonList;
			notifyItemRangeInserted(0, pokemonList.size());
		} else {
			mPokemonList = pokemonList;
			notifyDataSetChanged();
//			DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//				@Override
//				public int getOldListSize() {
//					return mPokemonList.size();
//				}
//
//				@Override
//				public int getNewListSize() {
//					return pokemonList.size();
//				}
//
//				@Override
//				public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//					int oldId = mPokemonList.get(oldItemPosition).getId();
//					int newId = pokemonList.get(newItemPosition).getId();
//					DLog.d(TAG, "oldId:"+ oldId + ", newId:" + newId);
//					return mPokemonList.get(oldItemPosition).getId() ==
//							pokemonList.get(newItemPosition).getId();
//				}
//
//				@Override
//				public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//					Pokemon newProduct = pokemonList.get(newItemPosition);
//					Pokemon oldProduct = mPokemonList.get(oldItemPosition);
//					return newProduct.getId() == oldProduct.getId()
//							&& TextUtils.equals(newProduct.getName(), oldProduct.getName());
//				}
//			});
//			mPokemonList = pokemonList;
//			result.dispatchUpdatesTo(this);
		}
	}

	@Override
	@NonNull
	public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		PokemonListItemBinding binding = DataBindingUtil
				.inflate(LayoutInflater.from(parent.getContext()), R.layout.pokemon_list_item,
						parent, false);
		binding.setCallback(mPokemonClickCallback);
		return new ProductViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
		Pokemon pokemon = mPokemonList.get(position);
		holder.binding.setPokemon(pokemon);
		Glide.with(BasicApp.getApp()).load(pokemon.getUrl()).into(holder.binding.image);
		holder.binding.executePendingBindings();
	}

	@Override
	public int getItemCount() {
		return mPokemonList == null ? 0 : mPokemonList.size();
	}

	@Override
	public long getItemId(int position) {
		return mPokemonList.get(position).getId();
	}

	static class ProductViewHolder extends RecyclerView.ViewHolder {

		final PokemonListItemBinding binding;

		public ProductViewHolder(PokemonListItemBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}
	}
}

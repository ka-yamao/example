package c.local.com.example.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import c.local.com.example.adapter.PokemonAdapter
import c.local.com.example.data.Pokemon
import c.local.com.example.databinding.KotlinFragmentBinding
import c.local.com.example.viewmodel.KotlinViewModel

class KotlinFragment : Fragment() {

    companion object {
        fun newInstance() = KotlinFragment()
    }

    private lateinit var binding: KotlinFragmentBinding

    private lateinit var viewModel: KotlinViewModel

    private lateinit var adapter: PokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = KotlinFragmentBinding.inflate(inflater, container, false)
        // もしくは下記で、KotlinFragmentBinding を取得
        /*
        binding = DataBindingUtil.inflate<KotlinFragmentBinding>(
            inflater, R.layout.kotlin_fragment,
            container,
            false
        )
        */
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(KotlinViewModel::class.java)

        adapter = PokemonAdapter(mPokemonClickCallback)

        binding.pokemonList.adapter = adapter
        binding.kotlinViewModel = viewModel
        subscribeUi(viewModel.getPokemonListLiveData())
        binding.refresh.setOnRefreshListener {
            viewModel.fetch(false)
        }
        // １ページ名を取得
        viewModel.fetch(false)
        // レコメンドのデータを取得
        Handler(Looper.getMainLooper()).post { viewModel.fetch(false) }
    }

    private fun subscribeUi(liveData: LiveData<List<Pokemon>>) {
        // Update the list when the data changes
        liveData.observe(
            viewLifecycleOwner,
            { pokemonList: List<Pokemon?>? ->
                if (pokemonList != null) {
                    adapter.setPokemonList(pokemonList)
                }
                binding.refresh.isRefreshing = false
                binding.executePendingBindings()
            })
    }

    private val mPokemonClickCallback =
        PokemonClickCallback { pokemon: Pokemon ->
            Toast.makeText(
                context,
                pokemon.name,
                Toast.LENGTH_SHORT
            ).show()
        }
}
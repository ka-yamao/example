package c.local.com.example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import c.local.com.example.databinding.KotlinFragmentBinding
import c.local.com.example.viewmodel.KotlinViewModel

class KotlinFragment : Fragment() {

    companion object {
        fun newInstance() = KotlinFragment()
    }

    private lateinit var binding: KotlinFragmentBinding

    private lateinit var viewModel: KotlinViewModel


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
    }
}
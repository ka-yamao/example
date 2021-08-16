package c.local.com.example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import c.local.com.example.databinding.KotlinFragmentBinding

class KotlinFragment : Fragment() {

    companion object {
        fun newInstance() = KotlinFragment()
    }

    private lateinit var viewModel: KotlinViewModel

    private var binding: KotlinFragmentBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = KotlinFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(KotlinViewModel::class.java)

    }
}
package com.example.postcard2.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.postcard2.BuildConfig
import com.example.postcard2.databinding.FragmentInputBinding
import com.example.postcard2.sources.imagesource.implementation.AssetsImageSource

class InputFragment : Fragment() {
    private lateinit var binding: FragmentInputBinding
    private val viewModel: InputViewModel by viewModels {
        InputViewModelFactory(
            AssetsImageSource(requireContext(), BuildConfig.FOO_STRING),
            AssetsImageSource(requireContext(), BuildConfig.BG_STRING)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.navigationLiveEvent.observe(viewLifecycleOwner, ::navigate)
    }

    private fun navigate(directions: NavDirections) {
        findNavController().navigate(directions)
    }
}
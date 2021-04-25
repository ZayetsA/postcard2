package com.example.postcard2.card

import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.postcard2.databinding.FragmentCardBinding
import com.google.android.material.textview.MaterialTextView


class CardFragment : Fragment() {

    private lateinit var binding: FragmentCardBinding
    private val args: CardFragmentArgs by navArgs()
    private val viewModel: CardViewModel by viewModels { CardViewModelFactory(args.inputModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        animateShowMainText()
        binding.cardContainer.transitionToEnd()
        animateText(binding.cardContainerName)
        animateText(binding.cardContainerTitle)
    }

    private fun animateShowMainText() {
        val animator = ObjectAnimator.ofFloat(binding.cardContainerText, View.ALPHA, 0f, 1f)
        animator.duration = 1000
        animator.start()
    }

    private fun animateText(layoutCardText: MaterialTextView) {
        val animator = ObjectAnimator.ofArgb(
            layoutCardText,
            "textColor", Color.BLUE, Color.RED
        )
        animator.repeatCount = Animation.INFINITE
        animator.duration = 1000
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        val preferences: SharedPreferences? = context?.getSharedPreferences("Card", Context.MODE_PRIVATE)
        val editor = preferences?.edit()
        editor?.clear()
        editor?.apply()
    }
}
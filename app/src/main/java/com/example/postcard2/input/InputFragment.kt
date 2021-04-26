package com.example.postcard2.input

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.postcard2.BuildConfig
import com.example.postcard2.databinding.FragmentInputBinding
import com.example.postcard2.presets.PresetsAdapter
import com.example.postcard2.sources.imagesource.implementation.AssetsImageSource
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException


class InputFragment : Fragment() {

    companion object {
        private const val IMAGE_CODE = 100
    }

    private lateinit var adapter: PresetsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentInputBinding
    private val viewModel: InputViewModel by viewModels {
        InputViewModelFactory(
            AssetsImageSource(requireContext(), BuildConfig.FOO_STRING),
            AssetsImageSource(requireContext(), BuildConfig.BG_STRING),
            activity
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
        binding.parentFragmentThemeNext.setOnClickListener {
            setNextPreset()
        }
        binding.parentFragmentThemePrevious.setOnClickListener {
            setPreviousPreset()
        }
        viewModel.navigationLiveEvent.observe(viewLifecycleOwner, ::navigate)
        binding.parentFragmentProfileImage.setOnClickListener {
            getImageFromGallery()
        }
        createPresetsView()
        viewModel.checkArgs()
    }

    private fun getImageFromGallery() {
        startActivityForResult(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            ), IMAGE_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {
            val selectedImage: Uri? = data?.data
            val bitmap: Bitmap?
            try {
                val uploadedBitmap =
                    MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImage)
                bitmap = Bitmap.createScaledBitmap(
                    uploadedBitmap, uploadedBitmap.width / 20, uploadedBitmap.height / 20, true
                )
                binding.parentFragmentProfileImage.setImageBitmap(bitmap)
                viewModel.model.profileImage = bitMapToString(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun createPresetsView() {
        viewModel.addDefaultThemes()
        adapter = PresetsAdapter(viewModel.listOfThemes) {
            binding.inputCardInputTitle.setText(it.title)
            binding.inputCardInputText.setText(it.text)
            viewModel.model.backgroundImage = it.imageId.toString()
            binding.inputCardContainer.setBackgroundResource(it.imageId)
        }
        with(binding) {
            parentFragmentCards.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            parentFragmentCards.layoutManager = layoutManager
            snapHelper.attachToRecyclerView(parentFragmentCards)
        }
    }

    private fun setPreviousPreset() {
        try {
            binding.parentFragmentCards.layoutManager?.smoothScrollToPosition(
                binding.parentFragmentCards,
                null,
                layoutManager.findFirstVisibleItemPosition() - 1
            )
        } catch (e: IllegalArgumentException) {
        }

    }

    private fun setNextPreset() {
        binding.parentFragmentCards.layoutManager?.smoothScrollToPosition(
            binding.parentFragmentCards,
            null,
            layoutManager.findLastVisibleItemPosition() + 1
        )
    }

    private fun navigate(directions: NavDirections) {
        findNavController().navigate(directions)
    }
}
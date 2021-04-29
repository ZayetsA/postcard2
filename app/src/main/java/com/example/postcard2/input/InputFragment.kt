package com.example.postcard2.input

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: PresetsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentInputBinding
    private val viewModel: InputViewModel by viewModels {
        InputViewModelFactory(
            AssetsImageSource(requireContext(), BuildConfig.FOO_STRING),
            AssetsImageSource(requireContext(), BuildConfig.BG_STRING),
            activity?.getPreferences(Context.MODE_PRIVATE)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputBinding.inflate(inflater)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    getResult(data)
                }
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.shouldCloseLiveData.observe(viewLifecycleOwner, { activity?.finish() })
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
        binding.inputCardInputText.setOnKeyListener { currentView, keyCode, _ ->
            handleKeyEvent(currentView, keyCode)
        }
        createPresetsView()
        viewModel.checkArgs()
    }

    private fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun getResult(data: Intent?) {
        val bitmap: Bitmap?
        try {
            val uploadedBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val src: ImageDecoder.Source? =
                    activity?.contentResolver?.let {
                        data?.data?.let { it1 ->
                            ImageDecoder.createSource(
                                it,
                                it1
                            )
                        }
                    }
                src?.let { ImageDecoder.decodeBitmap(it) }
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(activity?.contentResolver, data?.data)
            }

            bitmap = uploadedBitmap?.let {
                Bitmap.createScaledBitmap(
                    it,
                    (uploadedBitmap.width * (0.05)).toInt(),
                    (uploadedBitmap.height * (0.05)).toInt(),
                    true
                )
            }
            binding.parentFragmentProfileImage.setImageBitmap(bitmap)
            viewModel.model.profileImage = bitmap?.let { bitMapToString(it) }.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
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

            binding.parentFragmentCards.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            binding.parentFragmentCards.layoutManager = layoutManager
            snapHelper.attachToRecyclerView(binding.parentFragmentCards)
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

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    private fun navigate(directions: NavDirections) {
        findNavController().navigate(directions)
    }
}
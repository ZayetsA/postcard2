package com.example.postcard2.input

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import java.io.FileNotFoundException
import java.io.IOException


class InputFragment : Fragment() {

    companion object {
        private const val IMAGE_CODE = 100
    }

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

        viewModel.navigationLiveEvent.observe(viewLifecycleOwner, ::navigate)
        binding.parentFragmentProfileImage.setOnClickListener {
            getImageFromGallery()
        }
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
                viewModel.model.profileImage = bitmap
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun navigate(directions: NavDirections) {
        findNavController().navigate(directions)
    }


    override fun onDestroy() {
        super.onDestroy()
        val preferences: SharedPreferences? = context?.getSharedPreferences("Card", Context.MODE_PRIVATE)
        val editor = preferences?.edit()
        editor?.clear()
        editor?.apply()
    }
}
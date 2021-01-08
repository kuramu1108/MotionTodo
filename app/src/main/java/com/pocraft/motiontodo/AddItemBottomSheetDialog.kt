package com.pocraft.motiontodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pocraft.motiontodo.databinding.AddItemBottomSheetBinding
import com.pocraft.motiontodo.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AddItemBottomSheetDialog : BottomSheetDialogFragment() {
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: AddItemBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddItemBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            bottomSheetSubmit.setOnClickListener {
                val item = bottomSheetInput.text.toString()
                viewModel.addItem(item)
                dismiss()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}
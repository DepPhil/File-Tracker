package com.phaggu.filetracker.editfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.phaggu.filetracker.R
import com.phaggu.filetracker.databinding.FragmentEditBinding
import timber.log.Timber

class EditFileFragment: Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate(inflater,R.layout.fragment_edit, container, false )
        val application = requireNotNull(this.activity).application
        val fileId = EditFileFragmentArgs.fromBundle(requireArguments()).fileId
        val viewModelFactory = EditFileViewModelFactory(application, fileId)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(EditFileViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.navigateToNextFragment.observe(viewLifecycleOwner, Observer {
            it?.let{
                Timber.i("Navigating back to Detail Fragment")
                this.findNavController().navigate(
                        EditFileFragmentDirections.actionEditFileFragmentToFileDetailFragment(it)
                )
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
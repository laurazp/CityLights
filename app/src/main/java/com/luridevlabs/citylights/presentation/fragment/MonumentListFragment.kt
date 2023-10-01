package com.luridevlabs.citylights.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luridevlabs.citylights.databinding.FragmentHomeBinding
import com.luridevlabs.citylights.databinding.FragmentMonumentListBinding
import com.luridevlabs.citylights.model.ResourceState
import com.luridevlabs.citylights.presentation.adapter.MonumentListAdapter
import com.luridevlabs.citylights.presentation.viewmodel.MonumentListState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MonumentListFragment : Fragment() {

    private lateinit var binding: FragmentMonumentListBinding

    private val monumentListAdapter = MonumentListAdapter()
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonumentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initContent()
        initUI()
    }

    private fun initContent() {
        monumentsViewModel.getMonumentListLiveData().observe(viewLifecycleOwner) { state ->
            if (state != null) handleMonumentListState(state)
        }

        if (monumentsViewModel.getMonumentListLiveData().value == null) {
            monumentsViewModel.fetchMonuments()
        }
    }

    private fun handleMonumentListState(state: MonumentListState) {
        when(state) {
            is ResourceState.Loading -> {
                binding.pbMonumentList.visibility = View.VISIBLE
            }
            is ResourceState.Success -> {
                binding.pbMonumentList.visibility = View.GONE
                monumentListAdapter.submitList(state.result)
            }
            is ResourceState.Error -> {
                binding.pbMonumentList.visibility = View.GONE
                showErrorDialog(state.error)
            }
        }
    }

    private fun initUI() {
        binding.rvMonumentList.adapter = monumentListAdapter
        binding.rvMonumentList.layoutManager = LinearLayoutManager(requireContext())

        monumentListAdapter.onClickListener = { monument ->

            findNavController().navigate(
                //R.id.action_monumentListFragment_to_monumentDetailFragment
                MonumentListFragmentDirections.actionMonumentListFragmentToMonumentDetailFragment(monument.monumentId)
            )
        }
    }

    private fun showErrorDialog(error: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("Aceptar", null)
            .setNegativeButton("Reintentar") { dialog, witch ->
                monumentsViewModel.fetchMonuments()
            }
    }
}
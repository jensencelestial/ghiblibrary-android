package com.jensencelestial.ghiblibrary.android.ui.vehicle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jensencelestial.ghiblibrary.android.data.model.Vehicle
import com.jensencelestial.ghiblibrary.android.databinding.FragmentVehiclesBinding
import com.jensencelestial.ghiblibrary.android.ui.UIState
import com.jensencelestial.ghiblibrary.android.viewmodel.VehiclesViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehiclesFragment : Fragment() {
    private val vehiclesViewModel: VehiclesViewModel by viewModels()

    private var _binding: FragmentVehiclesBinding? = null
    private val binding get() = _binding!!

    private lateinit var fastAdapter: FastAdapter<VehicleItem>
    private lateinit var vehiclesAdapter: ItemAdapter<VehicleItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehiclesBinding.inflate(inflater, container, false)
        val view = binding.root

        vehiclesAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(listOf(vehiclesAdapter))

        binding.rvVehicles.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = fastAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewEvents()
        initObservers()
    }

    private fun initViewEvents() {
        binding.swltVehicles.setOnRefreshListener {
            vehiclesViewModel.getVehicles()
        }

        fastAdapter.onClickListener =
            { view: View?, iAdapter: IAdapter<VehicleItem>, vehicleItem: VehicleItem, i: Int ->
                findNavController().navigate(
                    VehiclesFragmentDirections.actionVehiclesFragmentToVehicleFragment(
                        vehicleItem.vehicle.id
                    )
                )
                false
            }
    }

    private fun initObservers() {
        vehiclesViewModel.vehicles.observe(this.viewLifecycleOwner, VehiclesObserver())
    }

    private inner class VehiclesObserver : Observer<UIState<List<Vehicle>>> {
        override fun onChanged(vehicles: UIState<List<Vehicle>>) {
            when (vehicles) {
                is UIState.Loading -> {
                    if (!binding.swltVehicles.isRefreshing) {
                        binding.swltVehicles.isRefreshing = true
                    }
                }
                is UIState.Success -> {
                    binding.swltVehicles.isRefreshing = false

                    vehiclesAdapter.clear()
                    vehiclesAdapter.add(vehicles.value.map { VehicleItem(it) })
                }
                is UIState.Failure -> {
                    binding.swltVehicles.isRefreshing = false
                }
            }
        }
    }
}
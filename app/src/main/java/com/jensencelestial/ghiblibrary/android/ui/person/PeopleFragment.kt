package com.jensencelestial.ghiblibrary.android.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jensencelestial.ghiblibrary.android.data.model.Person
import com.jensencelestial.ghiblibrary.android.databinding.FragmentPeopleBinding
import com.jensencelestial.ghiblibrary.android.ui.UIState
import com.jensencelestial.ghiblibrary.android.viewmodel.PeopleViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeopleFragment : Fragment() {
    private val peopleViewModel: PeopleViewModel by viewModels()

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private lateinit var fastAdapter: FastAdapter<PersonItem>
    private lateinit var peopleAdapter: ItemAdapter<PersonItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        val view = binding.root

        peopleAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(listOf(peopleAdapter))

        binding.rvPeople.apply {
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
        binding.swltPeople.setOnRefreshListener {
            peopleViewModel.getPeople()
        }

        fastAdapter.onClickListener =
            { view: View?, iAdapter: IAdapter<PersonItem>, personItem: PersonItem, i: Int ->
                findNavController().navigate(
                    PeopleFragmentDirections.actionPeopleFragmentToPersonFragment(
                        personItem.person.id
                    )
                )
                false
            }
    }

    private fun initObservers() {
        peopleViewModel.people.observe(this.viewLifecycleOwner, PeopleObserver())
    }

    private inner class PeopleObserver : Observer<UIState<List<Person>>> {
        override fun onChanged(people: UIState<List<Person>>) {
            when (people) {
                is UIState.Loading -> {
                    if (!binding.swltPeople.isRefreshing) {
                        binding.swltPeople.isRefreshing = true
                    }
                }
                is UIState.Success -> {
                    binding.swltPeople.isRefreshing = false

                    peopleAdapter.clear()
                    peopleAdapter.add(people.value.map { PersonItem(it) })
                }
                is UIState.Failure -> {
                    binding.swltPeople.isRefreshing = false
                }
            }
        }
    }
}
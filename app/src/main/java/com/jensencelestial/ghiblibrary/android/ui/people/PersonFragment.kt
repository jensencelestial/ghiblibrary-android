package com.jensencelestial.ghiblibrary.android.ui.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.jensencelestial.ghiblibrary.android.data.model.Person
import com.jensencelestial.ghiblibrary.android.databinding.FragmentPersonBinding
import com.jensencelestial.ghiblibrary.android.ui.UIState
import com.jensencelestial.ghiblibrary.android.viewmodel.PersonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonFragment : Fragment() {
    private val personViewModel: PersonViewModel by viewModels()

    private var _binding: FragmentPersonBinding? = null
    private val binding get() = _binding!!

    private val args: PersonFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewEvents()
        initObservers()

        personViewModel.getPerson(args.personId)
    }

    private fun initViewEvents() {
        binding.swltPerson.setOnRefreshListener {
            personViewModel.getPerson(args.personId)
        }
    }

    private fun initObservers() {
        personViewModel.person.observe(this.viewLifecycleOwner, PersonObserver())
    }

    private fun setPersonContent(person: Person) {
        binding.lytPerson.tvName.text = person.name

        binding.lytPerson.tvGender.text = person.gender

        binding.lytPerson.tvAge.text = person.age

        binding.lytPerson.tvEyeColor.text = person.eyeColor

        binding.lytPerson.tvHairColor.text = person.hairColor

        binding.lytPerson.ivThumbnail.load(person.imageUrl) {
            crossfade(true)
        }
    }

    private inner class PersonObserver : Observer<UIState<Person>> {
        override fun onChanged(person: UIState<Person>) {
            when (person) {
                is UIState.Loading -> {
                    if (!binding.swltPerson.isRefreshing) {
                        binding.swltPerson.isRefreshing = true
                    }
                }
                is UIState.Success -> {
                    binding.swltPerson.isRefreshing = false

                    setPersonContent(person.value)
                }
                is UIState.Failure -> {
                    binding.swltPerson.isRefreshing = false
                }
            }
        }
    }
}
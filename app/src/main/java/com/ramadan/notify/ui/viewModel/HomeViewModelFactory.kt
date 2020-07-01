package com.ramadan.notify.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramadan.notify.data.repository.NoteRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }

}
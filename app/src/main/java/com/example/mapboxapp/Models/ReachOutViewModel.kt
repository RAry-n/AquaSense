package com.example.mapboxapp.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapboxapp.IssueRepository
import com.example.mapboxapp.dataclass.problems

class ReachOutViewModel : ViewModel(){

    private val repository : IssueRepository = IssueRepository().getInstance()
    private val _allItems = MutableLiveData<List<problems>>()
    val allItems : LiveData<List<problems>> = _allItems

    init {
        repository.loadItems(_allItems)
    }
}
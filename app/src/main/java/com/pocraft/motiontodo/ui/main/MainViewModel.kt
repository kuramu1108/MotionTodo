package com.pocraft.motiontodo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {
    private val todos = MutableStateFlow<List<String>>(emptyList())
    private val alarms = MutableStateFlow<List<String>>(emptyList())
    private val events = MutableStateFlow<List<String>>(emptyList())
    private val transitionState = MutableStateFlow(TransitionState.NONE)

    data class ViewState(
        val todos: List<String> = emptyList(),
        val alarms: List<String> = emptyList(),
        val events: List<String> = emptyList(),
        val transitionState: TransitionState = TransitionState.NONE
    )

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> = _state

    private val _selectedCategory = MutableStateFlow(Category.NONE)

    fun addItem(item: String) {
        when(_selectedCategory.value) {
            Category.NONE -> Unit
            Category.TODO -> todos.add(item)
            Category.ALARM -> alarms.add(item)
            Category.EVENT -> events.add(item)
        }
    }

    fun transitionStarted() {
        transitionState.value = TransitionState.STARTED
    }
    fun transitionFinished() {
        transitionState.value = TransitionState.FINISHED
    }

    fun selectCategory(category: Category) {
        _selectedCategory.value = category
    }

    private fun MutableStateFlow<List<String>>.add(item: String) {
        val newMutableList = value.toMutableList()
        newMutableList.add(item)
        value = newMutableList
    }

    init {
        viewModelScope.launch {
            combine(
                todos,
                alarms,
                events,
                transitionState
            ) { todos, alarms, events, state ->
                ViewState(
                    todos = todos,
                    alarms = alarms,
                    events = events,
                    transitionState = state
                )
            }.collect{
                _state.value = it
            }
        }
    }
}

enum class TransitionState {
    NONE, STARTED, FINISHED
}

enum class Category {
    NONE, TODO, ALARM, EVENT
}
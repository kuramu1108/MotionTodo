package com.pocraft.motiontodo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocraft.motiontodo.model.Item
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {
    private val todos = MutableStateFlow<List<Item>>(emptyList())
    private val alarms = MutableStateFlow<List<Item>>(emptyList())
    private val events = MutableStateFlow<List<Item>>(emptyList())
    private val transitionState = MutableStateFlow(TransitionState.NONE)

    data class ViewState(
        val todos: List<Item> = emptyList(),
        val alarms: List<Item> = emptyList(),
        val events: List<Item> = emptyList(),
        val transitionState: TransitionState = TransitionState.NONE
    )

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> = _state

    private val _selectedCategory = MutableStateFlow(Category.NONE)

    fun addItem(itemTitle: String) {
        when(_selectedCategory.value) {
            Category.NONE -> Unit
            Category.TODO -> todos.add(itemTitle)
            Category.ALARM -> alarms.add(itemTitle)
            Category.EVENT -> events.add(itemTitle)
        }
    }

    fun removeItem(item: Item) {
        when(_selectedCategory.value) {
            Category.NONE -> Unit
            Category.TODO -> todos.remove(item)
            Category.ALARM -> alarms.remove(item)
            Category.EVENT -> events.remove(item)
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

    private fun MutableStateFlow<List<Item>>.add(itemTitle: String) {
        value = value.toMutableList().apply {
            add(Item(title = itemTitle))
        }
    }

    private fun MutableStateFlow<List<Item>>.remove(item: Item) {
        value = value.toMutableList().apply {
            remove(item)
        }
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
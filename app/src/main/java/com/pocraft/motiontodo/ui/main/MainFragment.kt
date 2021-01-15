package com.pocraft.motiontodo.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.pocraft.motiontodo.R
import com.pocraft.motiontodo.databinding.MainFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@ExperimentalCoroutinesApi
class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(_binding!!) {
            mainFragment.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionTrigger(
                    p0: MotionLayout?,
                    p1: Int,
                    p2: Boolean,
                    p3: Float
                ) {
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                    viewModel.transitionStarted()
                }

                override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                }

                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                    viewModel.transitionFinished()
                }
            })

            val todoAdapter = TodoAdapter(onItemRemoveClicked = viewModel::removeItem)
            todoList.adapter = todoAdapter
            val alarmAdapter = TodoAdapter(onItemRemoveClicked = viewModel::removeItem)
            alarmList.adapter = alarmAdapter
            val eventAdapter = TodoAdapter(onItemRemoveClicked = viewModel::removeItem)
            eventList.adapter = eventAdapter

            todoCard.setOnClickListener {
                when (mainFragment.currentState) {
                    R.id.start -> mainFragment.transitionToState(R.id.end)
                    else -> mainFragment.transitionToStart()
                }
            }
            alarmCard.setOnClickListener {
                when (mainFragment.currentState) {
                    R.id.start -> mainFragment.transitionToState(R.id.alarm)
                    else -> mainFragment.transitionToStart()
                }
            }

            eventCard.setOnClickListener {
                when (mainFragment.currentState) {
                    R.id.start -> mainFragment.transitionToState(R.id.event)
                    else -> mainFragment.transitionToStart()
                }
            }

            lifecycleScope.launch {
                viewModel.state.collect { state ->
                    todoNum.text = state.todos.size.toString()
                    alarmNum.text = state.alarms.size.toString()
                    eventNum.text = state.events.size.toString()

                    todoAdapter.submitList(state.todos)
                    alarmAdapter.submitList(state.alarms)
                    eventAdapter.submitList(state.events)
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
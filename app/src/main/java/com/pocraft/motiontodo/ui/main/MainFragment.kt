package com.pocraft.motiontodo.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.pocraft.motiontodo.R
import com.pocraft.motiontodo.databinding.MainFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

            val todoAdapter = TodoAdapter()
            todoList.adapter = todoAdapter
            val alarmAdapter = TodoAdapter()
            alarmList.adapter = alarmAdapter
            val eventAdapter = TodoAdapter()
            eventList.adapter = eventAdapter

            todoCard.setOnClickListener {
                Log.d("POTEST", "${mainFragment.currentState}")
                when (mainFragment.currentState) {
                    R.id.start -> mainFragment.transitionToState(R.id.end)
                    else -> mainFragment.transitionToStart()
                }
            }
            alarmCard.setOnClickListener {
                Log.d("POTEST", "${mainFragment.currentState}")
                when (mainFragment.currentState) {
                    R.id.start -> mainFragment.transitionToState(R.id.alarm)
                    else -> mainFragment.transitionToStart()
                }
            }

            lifecycleScope.launch {
                viewModel.state.collect { state ->
                    todoNum.text = state.todos.size.toString()
                    alarmNum.text = state.alarms.size.toString()
                    eventNum.text = state.events.size.toString()

                    when(state.transitionState) {
                        TransitionState.FINISHED -> {
                            when(mainFragment.currentState) {
                                R.id.start -> {
                                    todoAdapter.update(emptyList())
                                    alarmAdapter.update(emptyList())
                                }
                                R.id.end -> todoAdapter.update(state.todos)
                                R.id.alarm -> alarmAdapter.update(state.alarms)
                            }
                        }
                        else -> {
                            todoAdapter.update(emptyList())
                            alarmAdapter.update(emptyList())
                            eventAdapter.update(emptyList())
                        }
                    }
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
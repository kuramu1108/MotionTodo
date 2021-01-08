package com.pocraft.motiontodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.pocraft.motiontodo.databinding.MainActivityBinding
import com.pocraft.motiontodo.ui.main.Category
import com.pocraft.motiontodo.ui.main.MainFragment
import com.pocraft.motiontodo.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }

        with(binding) {
            fabAdd.setOnClickListener {
                when (mainActivity.currentState) {
                    R.id.start -> mainActivity.transitionToState(R.id.end)
                    else ->  mainActivity.transitionToStart()
                }
            }

            fabAlarm.setOnClickListener{
                viewModel.selectCategory(Category.ALARM)
                showBottomSheet()
            }
            fabCalender.setOnClickListener {
                viewModel.selectCategory(Category.EVENT)
                showBottomSheet()
            }
            fabTodo.setOnClickListener {
                viewModel.selectCategory(Category.TODO)
                showBottomSheet()
            }
        }
    }

    private fun showBottomSheet() = AddItemBottomSheetDialog().show(supportFragmentManager, "BOTTOM_SHEET")
}
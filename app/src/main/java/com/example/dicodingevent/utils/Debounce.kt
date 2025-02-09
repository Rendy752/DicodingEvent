package com.example.dicodingevent.utils

import com.example.dicodingevent.ui.finished.FinishedViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class Debounce(
    private val coroutineScope: CoroutineScope,
    private val delayMillis: Long = 1000L,
    private val viewModel: FinishedViewModel
) {

    private var searchJob: Job? = null

    fun query(text: String) {
        if (text != viewModel.queryState.value.query) {
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                delay(delayMillis)
                viewModel.updateQuery(text)
            }
        }
    }
}
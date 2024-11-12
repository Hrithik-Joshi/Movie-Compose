package com.hrithik.moviecompose

import com.hrithik.moviecompose.viewModel.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


internal val appModule = module {
    viewModelOf(::MovieListViewModel)
}
package com.hrithik.moviecompose

import com.hrithik.moviecompose.model.repository.MovieRepository
import com.hrithik.moviecompose.viewModel.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


internal val appModule = module {
    single { MovieRepository() }
    viewModel { MovieListViewModel(get()) }
}
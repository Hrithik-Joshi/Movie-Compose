package com.hrithik.moviecompose

import com.hrithik.moviecompose.data.repository.MovieRepository
import com.hrithik.moviecompose.viewModel.MovieListViewModel
import com.hrithik.moviecompose.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


internal val appModule = module {
    single { MovieRepository() }
    viewModel { MovieListViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}
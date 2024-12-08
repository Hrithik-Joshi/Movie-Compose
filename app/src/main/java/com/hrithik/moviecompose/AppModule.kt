package com.hrithik.moviecompose

import com.hrithik.moviecompose.data.repository.MovieRepository
import com.hrithik.moviecompose.viewModel.BottomNavigationViewModel
import com.hrithik.moviecompose.viewModel.FavoriteListViewModel
import com.hrithik.moviecompose.viewModel.MovieListViewModel
import com.hrithik.moviecompose.viewModel.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


internal val appModule = module {
    single { MovieRepository(get()) }
    viewModel { MovieListViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { FavoriteListViewModel(get()) }
    viewModel { BottomNavigationViewModel(get()) }
}
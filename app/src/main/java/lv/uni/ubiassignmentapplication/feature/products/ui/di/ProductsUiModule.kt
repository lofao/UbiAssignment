package lv.uni.ubiassignmentapplication.feature.products.ui.di

import kotlinx.coroutines.Dispatchers
import lv.uni.ubiassignmentapplication.UbiApplication
import lv.uni.ubiassignmentapplication.feature.products.ui.product.ProductsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val ProductsUiModule = module {
    viewModel {
        ProductsViewModel(
            application = get<UbiApplication>(),
            dispatcher = Dispatchers.IO,
            useCase = get(),
            networkMonitor = get(),
        )
    }
}

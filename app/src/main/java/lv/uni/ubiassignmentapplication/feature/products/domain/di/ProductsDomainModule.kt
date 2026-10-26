package lv.uni.ubiassignmentapplication.feature.products.domain.di

import lv.uni.ubiassignmentapplication.feature.products.domain.usecase.GetProductsUseCase
import org.koin.dsl.module

val ProductsDomainModule = module {
    single<GetProductsUseCase> {
        return@single GetProductsUseCase(repository = get())
    }
}
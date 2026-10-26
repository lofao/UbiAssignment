package lv.uni.ubiassignmentapplication.feature.products.data.di

import lv.uni.ubiassignmentapplication.feature.products.data.datasource.ProductDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.datasource.ProductLocalDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.datasource.RemoteProductDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.datasource.RoomProductDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.repository.ProductsRepository
import lv.uni.ubiassignmentapplication.feature.products.data.repository.UbiApiRepository
import org.koin.dsl.module

val ProductsDataModule = module {

    single<ProductDataSource> {
        RemoteProductDataSource(httpClient = get())
    }

    single<ProductLocalDataSource> {
        RoomProductDataSource(productDao = get())
    }

    single<ProductsRepository> {
        UbiApiRepository(
            remoteDataSource = get(),
            localDataSource = get(),
        )
    }

}

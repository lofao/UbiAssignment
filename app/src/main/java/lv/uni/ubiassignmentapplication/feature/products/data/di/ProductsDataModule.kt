package lv.uni.ubiassignmentapplication.feature.products.data.di

import lv.uni.ubiassignmentapplication.feature.products.data.datasource.ProductDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.datasource.RemoteProductDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.repository.ProductsRepository
import lv.uni.ubiassignmentapplication.feature.products.data.repository.UbiApiRepository
import org.koin.dsl.module

val ProductsDataModule = module {

    single<ProductDataSource>  {
        return@single RemoteProductDataSource(httpClient = get())
    }

    single<ProductsRepository> {
        return@single UbiApiRepository(dataSource = get())
    }

}
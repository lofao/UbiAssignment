package lv.uni.ubiassignmentapplication.feature.products.data.repository

import lv.uni.ubiassignmentapplication.feature.products.data.datasource.ProductDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.model.ProductsResponseDto

class UbiApiRepository(private val dataSource: ProductDataSource) : ProductsRepository {
    override suspend fun getProducts(): ProductsResponseDto {
        return dataSource.getProducts()
    }
}
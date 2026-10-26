package lv.uni.ubiassignmentapplication.feature.products.data.datasource

import lv.uni.ubiassignmentapplication.feature.products.data.model.ProductsResponseDto

interface ProductDataSource {
    suspend fun getProducts(): ProductsResponseDto
}
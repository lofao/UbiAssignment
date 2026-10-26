package lv.uni.ubiassignmentapplication.feature.products.data.repository

import lv.uni.ubiassignmentapplication.feature.products.data.model.ProductsResponseDto

interface ProductsRepository {
    suspend fun getProducts(): ProductsResponseDto
}
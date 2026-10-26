package lv.uni.ubiassignmentapplication.feature.products.data.repository

import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product

data class GetProductsResult(
    val products: List<Product>,
    val isOffline: Boolean,
)

interface ProductsRepository {
    suspend fun getProducts(): GetProductsResult
}
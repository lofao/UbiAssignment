package lv.uni.ubiassignmentapplication.feature.products.domain.usecase

import lv.uni.ubiassignmentapplication.feature.products.data.repository.GetProductsResult
import lv.uni.ubiassignmentapplication.feature.products.data.repository.ProductsRepository

class GetProductsUseCase(
    private val repository: ProductsRepository,
) {
    suspend operator fun invoke(): GetProductsResult = repository.getProducts()
}
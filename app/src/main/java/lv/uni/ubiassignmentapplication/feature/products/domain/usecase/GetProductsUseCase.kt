package lv.uni.ubiassignmentapplication.feature.products.domain.usecase

import lv.uni.ubiassignmentapplication.feature.products.data.repository.ProductsRepository
import lv.uni.ubiassignmentapplication.feature.products.domain.mapper.toProducts
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product

class GetProductsUseCase(
    private val repository: ProductsRepository,
) {
    suspend operator fun invoke(): List<Product> = repository.getProducts().toProducts()
}
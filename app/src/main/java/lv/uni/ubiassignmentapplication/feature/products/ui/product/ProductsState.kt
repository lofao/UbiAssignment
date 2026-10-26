package lv.uni.ubiassignmentapplication.feature.products.ui.product

import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product

data class ProductsState(
    val isLoading: Boolean = true,
    val errorText: String = "",
    val products: List<Product> = emptyList(),
    val isOffline: Boolean = false,
)
package lv.uni.ubiassignmentapplication.feature.products.ui.product

import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product

data class ProductListUiState(
    val isLoading: Boolean = true,
    val errorText: String = "",
    val products: List<Product> = emptyList(),
    val isOffline: Boolean = false,
    val searchQuery: String = "",
    val selectedTab: ProductTab = ProductTab.ALL,
)

data class ProductDetailsUiState(
    val product: Product? = null,
    val isRefreshing: Boolean = false,
    val isOffline: Boolean = false,
)

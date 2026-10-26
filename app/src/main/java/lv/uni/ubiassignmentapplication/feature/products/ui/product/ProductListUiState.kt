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

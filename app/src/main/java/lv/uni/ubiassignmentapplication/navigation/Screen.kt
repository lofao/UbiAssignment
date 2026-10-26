package lv.uni.ubiassignmentapplication.navigation

import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product

sealed interface Screen {
    data object ProductList : Screen
    data class ProductDetails(val product: Product) : Screen
}

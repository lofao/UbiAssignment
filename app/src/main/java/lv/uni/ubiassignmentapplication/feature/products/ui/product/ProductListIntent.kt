package lv.uni.ubiassignmentapplication.feature.products.ui.product

import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product

sealed interface ProductListIntent {
    data object LoadProducts : ProductListIntent
    data object ErrorShown : ProductListIntent
    data class SearchQueryChanged(val query: String) : ProductListIntent
    data class TabSelected(val tab: ProductTab) : ProductListIntent
    data class ProductClicked(val product: Product) : ProductListIntent
}

package lv.uni.ubiassignmentapplication.feature.products.ui.product

import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.domain.model.isCamera
import lv.uni.ubiassignmentapplication.feature.products.domain.model.isIot

enum class ProductTab(val title: String) {
    ALL("All"),
    CAMERAS("Cameras"),
    IOT("IoT");

    fun matches(product: Product): Boolean = when (this) {
        ALL -> true
        CAMERAS -> product.isCamera
        IOT -> product.isIot
    }
}
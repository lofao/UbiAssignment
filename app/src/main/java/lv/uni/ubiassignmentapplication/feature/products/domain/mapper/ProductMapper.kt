package lv.uni.ubiassignmentapplication.feature.products.domain.mapper

import lv.uni.ubiassignmentapplication.feature.products.data.model.ProductsResponseDto
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product


internal fun ProductsResponseDto.toProducts(): List<Product> =
    devices?.mapNotNull { it?.toProduct() }.orEmpty()

internal fun ProductsResponseDto.ProductDto?.toProduct(): Product? {
    this ?: return null
    val productId = id ?: return null
    val productName = product?.name ?: return null


    return Product(
        deviceType = deviceType,
        id = productId,
        productName = productName,
        imageId = this.images?.default,
        lineId = this.line?.id,
        lineName = this.line?.name,
        shortNames = this.shortNames ?: emptyList(),
    )
}

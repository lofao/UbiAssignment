package lv.uni.ubiassignmentapplication.feature.products.data.local.mapper

import lv.uni.ubiassignmentapplication.feature.products.data.local.entity.ProductEntity
import lv.uni.ubiassignmentapplication.feature.products.data.model.ProductsResponseDto
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product

internal fun ProductsResponseDto.toEntities(): List<ProductEntity> =
    devices?.mapNotNull { it?.toEntity() }.orEmpty()

internal fun ProductsResponseDto.ProductDto.toEntity(): ProductEntity? {
    val productId = id ?: return null
    val name = product?.name ?: return null
    return ProductEntity(
        id = productId,
        productName = name,
        deviceType = deviceType,
        imageId = images?.default,
        lineId = line?.id,
        lineName = line?.name,
        shortNames = shortNames?.joinToString(",") ?: "",
    )
}

internal fun ProductEntity.toProduct(): Product {
    return Product(
        id = id,
        productName = productName,
        deviceType = deviceType,
        imageId = imageId,
        lineId = lineId,
        lineName = lineName,
        shortNames = if (shortNames.isBlank()) emptyList() else shortNames.split(","),
    )
}

internal fun List<ProductEntity>.toProducts(): List<Product> = map { it.toProduct() }

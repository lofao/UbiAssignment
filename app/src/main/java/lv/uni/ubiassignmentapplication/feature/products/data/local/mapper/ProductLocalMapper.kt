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
        sku = sku,
        abbrev = product.abbrev,
        modelName = compliance?.modelName,
        fcc = compliance?.fcc,
        ic = compliance?.ic,
        numberOfPorts = unifi?.network?.numberOfPorts,
        ethernetMaxSpeedMbps = unifi?.network?.ethernetMaxSpeedMegabitsPerSecond,
        formFactors = unifi?.network?.formFactor?.joinToString(","),
        wifiStandard = wifi,
        isARSupported = isARSupported,
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
        sku = sku,
        abbrev = abbrev,
        modelName = modelName,
        fcc = fcc,
        ic = ic,
        numberOfPorts = numberOfPorts,
        ethernetMaxSpeedMbps = ethernetMaxSpeedMbps,
        formFactors = if (formFactors.isNullOrBlank()) emptyList() else formFactors.split(","),
        wifiStandard = wifiStandard,
        isARSupported = isARSupported ?: false,
    )
}

internal fun List<ProductEntity>.toProducts(): List<Product> = map { it.toProduct() }

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
        id = productId,
        productName = productName,
        deviceType = deviceType,
        imageId = images?.default,
        lineId = line?.id,
        lineName = line?.name,
        shortNames = shortNames ?: emptyList(),
        sku = sku,
        abbrev = product.abbrev,
        modelName = compliance?.modelName,
        fcc = compliance?.fcc,
        ic = compliance?.ic,
        numberOfPorts = unifi?.network?.numberOfPorts,
        ethernetMaxSpeedMbps = unifi?.network?.ethernetMaxSpeedMegabitsPerSecond,
        formFactors = unifi?.network?.formFactor ?: emptyList(),
        wifiStandard = wifi,
        isARSupported = isARSupported ?: false,
    )
}

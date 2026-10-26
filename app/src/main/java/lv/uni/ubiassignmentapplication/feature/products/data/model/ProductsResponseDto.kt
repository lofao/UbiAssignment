package lv.uni.ubiassignmentapplication.feature.products.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponseDto(val devices: List<ProductDto>? = null) {

    @Serializable
    data class ProductDto(
        @SerialName("deviceType") val deviceType: String? = null,
        @SerialName("id") val id: String? = null,
        @SerialName("product") val product: ProductNameDto? = null,
        @SerialName("images") val images: ProductImagesDto? = null,
        @SerialName("line") val line: ProductLineDto? = null,
        @SerialName("shortnames") val shortNames: List<String>? = null,
        @SerialName("sku") val sku: String? = null,
        @SerialName("sysid") val sysid: String? = null,
        @SerialName("compliance") val compliance: ComplianceDto? = null,
        @SerialName("unifi") val unifi: UnifiDto? = null,
        @SerialName("wifi") val wifi: String? = null,
        @SerialName("isARSupported") val isARSupported: Boolean? = null,
    )

    @Serializable
    data class ProductLineDto(
        @SerialName("id") val id: String? = null,
        @SerialName("name") val name: String? = null,
    )

    @Serializable
    data class ProductNameDto(
        @SerialName("abbrev") val abbrev: String? = null,
        @SerialName("name") val name: String? = null,
    )

    @Serializable
    data class ProductImagesDto(
        @SerialName("default") val default: String? = null,
        @SerialName("nopadding") val noPadding: String? = null,
        @SerialName("topology") val topology: String? = null,
    )

    @Serializable
    data class ComplianceDto(
        @SerialName("modelName") val modelName: String? = null,
        @SerialName("fcc") val fcc: String? = null,
        @SerialName("ic") val ic: String? = null,
        @SerialName("rcm") val rcm: Boolean? = null,
    )

    @Serializable
    data class UnifiDto(
        @SerialName("network") val network: UnifiNetworkDto? = null,
    )

    @Serializable
    data class UnifiNetworkDto(
        @SerialName("numberOfPorts") val numberOfPorts: Int? = null,
        @SerialName("ethernetMaxSpeedMegabitsPerSecond") val ethernetMaxSpeedMegabitsPerSecond: Int? = null,
        @SerialName("formFactor") val formFactor: List<String>? = null,
    )

}

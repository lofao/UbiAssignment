package lv.uni.ubiassignmentapplication.feature.products.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponseDto(val devices: List<ProductDto>? = null) {

    @Serializable
    data class ProductDto(
        @SerialName("deviceType") val deviceType: String? = null,
        @SerialName("id") val id: String? = null,
        @SerialName("product")  val product: ProductNameDto? = null,
        @SerialName("images") val images: ProductImagesDto? = null,
        @SerialName("shortnames") val shortNames: List<String>? = null,
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

}
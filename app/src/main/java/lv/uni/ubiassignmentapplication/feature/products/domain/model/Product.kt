package lv.uni.ubiassignmentapplication.feature.products.domain.model


data class Product(
    val id: String,
    val productName: String,
    val deviceType: String?,
    val imageId: String?,
    val shortNames: List<String> = emptyList(),
)
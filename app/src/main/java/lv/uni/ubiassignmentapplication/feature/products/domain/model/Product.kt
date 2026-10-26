package lv.uni.ubiassignmentapplication.feature.products.domain.model


data class Product(
    val id: String,
    val productName: String,
    val deviceType: String?,
    val imageId: String?,
    val shortNames: List<String> = emptyList(),
)

fun Product.imageUrl(size: Int = 256): String? {
    val imgId = imageId ?: return null
    return "https://images.svc.ui.com/?u=https%3A%2F%2Fstatic.ui.com%2Ffingerprint%2Fui%2Fimages%2F${id}%2Fdefault%2F${imgId}.png&w=${size}&q=75"
}
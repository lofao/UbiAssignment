package lv.uni.ubiassignmentapplication.feature.products.domain.model

data class Product(
    val id: String,
    val productName: String,
    val deviceType: String?,
    val imageId: String?,
    val lineId: String? = null,
    val lineName: String? = null,
    val shortNames: List<String> = emptyList(),
)

fun Product.imageUrl(size: Int = 256): String? {
    val imgId = imageId ?: return null
    return "https://images.svc.ui.com/?u=https%3A%2F%2Fstatic.ui.com%2Ffingerprint%2Fui%2Fimages%2F$id%2Fdefault%2F$imgId.png&w=$size&q=75"
}

val Product.isCamera: Boolean
    get() {
        val dt = deviceType?.lowercase().orEmpty()
        val name = productName.lowercase()
        return dt.contains("camera") || name.contains("camera")
    }

val Product.isIot: Boolean
    get() {
        val dt = deviceType?.lowercase().orEmpty()
        val line = lineId?.lowercase().orEmpty()
        val name = productName.lowercase()

        val iotDeviceTypes = setOf(
            "sensor", "light", "doorlock", "alarm", "chime",
            "intercom", "card-reader", "access-hub", "power-supply",
        )
        val iotLines = setOf(
            "mfi", "unifi-led", "unifi-connect", "unifi-access",
        )
        val iotKeywords = listOf(
            "sensor", "plug", "outlet", "dimmer", "chime",
            "siren", "lock", "floodlight", "intercom", "smart",
            "superlink", "keyfob",
        )

        return (dt in iotDeviceTypes) ||
                (line in iotLines) ||
                iotKeywords.any { keyword -> name.contains(keyword) || dt.contains(keyword) }
    }

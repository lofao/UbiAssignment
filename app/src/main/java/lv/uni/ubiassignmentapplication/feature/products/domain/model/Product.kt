package lv.uni.ubiassignmentapplication.feature.products.domain.model

import android.content.Context

data class Product(
    val id: String,
    val productName: String,
    val deviceType: String?,
    val imageId: String?,
    val lineId: String? = null,
    val lineName: String? = null,
    val shortNames: List<String> = emptyList(),
    val sku: String? = null,
    val abbrev: String? = null,
    val modelName: String? = null,
    val fcc: String? = null,
    val ic: String? = null,
    val numberOfPorts: Int? = null,
    val ethernetMaxSpeedMbps: Int? = null,
    val formFactors: List<String> = emptyList(),
    val wifiStandard: String? = null,
    val isARSupported: Boolean = false,
)

fun Product.assetRelativePath(): String? {
    val imgId = imageId ?: return null
    return "$id/$imgId.png"
}

fun Product.assetRelativePaths(): List<String> {
    val imgId = imageId ?: return emptyList()
    return listOf(
        "$id/$imgId.png",
        "$id/$imgId",
        "$id/default/$imgId.png",
    )
}

fun Product.imageUrl(size: Int = 256): String? {
    val imgId = imageId ?: return null
    return "https://images.svc.ui.com/?u=https%3A%2F%2Fstatic.ui.com%2Ffingerprint%2Fui%2Fimages%2F$id%2Fdefault%2F$imgId.png&w=$size&q=75"
}

fun Product.imageUrl(context: Context, size: Int = 256): String? {
    if (imageId == null) return null
    val relativePaths = assetRelativePaths()

    // 1. Check preloaded APK assets (e.g. assets/product-id/imageId.png)
    for (relPath in relativePaths) {
        try {
            context.assets?.open(relPath)?.use {
                return "file:///android_asset/$relPath"
            }
        } catch (_: Exception) {
            // Asset not present in APK assets
        }
    }

    // 2. Fallback to remote URL
    return imageUrl(size)
}

val Product.formattedEthernetSpeed: String?
    get() {
        val mbps = ethernetMaxSpeedMbps ?: return null
        return when {
            mbps >= 100000 -> "${mbps / 1000} Gbps"
            mbps >= 1000 -> "${mbps / 1000} Gbps"
            else -> "$mbps Mbps"
        }
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

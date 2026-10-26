package lv.uni.ubiassignmentapplication.feature.products.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val productName: String,
    val deviceType: String?,
    val imageId: String?,
    val lineId: String?,
    val lineName: String?,
    val shortNames: String,
    val sku: String?,
    val abbrev: String?,
    val modelName: String?,
    val fcc: String?,
    val ic: String?,
    val numberOfPorts: Int?,
    val ethernetMaxSpeedMbps: Int?,
    val formFactors: String?,
    val wifiStandard: String?,
    val isARSupported: Boolean?,
)

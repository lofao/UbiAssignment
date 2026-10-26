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
)

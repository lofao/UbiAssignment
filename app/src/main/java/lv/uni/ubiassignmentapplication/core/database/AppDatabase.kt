package lv.uni.ubiassignmentapplication.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import lv.uni.ubiassignmentapplication.feature.products.data.local.dao.ProductDao
import lv.uni.ubiassignmentapplication.feature.products.data.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}

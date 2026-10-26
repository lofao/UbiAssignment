package lv.uni.ubiassignmentapplication.core.database.di

import androidx.room.Room
import lv.uni.ubiassignmentapplication.core.database.AppDatabase
import lv.uni.ubiassignmentapplication.feature.products.data.local.dao.ProductDao
import org.koin.dsl.module

val DatabaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = "ubi_assignment.db",
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    single<ProductDao> {
        get<AppDatabase>().productDao()
    }
}

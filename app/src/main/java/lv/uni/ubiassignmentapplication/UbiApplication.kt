package lv.uni.ubiassignmentapplication

import android.app.Application
import lv.uni.ubiassignmentapplication.core.database.di.DatabaseModule
import lv.uni.ubiassignmentapplication.core.network.NetworkModule
import lv.uni.ubiassignmentapplication.di.appModule
import lv.uni.ubiassignmentapplication.feature.products.data.di.ProductsDataModule
import lv.uni.ubiassignmentapplication.feature.products.domain.di.ProductsDomainModule
import lv.uni.ubiassignmentapplication.feature.products.ui.di.ProductsUiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class UbiApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@UbiApplication)
            modules(
                appModule(this@UbiApplication),
                DatabaseModule,
                NetworkModule,
                ProductsDataModule,
                ProductsDomainModule,
                ProductsUiModule,
            )
        }
    }
}

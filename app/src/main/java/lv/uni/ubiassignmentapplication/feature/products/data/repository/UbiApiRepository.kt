package lv.uni.ubiassignmentapplication.feature.products.data.repository

import lv.uni.ubiassignmentapplication.feature.products.data.datasource.ProductDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.datasource.ProductLocalDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.local.mapper.toEntities
import lv.uni.ubiassignmentapplication.feature.products.data.local.mapper.toProducts
import lv.uni.ubiassignmentapplication.feature.products.domain.mapper.toProducts
import timber.log.Timber

class UbiApiRepository(
    private val remoteDataSource: ProductDataSource,
    private val localDataSource: ProductLocalDataSource,
) : ProductsRepository {

    override suspend fun getProducts(): GetProductsResult {
        return try {
            val response = remoteDataSource.getProducts()
            val entities = response.toEntities()
            if (entities.isNotEmpty()) {
                localDataSource.saveProducts(entities)
            }
            GetProductsResult(
                products = response.toProducts(),
                isOffline = false,
            )
        } catch (e: Exception) {
            Timber.w(e, "Failed to fetch products from network, falling back to local database cache.")
            val cachedEntities = localDataSource.getProducts()
            if (cachedEntities.isNotEmpty()) {
                GetProductsResult(
                    products = cachedEntities.toProducts(),
                    isOffline = true,
                )
            } else {
                throw e
            }
        }
    }
}

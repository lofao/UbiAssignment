package lv.uni.ubiassignmentapplication.feature.products.data.datasource

import lv.uni.ubiassignmentapplication.feature.products.data.local.dao.ProductDao
import lv.uni.ubiassignmentapplication.feature.products.data.local.entity.ProductEntity

interface ProductLocalDataSource {
    suspend fun getProducts(): List<ProductEntity>
    suspend fun saveProducts(products: List<ProductEntity>)
}

class RoomProductDataSource(
    private val productDao: ProductDao,
) : ProductLocalDataSource {
    override suspend fun getProducts(): List<ProductEntity> {
        return productDao.getAllProducts()
    }

    override suspend fun saveProducts(products: List<ProductEntity>) {
        if (products.isNotEmpty()) {
            productDao.clearAllProducts()
            productDao.insertProducts(products)
        }
    }
}

package lv.uni.ubiassignmentapplication.feature.products.data.repository

import lv.uni.ubiassignmentapplication.feature.products.data.datasource.ProductDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.datasource.ProductLocalDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.local.mapper.toEntities
import lv.uni.ubiassignmentapplication.feature.products.data.model.ProductsResponseDto
import timber.log.Timber

class UbiApiRepository(
    private val remoteDataSource: ProductDataSource,
    private val localDataSource: ProductLocalDataSource,
) : ProductsRepository {

    override suspend fun getProducts(): ProductsResponseDto {
        return try {
            val response = remoteDataSource.getProducts()
            val entities = response.toEntities()
            if (entities.isNotEmpty()) {
                localDataSource.saveProducts(entities)
            }
            response
        } catch (e: Exception) {
            Timber.w(e, "Failed to fetch products from network, falling back to local database cache.")
            val cachedEntities = localDataSource.getProducts()
            if (cachedEntities.isNotEmpty()) {
                ProductsResponseDto(
                    devices = cachedEntities.map { entity ->
                        ProductsResponseDto.ProductDto(
                            id = entity.id,
                            deviceType = entity.deviceType,
                            product = ProductsResponseDto.ProductNameDto(name = entity.productName),
                            images = ProductsResponseDto.ProductImagesDto(default = entity.imageId),
                            line = ProductsResponseDto.ProductLineDto(id = entity.lineId, name = entity.lineName),
                            shortNames = if (entity.shortNames.isBlank()) emptyList() else entity.shortNames.split(","),
                        )
                    },
                )
            } else {
                throw e
            }
        }
    }
}

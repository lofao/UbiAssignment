package lv.uni.ubiassignmentapplication.feature.products.data.repository

import kotlinx.coroutines.runBlocking
import lv.uni.ubiassignmentapplication.feature.products.data.datasource.ProductDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.datasource.ProductLocalDataSource
import lv.uni.ubiassignmentapplication.feature.products.data.local.entity.ProductEntity
import lv.uni.ubiassignmentapplication.feature.products.data.model.ProductsResponseDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class UbiApiRepositoryTest {

    private val fakeRemoteDataSource = FakeRemoteDataSource()
    private val fakeLocalDataSource = FakeLocalDataSource()
    private val repository = UbiApiRepository(fakeRemoteDataSource, fakeLocalDataSource)

    @Test
    fun `getProducts returns isOffline false when remote succeeds`() = runBlocking {
        fakeRemoteDataSource.shouldFail = false
        fakeRemoteDataSource.response = ProductsResponseDto(
            devices = listOf(
                ProductsResponseDto.ProductDto(
                    id = "1",
                    product = ProductsResponseDto.ProductNameDto(name = "Test Product"),
                ),
            ),
        )

        val result = repository.getProducts()

        assertFalse(result.isOffline)
        assertEquals(1, result.products.size)
        assertEquals("Test Product", result.products[0].productName)
    }

    @Test
    fun `getProducts returns isOffline true when remote fails and local cache exists`() = runBlocking {
        fakeRemoteDataSource.shouldFail = true
        fakeLocalDataSource.cachedEntities = listOf(
            ProductEntity(
                id = "1",
                productName = "Cached Product",
                deviceType = "camera",
                imageId = "img123",
                lineId = "line1",
                lineName = "Line 1",
                shortNames = "CP",
            ),
        )

        val result = repository.getProducts()

        assertTrue(result.isOffline)
        assertEquals(1, result.products.size)
        assertEquals("Cached Product", result.products[0].productName)
    }

    @Test(expected = IOException::class)
    fun `getProducts throws exception when remote fails and local cache is empty`(): Unit = runBlocking {
        fakeRemoteDataSource.shouldFail = true
        fakeLocalDataSource.cachedEntities = emptyList()

        repository.getProducts()
    }
}

private class FakeRemoteDataSource : ProductDataSource {
    var shouldFail = false
    var response = ProductsResponseDto(devices = emptyList())

    override suspend fun getProducts(): ProductsResponseDto {
        if (shouldFail) {
            throw IOException("Network error")
        }
        return response
    }
}

private class FakeLocalDataSource : ProductLocalDataSource {
    var cachedEntities = listOf<ProductEntity>()

    override suspend fun getProducts(): List<ProductEntity> {
        return cachedEntities
    }

    override suspend fun saveProducts(products: List<ProductEntity>) {
        cachedEntities = products
    }
}

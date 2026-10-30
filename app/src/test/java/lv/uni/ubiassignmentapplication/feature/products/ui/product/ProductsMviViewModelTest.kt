package lv.uni.ubiassignmentapplication.feature.products.ui.product

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import lv.uni.ubiassignmentapplication.UbiApplication
import lv.uni.ubiassignmentapplication.feature.products.data.repository.GetProductsResult
import lv.uni.ubiassignmentapplication.feature.products.data.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import lv.uni.ubiassignmentapplication.core.network.NetworkMonitor
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.domain.usecase.GetProductsUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsMviViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `processIntent SearchQueryChanged updates state query`() = runTest {
        val application = UbiApplication()
        val fakeRepo = object : ProductsRepository {
            override suspend fun getProducts(): GetProductsResult {
                return GetProductsResult(
                    products = listOf(
                        Product(id = "1", productName = "Camera", deviceType = "camera", imageId = "img"),
                    ),
                    isOffline = false,
                )
            }
        }
        val useCase = GetProductsUseCase(fakeRepo)

        val viewModel = ProductsViewModel(
            application = application,
            dispatcher = testDispatcher,
            useCase = useCase,
            networkMonitor = FakeNetworkMonitor(true),
        )

        viewModel.processIntent(ProductListIntent.SearchQueryChanged("camera"))

        assertEquals("camera", viewModel.state.value.searchQuery)
    }

    @Test
    fun `processIntent TabSelected updates state selectedTab`() = runTest {
        val application = UbiApplication()
        val fakeRepo = object : ProductsRepository {
            override suspend fun getProducts(): GetProductsResult {
                return GetProductsResult(
                    products = emptyList(),
                    isOffline = false,
                )
            }
        }
        val useCase = GetProductsUseCase(fakeRepo)

        val viewModel = ProductsViewModel(
            application = application,
            dispatcher = testDispatcher,
            useCase = useCase,
            networkMonitor = FakeNetworkMonitor(true),
        )

        viewModel.processIntent(ProductListIntent.TabSelected(ProductTab.CAMERAS))

        assertEquals(ProductTab.CAMERAS, viewModel.state.value.selectedTab)
    }

    @Test
    fun `networkMonitor emitting false updates state isOffline to true dynamically`() = runTest {
        val application = UbiApplication()
        val fakeRepo = object : ProductsRepository {
            override suspend fun getProducts(): GetProductsResult {
                return GetProductsResult(products = emptyList(), isOffline = false)
            }
        }
        val useCase = GetProductsUseCase(fakeRepo)
        val fakeNetworkMonitor = FakeNetworkMonitor(true)

        val viewModel = ProductsViewModel(
            application = application,
            dispatcher = testDispatcher,
            useCase = useCase,
            networkMonitor = fakeNetworkMonitor,
        )

        assertFalse(viewModel.state.value.isOffline)

        fakeNetworkMonitor.isOnlineFlow.value = false

        assertTrue(viewModel.state.value.isOffline)

        fakeNetworkMonitor.isOnlineFlow.value = true

        assertFalse(viewModel.state.value.isOffline)
    }
}

private class FakeNetworkMonitor(
    initialIsOnline: Boolean = true,
) : NetworkMonitor {
    val isOnlineFlow = MutableStateFlow(initialIsOnline)
    override val isOnline: Flow<Boolean> = isOnlineFlow
    override var isCurrentlyOnline: Boolean = initialIsOnline
}

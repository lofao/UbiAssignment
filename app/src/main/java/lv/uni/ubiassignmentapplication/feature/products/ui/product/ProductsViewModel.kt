package lv.uni.ubiassignmentapplication.feature.products.ui.product

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lv.uni.ubiassignmentapplication.UbiApplication
import lv.uni.ubiassignmentapplication.core.coroutines.runAsync
import lv.uni.ubiassignmentapplication.core.network.ConnectivityNetworkMonitor
import lv.uni.ubiassignmentapplication.core.network.NetworkMonitor
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.domain.usecase.GetProductsUseCase

class ProductsViewModel(
    private val application: UbiApplication,
    private val dispatcher: CoroutineDispatcher,
    private val useCase: GetProductsUseCase,
    private val networkMonitor: NetworkMonitor = ConnectivityNetworkMonitor(application),
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(ProductListUiState())
    private val _detailsState = MutableStateFlow(ProductDetailsUiState())
    val state = _state.asStateFlow()
    val detailsState = _detailsState.asStateFlow()

    private val userIntents = MutableSharedFlow<ProductListIntent>()

    init {
        viewModelScope.launch {
            userIntents.collect { intent ->
                handleIntent(intent)
            }
        }
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                _state.update { currentState ->
                    currentState.copy(isOffline = !isOnline)
                }
                _detailsState.update {
                    it.copy(isOffline = !isOnline)
                }
            }
        }
        processIntent(ProductListIntent.LoadProducts)
    }

    fun processIntent(intent: ProductListIntent) {
        viewModelScope.launch {
            userIntents.emit(intent)
        }
    }

    private suspend fun handleIntent(intent: ProductListIntent) {
        when (intent) {
            ProductListIntent.LoadProducts -> loadProducts()
            ProductListIntent.ErrorShown -> clearError()
            is ProductListIntent.SearchQueryChanged -> updateSearchQuery(intent.query)
            is ProductListIntent.TabSelected -> updateTab(intent.tab)
            is ProductListIntent.ProductClicked -> { /* Handle product selection if needed */ }
        }
    }

    private suspend fun loadProducts() {
        val offlineCurrently = try {
            !networkMonitor.isCurrentlyOnline
        } catch (_: Exception) {
            false
        }
        _state.update { it.copy(isLoading = true, errorText = "", isOffline = offlineCurrently) }
        _detailsState.update { it.copy(isRefreshing = true, isOffline = offlineCurrently) }
        val productsResult = runAsync(dispatcher) {
            useCase()
        }
        if (productsResult.isFailure) {
            val errorMessage = productsResult.getError().localizedMessage ?: "Failed to load products"
            _state.update { it.copy(isLoading = false, errorText = errorMessage, isOffline = true) }
            _detailsState.update { it.copy(isRefreshing = false, isOffline = offlineCurrently) }

            return
        }
        val result = productsResult.getResponse()
        _detailsState.update { it.copy(isRefreshing = false, isOffline = result.isOffline ||  offlineCurrently) }
        _state.update {
            it.copy(
                isLoading = false,
                products = result.products,
                isOffline = result.isOffline || offlineCurrently,
                errorText = "",
            )
        }
    }

    private fun clearError() {
        _state.update { it.copy(errorText = "") }
    }

    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun updateTab(tab: ProductTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun updateProduct(product: Product) {
        _detailsState.update {
            it.copy(product = product)
        }
    }
}

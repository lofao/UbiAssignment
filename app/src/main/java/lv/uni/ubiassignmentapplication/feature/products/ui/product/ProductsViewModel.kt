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
import lv.uni.ubiassignmentapplication.core.network.isNetworkAvailable
import lv.uni.ubiassignmentapplication.feature.products.domain.usecase.GetProductsUseCase

class ProductsViewModel(
    private val application: UbiApplication,
    private val dispatcher: CoroutineDispatcher,
    private val useCase: GetProductsUseCase,
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(ProductListUiState())
    val state = _state.asStateFlow()

    private val userIntents = MutableSharedFlow<ProductListIntent>()

    init {
        viewModelScope.launch {
            userIntents.collect { intent ->
                handleIntent(intent)
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
            !application.isNetworkAvailable()
        } catch (_: Exception) {
            false
        }
        _state.update { it.copy(isLoading = true, errorText = "", isOffline = offlineCurrently) }
        val productsResult = runAsync(dispatcher) {
            useCase()
        }
        if (productsResult.isFailure) {
            val errorMessage = productsResult.getError().localizedMessage ?: "Failed to load products"
            _state.update { it.copy(isLoading = false, errorText = errorMessage, isOffline = true) }
            return
        }
        val result = productsResult.getResponse()
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
}

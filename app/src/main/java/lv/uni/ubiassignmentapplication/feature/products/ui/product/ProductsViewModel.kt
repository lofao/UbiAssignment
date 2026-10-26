package lv.uni.ubiassignmentapplication.feature.products.ui.product

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
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

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            val offlineCurrently = !application.isNetworkAvailable()
            _state.update { it.copy(isLoading = true, errorText = "", isOffline = offlineCurrently) }
            val productsResult = runAsync(dispatcher) {
                useCase()
            }
            if (productsResult.isFailure) {
                val errorMessage = productsResult.getError().localizedMessage ?: "Failed to load products"
                _state.update { it.copy(isLoading = false, errorText = errorMessage, isOffline = true) }
                return@launch
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
    }


    fun onErrorShown() {
        _state.update { it.copy(errorText = "") }
    }
}

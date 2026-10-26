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
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.domain.usecase.GetProductsUseCase

data class ProductsState(
    val isLoading: Boolean = true,
    val errorText: String = "",
    val products: List<Product> = emptyList()
)

class ProductsViewModel(
    private val application: UbiApplication,
    private val dispatcher: CoroutineDispatcher,
    private val useCase: GetProductsUseCase
): AndroidViewModel(application) {

    private val _state = MutableStateFlow(ProductsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val productsResult = runAsync(dispatcher) {
                useCase()
            }
            if (productsResult.isFailure) {
                _state.update { it.copy(isLoading = false, errorText = "Error") }
                // Handle error
                return@launch
            }
            val products = productsResult.getResponse()
            _state.update { it.copy(isLoading = false, products = products) }
        }
    }

    override fun onCleared() {
        super.onCleared()

    }



}
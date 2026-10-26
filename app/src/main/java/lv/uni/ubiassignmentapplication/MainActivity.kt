package lv.uni.ubiassignmentapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lv.uni.ubiassignmentapplication.core.ui.UbiAssignmentApplicationTheme
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.ui.details.ProductDetailsScreen
import lv.uni.ubiassignmentapplication.feature.products.ui.product.ProductListRoute
import lv.uni.ubiassignmentapplication.feature.products.ui.product.ProductsViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UbiAssignmentApplicationTheme {
                val viewModel: ProductsViewModel = koinViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                var selectedProduct by remember { mutableStateOf<Product?>(null) }

                Box(modifier = Modifier.fillMaxSize()) {
                    ProductListRoute(
                        onProductClick = { product ->
                            selectedProduct = product
                        },
                        viewModel = viewModel,
                    )

                    val currentProduct = selectedProduct
                    if (currentProduct != null) {
                        BackHandler {
                            selectedProduct = null
                        }
                        ProductDetailsScreen(
                            product = currentProduct,
                            onBackClick = { selectedProduct = null },
                            isRefreshing = state.isLoading,
                            onRefresh = { viewModel.loadProducts() },
                        )
                    }
                }
            }
        }
    }
}

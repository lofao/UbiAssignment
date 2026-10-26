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
import lv.uni.ubiassignmentapplication.core.ui.UbiAssignmentApplicationTheme
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.ui.details.ProductDetailsScreen
import lv.uni.ubiassignmentapplication.feature.products.ui.product.ProductsRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UbiAssignmentApplicationTheme {
                var selectedProduct by remember { mutableStateOf<Product?>(null) }

                Box(modifier = Modifier.fillMaxSize()) {
                    ProductsRoute(
                        onProductClick = { product ->
                            selectedProduct = product
                        },
                    )

                    val currentProduct = selectedProduct
                    if (currentProduct != null) {
                        BackHandler {
                            selectedProduct = null
                        }
                        ProductDetailsScreen(
                            product = currentProduct,
                            onBackClick = { selectedProduct = null },
                        )
                    }
                }
            }
        }
    }
}

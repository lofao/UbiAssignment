package lv.uni.ubiassignmentapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import lv.uni.ubiassignmentapplication.core.ui.UbiAssignmentApplicationTheme
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.ui.details.ProductDetailsScreen
import lv.uni.ubiassignmentapplication.feature.products.ui.product.ProductListIntent
import lv.uni.ubiassignmentapplication.feature.products.ui.product.ProductListScreen
import lv.uni.ubiassignmentapplication.feature.products.ui.product.ProductsViewModel
import lv.uni.ubiassignmentapplication.navigation.Screen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configures the app to draw UI content behind the system status bar
        enableEdgeToEdge()
        setContent {
            UbiAssignmentApplicationTheme {
                val viewModel: ProductsViewModel = koinViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val detailsState by viewModel.detailsState.collectAsStateWithLifecycle()
                val backStack = remember { mutableStateListOf<Screen>(Screen.ProductList) }

                NavDisplay(
                    backStack = backStack,
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    entryProvider = entryProvider {
                        entry<Screen.ProductList> {
                            ProductListScreen(
                                onProductClick = { product ->
                                    backStack.add(Screen.ProductDetails(product))
                                    viewModel.processIntent(ProductListIntent.ProductClicked(product))
                                },
                                viewModel = viewModel,
                            )
                        }
                        entry<Screen.ProductDetails> { destination ->
                            viewModel.updateProduct(destination.product)
                            ProductDetailsScreen(
                                state = detailsState,
                                onBackClick = {
                                    if (backStack.size > 1) {
                                        backStack.removeAt(backStack.lastIndex)
                                    }
                                },
                                onRefresh = { viewModel.processIntent(ProductListIntent.LoadProducts) },
                            )
                        }
                    },
                )
            }
        }
    }
}

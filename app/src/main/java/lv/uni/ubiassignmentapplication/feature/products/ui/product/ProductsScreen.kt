package lv.uni.ubiassignmentapplication.feature.products.ui.product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import lv.uni.ubiassignmentapplication.Greeting
import lv.uni.ubiassignmentapplication.core.ui.UbiAssignmentApplicationTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductsRoute(
    onProductClick: (Long) -> Unit,
    onError: (Throwable) -> Unit,
    viewModel: ProductsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProductsScreen(state)
}

@Composable
fun ProductsScreen(state: ProductsState) {
    UbiAssignmentApplicationTheme {
        val isLoading = state.isLoading
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {
            ProductsScreenContent(state)
        }
    }
}

@Composable
fun ProductsScreenContent(state: ProductsState) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Greeting(
            name = "Android",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
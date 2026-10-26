package lv.uni.ubiassignmentapplication.feature.products.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import lv.uni.ubiassignmentapplication.core.ui.UbiAssignmentApplicationTheme
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.domain.model.imageUrl
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductsRoute(
    onProductClick: (Product) -> Unit = {},
    onError: (Throwable) -> Unit = {},
    viewModel: ProductsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProductsScreen(
        state = state,
        onProductClick = onProductClick
    )
}

@Composable
fun ProductsScreen(
    state: ProductsState,
    onProductClick: (Product) -> Unit = {}
) {
    UbiAssignmentApplicationTheme {
        val isLoading = state.isLoading
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            ProductsScreenContent(
                state = state,
                onProductClick = onProductClick
            )
        }
    }
}

@Composable
fun ProductsScreenContent(
    state: ProductsState,
    onProductClick: (Product) -> Unit = {}
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (state.products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.errorText.ifEmpty { "No products available" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = state.products,
                    key = { it.id }
                ) { product ->
                    ProductItem(
                        product = product,
                        onClick = { onProductClick(product) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = product.imageUrl()
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (!imageUrl.isNullOrEmpty()) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = product.productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                ) { requestBuilder ->
                    requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL)
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = product.productName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            product.deviceType?.let { deviceType ->
                if (deviceType.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = deviceType,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductItemPreview() {
    UbiAssignmentApplicationTheme {
        ProductItem(
            product = Product(
                id = "ed67d43e-2d5c-4928-ace8edf984baeff1",
                productName = "UniFi Access Point Pro",
                deviceType = "Access Point",
                imageId = "977c1f8c477549aeb728727fd4ecc62"
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductsScreenPreview() {
    UbiAssignmentApplicationTheme {
        ProductsScreenContent(
            state = ProductsState(
                isLoading = false,
                products = listOf(
                    Product(
                        id = "ed67d43e-2d5c-4928-ace8edf984baeff1",
                        productName = "UniFi Access Point Pro",
                        deviceType = "Access Point",
                        imageId = "977c1f8c477549aeb728727fd4ecc62"
                    ),
                    Product(
                        id = "2",
                        productName = "UniFi Switch 24",
                        deviceType = "Switch",
                        imageId = "977c1f8c477549aeb728727fd4ecc62"
                    )
                )
            )
        )
    }
}

package lv.uni.ubiassignmentapplication.feature.products.ui.product

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import lv.uni.ubiassignmentapplication.core.ui.UbiAssignmentApplicationTheme
import lv.uni.ubiassignmentapplication.core.ui.components.OfflineModeIndicator
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.domain.model.imageUrl
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductsRoute(
    onProductClick: (Product) -> Unit = {},
    viewModel: ProductsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    ProductsScreen(
        state = state,
        onProductClick = onProductClick,
        onRefresh = viewModel::loadProducts,
        onErrorShown = viewModel::onErrorShown,
        listState = listState,
    )
}

@Composable
fun ProductsScreen(
    state: ProductsState,
    onProductClick: (Product) -> Unit = {},
    onRefresh: () -> Unit = {},
    onErrorShown: () -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
) {
    UbiAssignmentApplicationTheme {
        if (state.isLoading && state.products.isEmpty()) {
            FullscreenLoadingIndicator()
        } else {
            ProductsScreenContent(
                state = state,
                onProductClick = onProductClick,
                onRefresh = onRefresh,
                onErrorShown = onErrorShown,
                listState = listState,
            )
        }
    }
}

@Composable
fun ProductsScreenContent(
    state: ProductsState,
    onProductClick: (Product) -> Unit = {},
    onRefresh: () -> Unit = {},
    onErrorShown: () -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
) {
    var selectedTab by rememberSaveable { mutableStateOf(ProductTab.ALL) }
    val snackbarHostState = remember { SnackbarHostState() }

    val filteredProducts = remember(state.products, selectedTab) {
        state.products.filter { product -> selectedTab.matches(product) }
    }

    LaunchedEffect(selectedTab) {
        listState.scrollToItem(0)
    }

    ErrorLaunchedEffect(
        state = state,
        hostState = snackbarHostState,
        onRefresh = onRefresh,
        onErrorShown = onErrorShown,
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ProductsTopBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                isOffline = state.isOffline,
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (filteredProducts.isEmpty()) {
                EmptyProductsView(
                    isProductsEmpty = state.products.isEmpty(),
                    selectedTabTitle = selectedTab.title,
                )
            } else {
                ProductList(
                    products = filteredProducts,
                    listState = listState,
                    onProductClick = onProductClick,
                )
            }
        }
    }
}

@Composable
private fun ProductsTopBar(
    selectedTab: ProductTab,
    onTabSelected: (ProductTab) -> Unit,
    isOffline: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab.ordinal,
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth(),
            ) {
                ProductTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { onTabSelected(tab) },
                        text = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.titleSmall,
                            )
                        },
                    )
                }
            }
        }
        AnimatedVisibility(visible = isOffline) {
            OfflineModeIndicator()
        }
    }
}

@Composable
private fun EmptyProductsView(
    isProductsEmpty: Boolean,
    selectedTabTitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (isProductsEmpty) {
                "No products available"
            } else {
                "No $selectedTabTitle products found"
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ProductList(
    products: List<Product>,
    listState: LazyListState,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        itemsIndexed(
            items = products,
            key = { _, product -> product.id },
            contentType = { _, _ -> "product" },
        ) { index, product ->
            ProductListItem(
                product = product,
                onClick = { onProductClick(product) },
            )
            if (index < products.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                )
            }
        }
    }
}

@Composable
private fun FullscreenLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorLaunchedEffect(
    state: ProductsState,
    hostState: SnackbarHostState,
    onRefresh: () -> Unit,
    onErrorShown: () -> Unit,
) {
    val currentOnRefresh by rememberUpdatedState(onRefresh)
    val currentOnErrorShown by rememberUpdatedState(onErrorShown)

    LaunchedEffect(state.errorText) {
        if (state.errorText.isNotEmpty()) {
            val result = hostState.showSnackbar(
                message = state.errorText,
                actionLabel = "Retry",
                duration = SnackbarDuration.Long,
            )
            if (result == SnackbarResult.ActionPerformed) {
                currentOnRefresh()
            }
            currentOnErrorShown()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductListItem(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val imageUrl = product.imageUrl()
        Surface(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                if (!imageUrl.isNullOrEmpty()) {
                    GlideImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                    ) { requestBuilder ->
                        requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL)
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = product.productName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            product.deviceType?.takeIf { it.isNotBlank() }?.let { deviceType ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = deviceType.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListItemPreview() {
    UbiAssignmentApplicationTheme {
        ProductListItem(
            product = Product(
                id = "ed67d43e-2d5c-4928-ace8edf984baeff1",
                productName = "UniFi Access Point Pro",
                deviceType = "Access Point",
                imageId = "977c1f8c477549aeb728727fd4ecc62",
            ),
            onClick = {},
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
                        productName = "UniFi Protect G4 Camera",
                        deviceType = "camera",
                        imageId = "977c1f8c477549aeb728727fd4ecc62",
                    ),
                    Product(
                        id = "2",
                        productName = "UniFi Smart Sensor",
                        deviceType = "sensor",
                        imageId = "977c1f8c477549aeb728727fd4ecc62",
                    ),
                    Product(
                        id = "3",
                        productName = "UniFi Switch 24",
                        deviceType = "switch",
                        imageId = "977c1f8c477549aeb728727fd4ecc62",
                    ),
                ),
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductsScreenOfflinePreview() {
    UbiAssignmentApplicationTheme {
        ProductsScreenContent(
            state = ProductsState(
                isLoading = false,
                isOffline = true,
                products = listOf(
                    Product(
                        id = "ed67d43e-2d5c-4928-ace8edf984baeff1",
                        productName = "UniFi Protect G4 Camera",
                        deviceType = "camera",
                        imageId = "977c1f8c477549aeb728727fd4ecc62",
                    ),
                ),
            ),
        )
    }
}

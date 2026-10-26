package lv.uni.ubiassignmentapplication.feature.products.ui.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import lv.uni.ubiassignmentapplication.core.ui.UbiAssignmentApplicationTheme
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.domain.model.formattedEthernetSpeed
import lv.uni.ubiassignmentapplication.feature.products.domain.model.imageUrl
import lv.uni.ubiassignmentapplication.feature.products.domain.model.isCamera
import lv.uni.ubiassignmentapplication.feature.products.domain.model.isIot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    product: Product,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = product.productName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ProductHeroImageCard(product = product)

                ProductHeaderSection(product = product)

                ProductGeneralInfoCard(product = product)

                ProductHardwareSpecsCard(product = product)

                //Compliance section is optional
                //ProductComplianceCard(product = product)

                ProductShortNamesCard(product = product)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ProductHeroImageCard(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            val imageUrl = product.imageUrl(LocalContext.current, 512)
            if (!imageUrl.isNullOrEmpty()) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = product.productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                ) { requestBuilder ->
                    requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL)
                }
            } else {
                Text(
                    text = "No image available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ProductHeaderSection(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = product.productName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            product.sku?.let { sku ->
                if (sku.isNotBlank()) {
                    AssistChip(
                        onClick = {},
                        label = { Text(text = "SKU: $sku") },
                    )
                }
            }
            product.deviceType?.let { deviceType ->
                if (deviceType.isNotBlank()) {
                    AssistChip(
                        onClick = {},
                        label = { Text(text = deviceType) },
                    )
                }
            }
            product.wifiStandard?.let { wifi ->
                if (wifi.isNotBlank()) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(text = "Wi-Fi $wifi") },
                    )
                }
            }
            if (product.isCamera) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(text = "Camera") },
                )
            }
            if (product.isIot) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(text = "IoT") },
                )
            }
            if (product.isARSupported) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(text = "AR Supported") },
                )
            }
        }
    }
}

@Composable
private fun ProductGeneralInfoCard(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "General Info",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            product.lineName?.let { lineName ->
                if (lineName.isNotBlank()) {
                    DetailRow(label = "Product Line", value = lineName)
                }
            }

            product.deviceType?.let { deviceType ->
                if (deviceType.isNotBlank()) {
                    DetailRow(label = "Device Type", value = deviceType)
                }
            }

            DetailRow(
                label = "Product ID",
                value = product.id,
                isSelectable = true,
            )
        }
    }
}

@Composable
private fun ProductHardwareSpecsCard(
    product: Product,
    modifier: Modifier = Modifier,
) {
    val hasHardwareSpecs = product.numberOfPorts != null ||
            product.ethernetMaxSpeedMbps != null ||
            product.formFactors.isNotEmpty()

    if (!hasHardwareSpecs) return

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Hardware Specifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            product.numberOfPorts?.let { ports ->
                DetailRow(label = "Number of Ports", value = ports.toString())
            }

            product.formattedEthernetSpeed?.let { speed ->
                DetailRow(label = "Max Ethernet Speed", value = speed)
            }

            if (product.formFactors.isNotEmpty()) {
                DetailRow(
                    label = "Form Factor",
                    value = product.formFactors.joinToString(", "),
                )
            }
        }
    }
}

@Composable
private fun ProductComplianceCard(
    product: Product,
    modifier: Modifier = Modifier,
) {
    val hasCompliance = !product.modelName.isNullOrBlank() ||
            !product.fcc.isNullOrBlank() ||
            !product.ic.isNullOrBlank()

    if (!hasCompliance) return

    var isComplianceExpanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isComplianceExpanded = !isComplianceExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Compliance & Model",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Icon(
                    imageVector = if (isComplianceExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isComplianceExpanded) "Collapse compliance info" else "Expand compliance info",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            AnimatedVisibility(visible = isComplianceExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    product.modelName?.let { modelName ->
                        if (modelName.isNotBlank()) {
                            DetailRow(label = "Model Name", value = modelName)
                        }
                    }

                    product.fcc?.let { fcc ->
                        if (fcc.isNotBlank()) {
                            DetailRow(label = "FCC ID", value = fcc, isSelectable = true)
                        }
                    }

                    product.ic?.let { ic ->
                        if (ic.isNotBlank()) {
                            DetailRow(label = "IC ID", value = ic, isSelectable = true)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductShortNamesCard(
    product: Product,
    modifier: Modifier = Modifier,
) {
    if (product.shortNames.isEmpty()) return

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Model Short Names",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                product.shortNames.forEach { shortName ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(text = shortName) },
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isSelectable: Boolean = false,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (isSelectable) {
            SelectionContainer {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailsScreenPreview() {
    UbiAssignmentApplicationTheme {
        ProductDetailsScreen(
            product = Product(
                id = "ed67d43e-2d5c-4928-ace8edf984baeff1",
                productName = "UniFi Protect G4 Camera",
                deviceType = "camera",
                imageId = "977c1f8c477549aeb728727fd4ecc62",
                lineId = "unifi-protect",
                lineName = "UniFi Protect",
                shortNames = listOf("UVC-G4-PRO", "G4 Pro"),
                sku = "UVC-G4-PRO",
                abbrev = "G4 Pro",
                modelName = "UVC-G4-PRO",
                fcc = "SWX-UVCG4P",
                ic = "6545A-UVCG4P",
                numberOfPorts = 1,
                ethernetMaxSpeedMbps = 1000,
                formFactors = listOf("WALL", "POLE"),
                wifiStandard = "5e",
                isARSupported = true,
            ),
            onBackClick = {},
        )
    }
}

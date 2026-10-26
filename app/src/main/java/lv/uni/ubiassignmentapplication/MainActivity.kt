package lv.uni.ubiassignmentapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import lv.uni.ubiassignmentapplication.core.ui.UbiAssignmentApplicationTheme
import lv.uni.ubiassignmentapplication.feature.products.ui.product.ProductsRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UbiAssignmentApplicationTheme {
                ProductsRoute(
                    onProductClick = {}
                )
            }
        }
    }
}
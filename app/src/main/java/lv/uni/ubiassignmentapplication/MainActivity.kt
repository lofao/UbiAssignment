package lv.uni.ubiassignmentapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import lv.uni.ubiassignmentapplication.core.ui.UbiAssignmentApplicationTheme
import lv.uni.ubiassignmentapplication.feature.products.ui.product.ProductsRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UbiAssignmentApplicationTheme {
                ProductsRoute(
                    onProductClick = {},
                    onError = {}
                )
            }
        }
    }
}
package lv.uni.ubiassignmentapplication.feature.products.ui.details

import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.domain.model.imageUrl
import lv.uni.ubiassignmentapplication.feature.products.domain.model.isCamera
import lv.uni.ubiassignmentapplication.feature.products.domain.model.isIot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductDetailsTest {

    @Test
    fun `imageUrl generates correct url when imageId is present`() {
        val product = Product(
            id = "prod123",
            productName = "Camera Pro",
            deviceType = "camera",
            imageId = "img456",
        )

        val url = product.imageUrl(512)
        assertEquals(
            "https://images.svc.ui.com/?u=https%3A%2F%2Fstatic.ui.com%2Ffingerprint%2Fui%2Fimages%2Fprod123%2Fdefault%2Fimg456.png&w=512&q=75",
            url,
        )
    }

    @Test
    fun `imageUrl returns null when imageId is null`() {
        val product = Product(
            id = "prod123",
            productName = "Camera Pro",
            deviceType = "camera",
            imageId = null,
        )

        assertNull(product.imageUrl())
    }

    @Test
    fun `isCamera identifies camera products correctly`() {
        val cameraProduct = Product(
            id = "1",
            productName = "G4 Pro Camera",
            deviceType = "camera",
            imageId = "img",
        )
        val nonCameraProduct = Product(
            id = "2",
            productName = "UniFi Switch 24",
            deviceType = "switch",
            imageId = "img",
        )

        assertTrue(cameraProduct.isCamera)
        assertFalse(nonCameraProduct.isCamera)
    }

    @Test
    fun `isIot identifies IoT products correctly`() {
        val iotProduct = Product(
            id = "1",
            productName = "Smart Door Lock",
            deviceType = "doorlock",
            imageId = "img",
        )
        val nonIotProduct = Product(
            id = "2",
            productName = "UniFi Access Point",
            deviceType = "access point",
            imageId = "img",
        )

        assertTrue(iotProduct.isIot)
        assertFalse(nonIotProduct.isIot)
    }
}

package lv.uni.ubiassignmentapplication.feature.products.ui.details

import android.content.ContextWrapper
import android.content.res.AssetManager
import lv.uni.ubiassignmentapplication.feature.products.domain.model.Product
import lv.uni.ubiassignmentapplication.feature.products.domain.model.assetRelativePath
import lv.uni.ubiassignmentapplication.feature.products.domain.model.imageUrl
import lv.uni.ubiassignmentapplication.feature.products.domain.model.isCamera
import lv.uni.ubiassignmentapplication.feature.products.domain.model.isIot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.io.IOException

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
    fun `assetRelativePath generates correct path when imageId is present`() {
        val product = Product(
            id = "prod123",
            productName = "Camera Pro",
            deviceType = "camera",
            imageId = "img456",
        )

        val assetPath = product.assetRelativePath()
        assertEquals("prod123/img456.png", assetPath)
    }

    @Test
    fun `assetRelativePath returns null when imageId is null`() {
        val product = Product(
            id = "prod123",
            productName = "Camera Pro",
            deviceType = "camera",
            imageId = null,
        )

        assertNull(product.assetRelativePath())
    }

    @Test
    fun `imageUrl with Context falls back to remote url when not in assets or local files`() {
        val testContext = object : ContextWrapper(null) {
            override fun getFilesDir(): File = File("/non/existent/dir")
            override fun getAssets(): AssetManager {
                throw IOException("Asset not found")
            }
        }
        val product = Product(
            id = "prod123",
            productName = "Camera Pro",
            deviceType = "camera",
            imageId = "img456",
        )

        val url = product.imageUrl(testContext, 512)
        assertEquals(
            "https://images.svc.ui.com/?u=https%3A%2F%2Fstatic.ui.com%2Ffingerprint%2Fui%2Fimages%2Fprod123%2Fdefault%2Fimg456.png&w=512&q=75",
            url,
        )
    }

    @Test
    fun `imageUrl with Context prioritizes local override file when present`() {
        val tempDir = File.createTempFile("test_assets", "dir")
        tempDir.delete()
        tempDir.mkdirs()

        val overrideFile = File(tempDir, "assets/prod123/override.png")
        overrideFile.parentFile?.mkdirs()
        overrideFile.writeBytes(byteArrayOf(1, 2, 3))

        val testContext = object : ContextWrapper(null) {
            override fun getFilesDir(): File = tempDir
            override fun getAssets(): AssetManager {
                throw IOException("Asset not found")
            }
        }
        val product = Product(
            id = "prod123",
            productName = "Camera Pro",
            deviceType = "camera",
            imageId = "img456",
        )

        val url = product.imageUrl(testContext, 512)
        assertEquals(overrideFile.absolutePath, url)

        tempDir.deleteRecursively()
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

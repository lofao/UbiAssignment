package lv.uni.ubiassignmentapplication.feature.products.data.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import lv.uni.ubiassignmentapplication.feature.products.data.model.ProductsResponseDto

class RemoteProductDataSource(private val httpClient: HttpClient): ProductDataSource {
    override suspend fun getProducts(): ProductsResponseDto {
        return httpClient.get("/fingetprint/ui/public.json").body()
    }
}

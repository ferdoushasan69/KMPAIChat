package com.smartai.kmp.data.source.remote

import com.smartai.kmp.data.models.ContentItem
import com.smartai.kmp.data.models.GeminiResponseDto
import com.smartai.kmp.data.models.RequestBody
import com.smartai.kmp.data.models.RequestInlineData
import com.smartai.kmp.data.models.RequestPart
import com.smartai.kmp.utils.GEMINI_PRO
import com.smartai.kmp.utils.GEMINI_PRO_VISION
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.util.encodeBase64
import kotlinx.serialization.json.Json

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    override suspend fun generateContent(
        content: String,
        apiKey: String
    ): GeminiResponseDto {

        val parts = mutableListOf<RequestPart>()
        parts.add(RequestPart(text = content))

        val requestBody = RequestBody(listOf(ContentItem(parts)))

        return try {
            val responseText = client.post {
                url("v1beta/models/$GEMINI_PRO:generateContent")
                parameter("key", apiKey)
                setBody(Json.encodeToString(requestBody))
            }.body<GeminiResponseDto>()
            println("Api response : $responseText")
            responseText
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed api request : ${e.cause}")
            throw e
        }
    }

    override suspend fun generateContentWithImage(
        content: String,
        apiKey: String,
        images: List<ByteArray>
    ): GeminiResponseDto {

        val parts = mutableListOf<RequestPart>()
        parts.add(RequestPart(text = content))

        images.map { image ->
            val inlineData = RequestInlineData("image/jpeg", image.encodeBase64())
            parts.add(RequestPart(inlineData = inlineData))
        }

        val requestBody = RequestBody(contents = listOf(ContentItem(parts)))
        return try {
            val responseText = client.post {
                url("v1beta/models/$GEMINI_PRO_VISION:generateContent")
                parameter("key", apiKey)
                setBody(Json.encodeToString(requestBody))
            }.body<GeminiResponseDto>()
            println("Api response : $responseText")
            responseText
        } catch (e: Exception) {
            println("Api request failed : ${e.cause}")
            throw e
        }
    }
}
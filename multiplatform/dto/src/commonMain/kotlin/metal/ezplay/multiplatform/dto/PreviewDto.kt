package metal.ezplay.multiplatform.dto

import kotlinx.serialization.Serializable

@Serializable
class PreviewDto(
    val fileSize: Long,
    val fileName: String
)
package metal.ezplay.dto

import kotlinx.serialization.Serializable

@Serializable
class PreviewDto(
    val fileSize: Long,
    val fileName: String
)
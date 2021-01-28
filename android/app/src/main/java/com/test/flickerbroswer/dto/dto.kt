package com.test.flickerbroswer.dto

data class PhotosResponse(
    val stat: String,
    val photos: PhotosDto
)

data class PhotosDto(
    val photo: List<PhotoDto>
)

data class PhotoDto(
    val id: String,
    val secret: String,
    val server: String,
    val farm: Int
) {
    fun toUrl() = "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"
}
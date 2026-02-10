package com.example.githubaction.data.dto

data class ShoesDTO(
    val id: String
)


data class ShoesResponse(
    val items: List<ShoesDTO>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
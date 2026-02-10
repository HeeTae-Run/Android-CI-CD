package com.example.githubaction.data.mapper

import com.example.githubaction.data.dto.ShoesDTO
import com.example.githubaction.domain.model.ShoesModel

fun ShoesDTO.asDomain(): ShoesModel {
    return ShoesModel(
        id = id
    )
}
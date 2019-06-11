package com.example.filemanager.base.model

import com.example.filemanager.utils.FileType

data class FileModel(
    val path: String,
    val fileType: FileType,
    val name: String,
    val sizeInMB: Double,
    val extension: String = "",
    val subFiles: Int = 0
)
package com.pocraft.motiontodo.model

import java.util.*

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val title: String = ""
)
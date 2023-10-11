package com.example.wiresag.storage.entities

import com.example.wiresag.state.Pylon
import com.example.wiresag.state.WireSpan

interface EntitiesStore {
    fun save(pylons: Array<Pylon>, spans: Array<WireSpan>)
    fun load(): Entities

    data class Entities(val pylons: Array<Pylon>, val spans: Array<WireSpan>)
}


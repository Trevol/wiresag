package com.example.wiresag.storage.entities

import com.example.wiresag.state.Pylon
import com.example.wiresag.state.WireSpan

class InMemoryEntitiesStore : EntitiesStore {
    private var entities = EntitiesStore.Entities(emptyArray(), emptyArray())

    override fun save(pylons: Array<Pylon>, spans: Array<WireSpan>) {
        entities = EntitiesStore.Entities(pylons, spans)
    }

    override fun load(): EntitiesStore.Entities {
        return entities
    }
}
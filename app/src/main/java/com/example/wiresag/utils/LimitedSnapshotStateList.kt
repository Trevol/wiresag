package com.example.wiresag.utils

import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlin.math.min

class LimitedSnapshotStateList<E>(
    val maxSize: Int,
    private val delegate: SnapshotStateList<E> = SnapshotStateList()
) : MutableList<E> by delegate {

    fun isFull() = size == maxSize

    override fun add(element: E) = if (size < maxSize) {
        delegate.add(element)
    } else {
        false
    }

    override fun addAll(elements: Collection<E>) = if (size < maxSize) {
        val elements = if (elements is List<E>) elements else elements.toList()
        val toIndex = min(maxSize - size, elements.size)
        delegate.addAll(elements.subList(0, toIndex))
    } else {
        false
    }

    override fun addAll(index: Int, elements: Collection<E>) = if (size < maxSize) {
        val elements = if (elements is List<E>) elements else elements.toList()
        val toIndex = min(maxSize - size, elements.size)
        delegate.addAll(index, elements.subList(0, toIndex))
    } else {
        false
    }

    override fun add(index: Int, element: E) {
        if (size < maxSize) {
            delegate.add(index, element)
        }
    }

}
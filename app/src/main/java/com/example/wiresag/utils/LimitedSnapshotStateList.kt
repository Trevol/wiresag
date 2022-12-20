package com.example.wiresag.utils

import androidx.compose.runtime.snapshots.SnapshotStateList

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
        delegate.addAll(elements.toList().subList(0, maxSize - size))
    } else {
        false
    }

    override fun addAll(index: Int, elements: Collection<E>) = if (size < maxSize) {
        delegate.addAll(index, elements.toList().subList(0, maxSize - size))
    } else {
        false
    }

    override fun add(index: Int, element: E) {
        if (size < maxSize) {
            delegate.add(index, element)
        }
    }

}
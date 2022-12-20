package com.example.wiresag.utils

class LimitedMutableList<E>(val maxSize: Int) : MutableList<E>, ArrayList<E>() {

    override fun add(element: E) = if (size < maxSize) {
        super.add(element)
    } else {
        false
    }

    override fun addAll(elements: Collection<E>) = if (size < maxSize) {
        super.addAll(elements.toList().subList(0, maxSize - size))
    } else {
        false
    }

    override fun addAll(index: Int, elements: Collection<E>) = if (size < maxSize) {
        super.addAll(index, elements.toList().subList(0, maxSize - size))
    } else {
        false
    }

    override fun add(index: Int, element: E) {
        if (size < maxSize) {
            super.add(index, element)
        }
    }

}



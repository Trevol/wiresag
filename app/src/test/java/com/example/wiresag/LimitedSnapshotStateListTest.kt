package com.example.wiresag

import com.example.wiresag.utils.LimitedSnapshotStateList
import io.kotest.matchers.shouldBe
import org.junit.Test

class LimitedSnapshotStateListTest {

    @Test
    fun addAll() {
        LimitedSnapshotStateList<Int>(maxSize = 3).let { items ->
            items.addAll(listOf(1))
            items.toList() shouldBe listOf(1)

            items.addAll(listOf(2))
            items.toList() shouldBe listOf(1, 2)

            items.addAll(listOf(3))
            items.toList() shouldBe listOf(1, 2, 3)

            items.addAll(listOf(4, 5, 6))
            items.toList() shouldBe listOf(1, 2, 3)
        }

        LimitedSnapshotStateList<Int>(maxSize = 3).let { items ->
            items.addAll(listOf(1))
            items.toList() shouldBe listOf(1)

            items.addAll(listOf(2, 3, 4, 5, 6))
            items.toList() shouldBe listOf(1, 2, 3)
        }

        LimitedSnapshotStateList<Int>(maxSize = 3).let { items ->
            items.addAll(listOf(1, 2, 3, 4, 5, 6))
            items.toList() shouldBe listOf(1, 2, 3)

            items.addAll(listOf(4, 5, 6))
            items.toList() shouldBe listOf(1, 2, 3)
        }

        //addAll(index, elements)***********************************
        //впихнуть только то, что влезет - существующее не выталкивать
        LimitedSnapshotStateList<Int>(maxSize = 3).let { items ->
            items.addAll(listOf(1, 2))

            items.addAll(1, listOf(4, 5, 6))
            items.toList() shouldBe listOf(1, 4, 2)
        }

        LimitedSnapshotStateList<Int>(maxSize = 3).let { items ->
            items.addAll(listOf(1, 2))

            items.addAll(0, listOf(4, 5, 6))
            items.toList() shouldBe listOf(4, 1, 2)
        }

        LimitedSnapshotStateList<Int>(maxSize = 3).let { items ->
            items.addAll(listOf(1))

            items.addAll(0, listOf(4))
            items.toList() shouldBe listOf(4, 1)
        }

        LimitedSnapshotStateList<Int>(maxSize = 3).let { items ->
            items.addAll(listOf(1))

            items.addAll(1, listOf(4))
            items.toList() shouldBe listOf(1, 4)
        }

        LimitedSnapshotStateList<Int>(maxSize = 3).let { items ->
            items.addAll(0, listOf(4))
            items.toList() shouldBe listOf(4)
        }

        LimitedSnapshotStateList<Int>(maxSize = 3).let { items ->
            items.addAll(0, listOf(1, 2, 3, 4, 5))
            items.toList() shouldBe listOf(1, 2, 3)
        }
    }

}
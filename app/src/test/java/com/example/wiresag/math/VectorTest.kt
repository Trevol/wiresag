package com.example.wiresag.math

import io.kotest.matchers.shouldBe
import org.junit.Test

class VectorTest {
    @Test
    fun isCollinear() {
        Vector2d(.0, -1.45).isCollinear(Vector2d(-.0, 2.34)) shouldBe true
        Vector2d(-1.45, .0).isCollinear(Vector2d(2.34, -.0)) shouldBe true

        Vector2d(-1.45, 2.34).isCollinear(Vector2d(-1.45, 2.34)) shouldBe true
        Vector2d(-1.45, 2.34).isCollinear(Vector2d(-1.45, 2.34) * 1.345) shouldBe true
        Vector2d(-1.45, -2.34).isCollinear(Vector2d(-1.45, 2.34) * 1.345) shouldBe false

        Vector2d(-1.45, 2.34).isCollinear(Vector2d(-1.45, 2.34) * 5.67) shouldBe true
        Vector2d(-1.45, 2.34).isCollinear(Vector2d(-1.45, 2.34) * 5.6779757) shouldBe true
        Vector2d(-1.45, 2.34).isCollinear(Vector2d(-1.45, 2.34) * 5.666666666666) shouldBe true
        Vector2d(-1.45, 2.34).isCollinear(Vector2d(-1.45, 2.34) * 5.77777777777777) shouldBe true
    }
}
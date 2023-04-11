package com.example.wiresag

import com.example.wiresag.math.*
import io.kotest.matchers.shouldBe
import org.junit.Test

class ParametricLineEquationTest {
    @Test
    fun intersection() {

        Pair(
            ParametricLineEquation(Point2d(1.0, 0.0), Vector2d(0.0, 1.5)),
            ParametricLineEquation(Point2d(2.0, 0.0), Vector2d(1.5, 0.0))
        ).let { (eq1, eq2) ->
            val expected = Point2d(1.0, 0.0)
            eq1.intersection(eq2) shouldBe expected
            eq2.intersection(eq1) shouldBe expected
        }

        Pair(
            ParametricLineEquation(Point2d(1.0, 0.0), Vector2d(0.0, 1.5)),
            ParametricLineEquation(Point2d(2.0, 1.34), Vector2d(1.5, 0.0))
        ).let { (eq1, eq2) ->
            val expected = Point2d(1.0, 1.34)
            eq1.intersection(eq2) shouldBe expected
            eq2.intersection(eq1) shouldBe expected
        }


        Pair(
            ParametricLineEquation(Point2d(2.0, 0.0), Vector2d(1.5, 0.0)),
            ParametricLineEquation(Point2d(1.0, 2.0), Vector2d(2.0, 1.0))
        ).let { (eq1, eq2) ->
            val expected = Point2d(-3.0, 0.0)
            eq1.intersection(eq2) shouldBe expected
            eq2.intersection(eq1) shouldBe expected
        }

        Pair(
            ParametricLineEquation(Point2d(2.0, 0.0), Vector2d(0.0, 1.5)),
            ParametricLineEquation(Point2d(1.0, 2.0), Vector2d(2.0, 1.0))
        ).let { (eq1, eq2) ->
            val expected = Point2d(2.0, 2.5)
            eq1.intersection(eq2) shouldBe expected
            eq2.intersection(eq1) shouldBe expected
        }

        //***************
        Pair(
            ParametricLineEquation(Point2d(0.0, 0.0), Vector2d(1.0, 1.0)),
            ParametricLineEquation(Point2d(2.0, 0.0), Vector2d(-1.0, 1.0))
        ).let { (eq1, eq2) ->
            val expected = Point2d(1.0, 1.0)
            eq1.intersection(eq2) shouldBe expected
            eq2.intersection(eq1) shouldBe expected
        }

        Pair(
            ParametricLineEquation(Point2d(0.0, 0.0), Vector2d(1.0, 2.0)),
            ParametricLineEquation(Point2d(2.0, 0.0), Vector2d(-1.0, 2.0))
        ).let { (eq1, eq2) ->
            val expected = Point2d(1.0, 2.0)
            eq1.intersection(eq2) shouldBe expected
            eq2.intersection(eq1) shouldBe expected
        }

        Pair(
            ParametricLineEquation(Point2d(0.0, 0.0), Vector2d(1.0, 3.0)),
            ParametricLineEquation(Point2d(2.0, 0.0), Vector2d(-1.0, 3.0))
        ).let { (eq1, eq2) ->
            val expected = Point2d(1.0, 3.0)
            eq1.intersection(eq2) shouldBe expected
            eq2.intersection(eq1) shouldBe expected
        }

        Pair(
            ParametricLineEquation(Point2d(0.0, 2.867), Vector2d(1.0, 3.787)),
            ParametricLineEquation(Point2d(2.0, 2.867), Vector2d(-1.0, 3.787))
        ).let { (eq1, eq2) ->
            val expected = Point2d(1.0, 2.867 + 3.787)
            eq1.intersection(eq2) shouldBe expected
            eq2.intersection(eq1) shouldBe expected
        }

        Pair(
            ParametricLineEquation(Point2d(2.0, -3.0), Vector2d(-4.0, -2.0)),
            ParametricLineEquation(Point2d(-1.0, 2.0), Vector2d(2.0, -1.0))
        ).let { (eq1, eq2) ->
            val expected = Point2d(5.5, -1.25)
            eq1.intersection(eq2) shouldBe expected
            eq2.intersection(eq1) shouldBe expected
        }

        /*Pair(
            ParametricLineEquation(Point2d(2.0, -3.0), Vector2d(-4.0, 1.0)),
            ParametricLineEquation(Point2d(-1.0, 2.0), Vector2d(2.0, -1.0))
        ).let { (eq1, eq2) ->
            val expected = Point2d(16.25, -6.75)
            eq1.intersection(eq2) shouldBe expected
            eq2.intersection(eq1) shouldBe expected
        }*/
        //****************

        Pair(
            ParametricLineEquation(Point2d(1.0, 0.0), Vector2d(0.0, 1.5)),
            ParametricLineEquation(Point2d(2.0, 0.0), Vector2d(0.0, -1.5))
        ).let { (eq1, eq2) ->
            eq1.intersection(eq2) shouldBe null
            eq2.intersection(eq1) shouldBe null
            eq1.intersection(eq1) shouldBe null
        }

        Pair(
            ParametricLineEquation(Point2d(1.0, 34.678), Vector2d(34.56, 1.5)),
            ParametricLineEquation(Point2d(2.43, 67.21), Vector2d(-34.56 * 5.67, -1.5 * 5.67))
        ).let { (eq1, eq2) ->
            eq1.intersection(eq2) shouldBe null
            eq2.intersection(eq1) shouldBe null
            eq1.intersection(eq1) shouldBe null
        }
    }
}
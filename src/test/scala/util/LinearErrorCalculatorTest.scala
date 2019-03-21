package util

import model.{HeightWeight, SimplePoint}
import org.specs2.mutable.Specification

class LinearErrorCalculatorTest extends Specification {

  "linearMeanSquaredError" should {
    "sum up the squares of errors" in {

      val expected = List(
        HeightWeight("Male",174.0,96.0),
        HeightWeight("Male",189.0,87.0),
      )

      val data = expected.map(dataPoint => SimplePoint(dataPoint.height, dataPoint.weight))
      val linearMeanSquaredError: Double = LinearErrorCalculator.linearMeanSquaredError(data, 1, 1)

      linearMeanSquaredError shouldEqual(4212.5)
    }

  }

  "linear mean absolute error" should {
    "sum up absolute errors" in {
      val expected = List(
        HeightWeight("Female",174.0,96.0),
        HeightWeight("Male",189.0,87.0),
      )

      val data = expected.map(dataPoint => SimplePoint(dataPoint.height, dataPoint.weight))
      val meanAbsoluteError: Double = LinearErrorCalculator.linearMeanAbsoluteError(data, 1, 1)

      meanAbsoluteError shouldEqual(91.0)
    }
  }
}

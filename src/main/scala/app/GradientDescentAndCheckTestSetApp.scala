package app

import com.cibo.evilplot._
import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
import model.{HeightWeight, SimplePoint}
import util.GradientDescent.LearnedParameterSet
import util._


object GradientDescentAndCheckTestSetApp {

  def main(args: Array[String]): Unit = {

    def arrayToHeightWeight(strings: Array[String]) = HeightWeight(strings(0), strings(1).toDouble, strings(2).toDouble)
    def heightWeightTo2dPoint(data: List[HeightWeight]) = data.map(dataPoint => SimplePoint(dataPoint.height, dataPoint.weight))

    //load in the training data
    val heightWeights = CsvReader.asCaseClassList("/nlys-train.csv", true, arrayToHeightWeight)
    val data = heightWeightTo2dPoint(heightWeights)
    val normalisedData = FeatureScaler.meanNormalisedData(data)

    //run gradient descent
    val learnedParameters: LearnedParameterSet = GradientDescent.gradientDescent(normalisedData, 0, 1, 3, 500)

    //load in test data
    val heightWeightsTestData = CsvReader.asCaseClassList("/nlys-test.csv", true, arrayToHeightWeight)
    val heightWeightsTestDataPoints = heightWeightTo2dPoint(heightWeightsTestData)
    val heightWeightTestDataScaled = FeatureScaler.meanNormalisedData(heightWeightsTestDataPoints)

    //plot the trend of cost vs iteration so we know if gradient descent is working
    displayPlot(Plotter.costItersPlot(learnedParameters.history, None))
    //plot our training data, and the line that we fit with gradient descent
    displayPlot(Plotter.heightWeightPlot(normalisedData.map(p => HeightWeight("", p.x, p.y)), learnedParameters.theta0, learnedParameters.theta1, Some("Height vs weight TRAINING data and line fit via linear regression")))
    //plot our test data, and the line that we fit with gradient descent
    displayPlot(Plotter.heightWeightPlot(heightWeightTestDataScaled.map(p => HeightWeight("", p.x, p.y)), learnedParameters.theta0, learnedParameters.theta1, Some("Height vs weight TEST data and line fit via linear regression")))

    //just print out the mean absolute error for the line we fit when we compare to the test set
    val meanAbsoluteError = LinearErrorCalculator.linearMeanAbsoluteError(normalisedData, learnedParameters.theta0, learnedParameters.theta1)
    println(s"Hello, world! This is the absolute error: $meanAbsoluteError")
  }

}

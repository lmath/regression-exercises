package app

import com.cibo.evilplot._
import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
import model.{HeightWeight, House, SimplePoint}
import util.GradientDescent.LearnedParameterSet
import util.{CsvReader, FeatureScaler, GradientDescent, Plotter}

object GradientDescentApp {

  def main(args: Array[String]): Unit = {

//    def arrayToHouse(strings: Array[String]) = House(strings(0), strings(4).toDouble, strings(80).toDouble)
    def arrayToHouse(strings: Array[String]) = House(strings(0), strings(1).toDouble, strings(2).toDouble)
    def houseTo2dPoint(data: List[House]) = data.map(dataPoint => SimplePoint(dataPoint.lotArea, dataPoint.salePrice))

    //load in the training data
    val heightWeights = CsvReader.asCaseClassList("house-prices-train.csv", true, arrayToHouse)
    val data = houseTo2dPoint(heightWeights)
    val normalisedData = FeatureScaler.meanNormalisedData(data)

    //run gradient descent
    val learnedParameters: LearnedParameterSet = GradientDescent.gradientDescent(normalisedData, 0, 1, 3, 500)

    //plot the trend of cost vs iteration so we know if gradient descent is working
    displayPlot(Plotter.costItersPlot(learnedParameters.history, None))
    //plot our training data, and the line that we fit with gradient descent
    displayPlot(Plotter.heightWeightPlot(normalisedData.map(p => HeightWeight("", p.x, p.y)), learnedParameters.theta0, learnedParameters.theta1, Some("Height vs weight TRAINING data and line fit via linear regression")))
  }

}

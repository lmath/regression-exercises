package app

import com.cibo.evilplot._
import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
import model.{HeightWeight, House, SimplePoint}
import util.GradientDescent.LearnedParameterSet
import util.{CsvReader, FeatureScaler, GradientDescent, Plotter}

object GradientDescentApp {

  def main(args: Array[String]): Unit = {

    def arrayToHouse(strings: Array[String]) = House(strings(0), strings(4).toDouble, strings(80).toDouble)
    def HouseTo2dPoint(data: List[House]) = data.map(dataPoint => SimplePoint(dataPoint.lotArea, dataPoint.salePrice))

    //load in the training data
    val sizesPrices = CsvReader.asCaseClassList("/house-prices-training-data.csv", true, arrayToHouse)
    val data = HouseTo2dPoint(sizesPrices)
    val normalisedData = FeatureScaler.meanNormalisedData(data)

    //run gradient descent
    val learnedParameters: LearnedParameterSet = GradientDescent.gradientDescent(normalisedData, 0, 1, 1, 5000)

    //plot the trend of cost vs iteration so we know if gradient descent is working
    displayPlot(Plotter.costItersPlot(learnedParameters.history, None))
    //plot our training data, and the line that we fit with gradient descent
    displayPlot(Plotter.scatterPlotWithFittedLine(
      data = normalisedData,
      theta0 = learnedParameters.theta0,
      theta1 = learnedParameters.theta1,
      title = None,
      xLabel = "Lot size",
      yLabel = "Selling price"
    )
    )
  }


}

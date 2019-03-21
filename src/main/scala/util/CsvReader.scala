package util

import java.io._

import scala.io.Source

object CsvReader {

  def testResourceFile(fileName: String): File = {
    val classLoader = getClass().getClassLoader()
    new File(classLoader.getResource(fileName).getFile())
  }

  //TODO: have one function for this
  def asCaseClassList[T](csvFile: String, hasHeader: Boolean, transformer: Array[String] => T): List[T] = {
    import scala.collection.mutable.ListBuffer

    val fileStream: InputStream = getClass.getResourceAsStream(s"$csvFile")
    val lines: Iterator[String] = Source.fromInputStream(fileStream).getLines

    var horribleMutableList = new ListBuffer[T]()
    val iterator = if(hasHeader) lines.drop(1) else lines

    for (line <- iterator) {
      val cols: Array[String] = line.split(",").map(_.trim)
      val newItem = transformer(cols)
      horribleMutableList += newItem
    }
    fileStream.close

    horribleMutableList.toList
  }

  def asCaseClassListFromTestResource[T](csvFile: String, hasHeader: Boolean, transformer: Array[String] => T): List[T] = {
    import scala.collection.mutable.ListBuffer

    val file = testResourceFile(csvFile)

    val bufferedSource = scala.io.Source.fromFile(file)
    val iterator = if(hasHeader) bufferedSource.getLines().drop(1) else bufferedSource.getLines()

    var horribleMutableList = new ListBuffer[T]()

    for (line <- iterator) {
      val cols: Array[String] = line.split(",").map(_.trim)
      val newItem = transformer(cols)
      horribleMutableList += newItem
    }
    bufferedSource.close

    horribleMutableList.toList
  }
}

object CSVWriter {

  def writeCaseClassListToCsv[T](csvFile: String, transformer: T => Array[String], data: List[T]) = {
    val writer = new BufferedWriter(new FileWriter(csvFile))

    val rows: Seq[String] = data.map { row =>
      s"${transformer(row).mkString(",")}\n"
    }
    rows.foreach(writer.write)
    writer.close()
  }

}


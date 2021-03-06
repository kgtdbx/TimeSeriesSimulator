package org.onoreak.timeSeries

import java.io.{File, _}
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId, ZoneOffset}

import be.cetic.tsimulus.Utils.{config2Results, generate}
import be.cetic.tsimulus.config.Configuration
import org.joda.time.format.DateTimeFormat
import spray.json._

import scala.io.Source

/**
  * Created by Chris on 5/1/17.
  */
object CsvMain
{
  def main(args: Array[String]): Unit =
  {
    if (args.length < 1) {
      println("Usage: uber-tssimulatorcontroller-1.0-SNAPSHOT.jar {path-to-config.json}")
      return
    }

    var fileIter = 0;

    val content = Source.fromFile(new File(args(0)))
      .getLines()
      .mkString("\n")

    val dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS")

    val config = Configuration(content.parseJson)
    var file = new File("output - " + fileIter +  " .csv")
    var bw = new BufferedWriter(new FileWriter(file))

    bw.write("date,series,value\n")

    generate(config2Results(config)).foreach {e =>
      var dt = LocalDateTime.parse(dtf.print(e._1), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
      var zdt = dt.atZone(ZoneOffset.UTC)
      bw.write(zdt.toInstant.toEpochMilli + "," + e._2 + "," + e._3 + "\n")

    }
    bw.close()
    println("Done!")
  }
}
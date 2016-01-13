package org.template.clustering

import io.prediction.controller.PPreparator
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.joda.time.{DateTime,LocalDateTime}

class PreparedData(
  val points: RDD[(String,Double,Double,DateTime)]
) extends Serializable

class Preparator
  extends PPreparator[TrainingData, PreparedData] {

  def prepare(sc: SparkContext, trainingData: TrainingData): PreparedData = {
    new PreparedData(points = trainingData.points)
  }
}



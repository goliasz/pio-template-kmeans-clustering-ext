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
    //Take only events with max event time
    val gr = trainingData.points.map(x=>((x._1,x._2,x._3),x._4)).groupByKey.map(x=>(x._1, x._2.map(_.getMillis).max)).map(x=>(x._1._1,x._1._2,x._1._3,new DateTime(x._2)))
    new PreparedData(points = gr)
  }
}



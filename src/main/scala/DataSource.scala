package org.template.clustering

import io.prediction.controller.PDataSource
import io.prediction.controller.EmptyEvaluationInfo
import io.prediction.controller.EmptyActualResult
import io.prediction.controller.Params
import io.prediction.data.store.PEventStore
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.joda.time.{DateTime,LocalDateTime}

import grizzled.slf4j.Logger

case class DataSourceParams(appName: String, limitDays: Int) extends Params

class DataSource(val dsp: DataSourceParams)
  extends PDataSource[TrainingData, EmptyEvaluationInfo, Query, EmptyActualResult] {

  @transient lazy val logger = Logger[this.type]

  override
  def readTraining(sc: SparkContext): TrainingData = {
    println("Gathering data from event server.")
    val aStartTime = if (dsp.limitDays>0) Option(new org.joda.time.LocalDateTime().minusDays(dsp.limitDays).toDateTime) else None

	  val pointsRDD: RDD[(String,Double,Double,DateTime)] = PEventStore.find(
      appName = dsp.appName,
      entityType = Option("point"),
      startTime = aStartTime,
      eventNames = Some(List("$set")))(sc).map { event =>
        try {
	        (event.properties.get[String]("id"), event.properties.get[Double]("attr0"), event.properties.get[Double]("attr1"),event.eventTime)
        } catch {
          case e: Exception => {
            logger.error(s"Failed to convert event ${event} of. Exception: ${e}.")
            throw e
          }
        }
      }

//			Vectors.dense(Array(
//              properties.get[Double]("attr0"),
//              properties.get[Double]("attr1")
//        ))

    new TrainingData(pointsRDD)
  }
}

class TrainingData(
  val points: RDD[(String,Double,Double,DateTime)]
) extends Serializable


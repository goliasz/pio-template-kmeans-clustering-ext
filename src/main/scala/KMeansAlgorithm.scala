package org.template.clustering

import io.prediction.controller.P2LAlgorithm
import io.prediction.controller.Params
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import grizzled.slf4j.Logger
import org.joda.time.DateTime

case class AlgorithmParams(
  val numberOfCenters: Int,
  val numberOfIterations: Int,
  val numberOfRuns: Int,
  val initMode: String,
  val seed: Int
) extends Params

class KMExtModel(
  val kMeansModel: KMeansModel,
  val ids: List[(String,Double,Double)]
) extends Serializable {}

class KMeansAlgorithm(val ap: AlgorithmParams) extends P2LAlgorithm[PreparedData, KMExtModel, Query, PredictedResult] {

  @transient lazy val logger = Logger[this.type]

  def train(sc: SparkContext, data: PreparedData): KMExtModel = {
    println("Running the K-Means-Ext clustering algorithm.")

    new KMExtModel(
      kMeansModel = KMeans.train(
        data.points.map(x=>(x._2,x._3)).distinct.map(x=>(Vectors.dense(Array(x._1,x._2)))).cache, 
        ap.numberOfCenters, 
        ap.numberOfIterations, 
        ap.numberOfRuns, 
        ap.initMode, 
        ap.seed
      ),
      ids = data.points.map(x=>(x._1,x._2,x._3)).distinct.collect.toList 
    )
  }

  def predict(model: KMExtModel, query: Query): PredictedResult = {
    val result = model.ids.filter(x=>{query.id==x._1}).map(x=>(model.kMeansModel.predict(Vectors.dense(Array(x._2,x._3))),x._2,x._3)).map(x=>{new ClusterResult(x._1,x._2,x._3)})

    PredictedResult(clusters = result.toArray)
  }
}

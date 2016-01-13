package org.template.clustering

import io.prediction.controller.{Engine,EngineFactory}

case class Query(
  val id: String
) extends Serializable

case class PredictedResult(
  clusters: Array[ClusterResult]
) 
extends Serializable

case class ClusterResult(
  cluster: Double,
  attr0: Double,
  attr1: Double
//,
//  eventTime: DateTime
) extends Serializable

object ClusteringEngine extends EngineFactory {
  def apply() = {
    new Engine(
      classOf[DataSource],
      classOf[Preparator],
      Map("kmeans_ext" -> classOf[KMeansAlgorithm]),
      	classOf[Serving])
  }
}

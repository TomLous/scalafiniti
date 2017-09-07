package com.datlinq.datafiniti


import com.datlinq.datafiniti.config.DatafinitiAPIFormats.CSV
import com.datlinq.datafiniti.config.DatafinitiAPIViews.BusinessesAllBasic
import com.datlinq.datafiniti.response.DatafinitiTypes.DatafinitiFuture
import com.typesafe.config.{Config, ConfigFactory}
import org.json4s._
import org.scalatest._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._



/**
  * Created by Tom Lous on 07/09/2017.
  * Copyright © 2017 Datlinq B.V..
  */
class CustomTestIgnore extends fixture.FunSuite with PrivateMethodTester {


  type FixtureParam = DatafinitiAPIv3
  implicit val json4sFormats: DefaultFormats.type = DefaultFormats

  def withFixture(test: OneArgTest): Outcome = {
    implicit val config: Config = ConfigFactory.load()
    val apiv3 = DatafinitiAPIv3()
    test(apiv3)
  }


  ignore("download just-eat.co.uk") { apiv3 => {

    val et: DatafinitiFuture[List[String]] = apiv3.downloadLinks(BusinessesAllBasic, Some("""sourceURLs:*just-eat.co.uk*"""), CSV)

    val resultList = Await.result(et.value, Duration.Inf)


    println(resultList)


  }
  }

  test("download just-eat.co.uk (2)") { apiv3 => {

    val et: DatafinitiFuture[JValue] = apiv3.query(
      apiView = BusinessesAllBasic,
      query = Some("""sourceURLs:*just-eat.co.uk*"""),
      numberOfRecords = Some(30399),
      format = CSV,
      download = Some(false))

    val result = Await.result(et.value, Duration.Inf)


    val response = result.getOrElse(JNothing)


    println("Amount: " + (response \ "estimated total").extract[Long])


    val csv = (response \ "records").extract[String]

    val p = new java.io.PrintWriter("/tmp/justeat.csv")
    p.write(csv)
    p.close()


    println(csv)


  }
  }


  test("get user info") { apiv3 => {

    val et: DatafinitiFuture[Option[Long]] = apiv3.userInfoField("available_downloads")

    val resultList = Await.result(et.value, Duration.Inf)

    assert(resultList.isRight)
    assert(resultList.getOrElse(None).getOrElse(0L) > 0)
  }
  }

}

package com.datlinq.datafiniti

import com.datlinq.datafiniti.config.APIFormats.JSON
import com.datlinq.datafiniti.config.APITypes._
import com.datlinq.datafiniti.config.APIViews.BusinessesAllBasic
import com.typesafe.config.ConfigFactory
import org.scalatest._

/**
  * Created by Tom Lous on 30/08/2017.
  * Copyright © 2017 Datlinq B.V..
  */
class APIv3Test extends fixture.FunSuite with PrivateMethodTester {

  type FixtureParam = APIv3

  def withFixture(test: OneArgTest): Outcome = {
    val config = ConfigFactory.load()
    val apiKey = config.getString("apiKey")
    val apiv3 = APIv3(apiKey)
    test(apiv3)
  }



  test("private buildUrl") { apiv3 => {
    val buildUrl = PrivateMethod[String]('buildUrl)
    val token = "-token-"

    def invoke(apiType: APIType, queryParts: Map[String, Any]): String = {
      apiv3.invokePrivate(buildUrl(apiType, queryParts)).replace(apiv3.apiToken, token)
    }

    assert(invoke(Businesses, Map.empty[String, Any]) === s"https://$token:@api.datafiniti.co/v3/data/businesses")
    assert(invoke(Products, List("a" -> 1).toMap) === s"https://$token:@api.datafiniti.co/v3/data/products?a=1")
    assert(invoke(Products, List("a" -> 1, "b" -> None, "c" -> Some(true)).toMap) === s"https://$token:@api.datafiniti.co/v3/data/products?a=1&c=true")
    assert(invoke(Businesses, List("view" -> "businesses_all", "format" -> "JSON", "q" -> Some("categories:hotels"), "records" -> 1, "download" -> false).toMap) === s"https://$token:@api.datafiniti.co/v3/data/businesses?format=JSON&q=categories:hotels&records=1&download=false&view=businesses_all")
  }
  }


  test("query") { apiv3 => {
    val res = apiv3.query(BusinessesAllBasic, Some("categories:hotels"), Some(1), Some(false), JSON)

    //      .map{
    //            case json:JValue => {
    //
    //              println(json)
    //              json
    //            }
    //            case x => {
    //              println(x)
    //
    //            }

    //    val output = Await.result(res, Duration.Inf)


    //        println("test")
    //        println(output)

    //      .map{
    //      case json:JValue => {
    //        println(json)
    //        assert(true)
    //      }
    //      case x => {
    //        println(x)
    //        assert(false)
    //      }
    //    }

  }
  }

}

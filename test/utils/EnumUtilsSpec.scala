/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils

import models.CompanyBenefits
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import uk.gov.hmrc.play.test.UnitSpec

class EnumUtilsSpec extends UnitSpec {

  "EnumerationUtil" should {

    "return a JsError when it cannot parse json object" in {
      val json = Json.parse(
        """
          |{
          | "enum" : "something"
          |}
        """.stripMargin)
      json.validate[CompanyBenefits.Value] match {
        case JsSuccess(v, _) =>
          !v.isInstanceOf[CompanyBenefits.Value]
        case JsError(errors) =>
          errors.head._2.head.message shouldBe "String value expected"
      }
    }

    "return a JsError when it cannot parse json string" in {
      val json = Json.parse(
        """
          |"something"
        """.stripMargin)
      json.validate[CompanyBenefits.Value] match {
        case JsSuccess(v, _) =>
          !v.isInstanceOf[CompanyBenefits.Value]
        case JsError(errors) =>
          errors.head._2.head.message shouldBe
            "Enumeration expected of type: 'class models.CompanyBenefits$', but it does not appear to contain the value: 'something'"
      }
    }

    "return success when the the input is part of the Enum" in {
      val json = Json.parse(
        """
          |"company-car-benefit"
        """.stripMargin)
      json.validate[CompanyBenefits.Value] match {
        case JsSuccess(v, _) =>
          v.isInstanceOf[CompanyBenefits.Value] shouldBe true
        case JsError(errors) =>
          errors.head._2.head.message shouldBe ""
      }
    }

    "return valid json when the object is written" in {
      val res: JsValue = Json.toJson(CompanyBenefits.COMPANY_CAR_BENEFIT)
      res.toString() shouldBe "\"company-car-benefit\""
    }

  }

}

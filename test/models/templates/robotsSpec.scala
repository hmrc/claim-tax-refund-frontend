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

package models.templates

import base.SpecBase
import models.templates.xml.robots
import org.jsoup.Jsoup
import play.api.Logger
import utils.{MockUserAnswers, RobotsXmlHelper, WireMockHelper}

class robotsSpec extends SpecBase with WireMockHelper{
  val fullUserAnswers = MockUserAnswers.fullValidUserAnswers
  val fullXml = robots(fullUserAnswers, new RobotsXmlHelper(fullUserAnswers)(messages))

  "robots Xml" must {
    "have all the sections when all questions answered" in {
      val xml = Jsoup.parse(fullXml.toString)
      xml.select("contactSection") mustBe(???)
    }

    "not have a line feed" when {
      "the xml is generated" in {
        Logger.warn (s"\n\n\n\n\n\n\n\n     $fullXml \n\n\n")
        1 mustBe 3

      }
    }
  }


}
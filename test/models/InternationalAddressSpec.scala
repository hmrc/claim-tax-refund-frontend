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

package models

import base.SpecBase

import scala.xml.Node
import scala.xml.Utility._

class InternationalAddressSpec extends SpecBase {

  "answered lines" must {

    "contain address line 1, 2 and country when optional lines aren't answered" in {
      val address = InternationalAddress("line1", "line2", None, None, None, "country")
      InternationalAddress.answeredLines(address) mustBe Seq("line1", "line2", "country")
    }

    "contain address lines 3, 4 and 5 if they have been answered" in {
      val address = InternationalAddress("line1", "line2", Some("line3"), Some("line4"), Some("line5"), "country")
      InternationalAddress.answeredLines(address) mustBe Seq("line1", "line2", "line3", "line4", "line5", "country")
    }
  }


  "as Xml" must {
    "pass the correct xml back" in {
      val address = InternationalAddress("line1", "line2", None, Some("line4"), None, "country")
      val expectedElem: Node =
        trim(<paymentAddress>
          <internationalAddress>
            <addressLine1>line1</addressLine1>
            <addressLine2>line2</addressLine2>
            <addressLine3></addressLine3>
            <addressLine4>line4</addressLine4>
            <addressLine5></addressLine5>
            <country>country</country>
          </internationalAddress>
        </paymentAddress>)

      InternationalAddress.toXml(address) mustBe expectedElem
    }
  }

  "as string" must {

    "give a comma-separated string of all of the answered lines" in {
      val address = InternationalAddress("line1", "line2", None, Some("line4"), None, "country")
      InternationalAddress.asString(address) mustBe "line1<br>line2<br>line4<br>country"
    }
  }
}

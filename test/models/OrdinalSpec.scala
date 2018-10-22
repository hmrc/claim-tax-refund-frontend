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

class OrdinalSpec extends SpecBase {
	"Oridinal model" must {
		"return 'first' when index is 0" in {
			Ordinal.getOrdinal(Index(0)) mustBe "first"
		}

		"return 'third' when index is 2" in {
			Ordinal.getOrdinal(Index(2)) mustBe "third"
		}

		"return 'first' when index is 0 and capaitalize is used" in {
			Ordinal.getOrdinal(Index(0)).capitalize mustBe "First"
		}

	}
}
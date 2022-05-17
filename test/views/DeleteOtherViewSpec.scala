/*
 * Copyright 2022 HM Revenue & Customs
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

package views

import play.api.data.Form
import controllers.routes
import forms.BooleanForm
import models.SelectTaxYear.CYMinus3
import models.{Index, NormalMode}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import views.behaviours.NewYesNoViewBehaviours
import views.html.deleteOther

class DeleteOtherViewSpec extends NewYesNoViewBehaviours with GuiceOneAppPerSuite {

  val messageKeyPrefix = "deleteOther"
  val itemName = "qwerty"
  val index = Index(1)
  val benefitCollectionId = "otherBenefit"
  private val taxYear = CYMinus3

  override val form = new BooleanForm()()
  val deleteOther: deleteOther = fakeApplication.injector.instanceOf[deleteOther]

  def createView = () =>
    deleteOther(
      form = form,
      mode = NormalMode,
      index = index,
      itemName = itemName,
      collectionId = benefitCollectionId,
      taxYear = taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) =>
    deleteOther(
      form = form,
      mode = NormalMode,
      index = index,
      itemName = itemName,
      collectionId = benefitCollectionId,
      taxYear = taxYear)(fakeRequest, messages)

  "DeleteOther view" must {

    behave like normalPage(createView, messageKeyPrefix, None, "qwerty")

    behave like pageWithBackLink(createView)

    behave like yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.DeleteOtherController.onSubmit(NormalMode, index, itemName, benefitCollectionId).url,
      expectedHintTextKey = None,
      args = "qwerty"
    )
  }
}

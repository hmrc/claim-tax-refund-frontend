/*
 * Copyright 2020 HM Revenue & Customs
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

import identifiers._
import javax.inject.Singleton
import models._
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap

@Singleton
class CascadeUpsert {

  val funcMap: Map[String, (JsValue, CacheMap) => CacheMap] =
    Map(
      SelectTaxYearId.toString -> removeEmployments,
      SelectBenefitsId.toString -> storeBenefit,
      SelectCompanyBenefitsId.toString -> storeCompanyBenefit,
      SelectTaxableIncomeId.toString -> storeTaxableIncome,
      AnyCompanyBenefitsId.toString -> anyCompanyBenefits,
      AnyTaxableIncomeId.toString -> anyTaxableIncome,
      AnyBenefitsId.toString -> anyBenefits,
      EmploymentDetailsId.toString -> claimDetails,
      WhereToSendPaymentId.toString -> whereToSendPayment,
      PaymentAddressCorrectId.toString -> paymentAddressCorrect,
      IsPaymentAddressInTheUKId.toString -> isPaymentAddressInTheUK,
      OtherBenefit.collectionId -> removeOtherBenefit,
      OtherCompanyBenefit.collectionId -> removeOtherCompanyBenefit,
      OtherTaxableIncome.collectionId -> removeOtherTaxableIncome
    )

  def apply[A](key: String, value: A, originalCacheMap: CacheMap)(implicit fmt: Format[A]): CacheMap =
    funcMap.get(key).fold(store(key, value, originalCacheMap)) { fn => fn(Json.toJson(value), originalCacheMap) }

  def addRepeatedValue[A](key: String, value: A, originalCacheMap: CacheMap)(implicit fmt: Format[A]): CacheMap = {
    val values = originalCacheMap.getEntry[Seq[A]](key).getOrElse(Seq()) :+ value
    originalCacheMap copy (data = originalCacheMap.data + (key -> Json.toJson(values)))
  }

  private def store[A](key: String, value: A, cacheMap: CacheMap)(implicit fmt: Format[A]) =
    cacheMap copy (data = cacheMap.data + (key -> Json.toJson(value)))

  private def removeEmployments(newTaxYear: JsValue, cacheMap: CacheMap): CacheMap = {
    val mapToStore = cacheMap.data.get(SelectTaxYearId.toString).map {
      cachedTaxYear =>
        if(cachedTaxYear != newTaxYear){
          cacheMap copy (data = cacheMap.data - (EmploymentDetailsId.toString, EnterPayeReferenceId.toString, DetailsOfEmploymentOrPensionId.toString))
        } else {
          cacheMap
        }
    }.getOrElse(cacheMap)

    store(SelectTaxYearId.toString, newTaxYear, mapToStore)
  }

  private def anyBenefits(value: JsValue, cacheMap: CacheMap): CacheMap =
    if (value.as[Boolean]) {
      store(AnyBenefitsId.toString, value, cacheMap)
    } else {
      store(AnyBenefitsId.toString, value, cacheMap.copy(data = cacheMap.data - (
        SelectBenefitsId.toString,
        HowMuchBereavementAllowanceId.toString,
        HowMuchCarersAllowanceId.toString,
        HowMuchJobseekersAllowanceId.toString,
        HowMuchIncapacityBenefitId.toString,
        HowMuchEmploymentAndSupportAllowanceId.toString,
        HowMuchStatePensionId.toString,
        OtherBenefitId.toString,
        AnyOtherBenefitsId.toString
      )))
    }

  private def anyCompanyBenefits(value: JsValue, cacheMap: CacheMap): CacheMap =
    if (value.as[Boolean]) {
      store(AnyCompanyBenefitsId.toString, value, cacheMap)
    } else {
      store(AnyCompanyBenefitsId.toString, value, cacheMap.copy(data = cacheMap.data - (
        SelectCompanyBenefitsId.toString,
        HowMuchCarBenefitsId.toString,
        HowMuchFuelBenefitId.toString,
        HowMuchMedicalBenefitsId.toString,
        OtherCompanyBenefitId.toString,
        AnyOtherCompanyBenefitsId.toString
      )))
    }

  private def anyTaxableIncome(value: JsValue, cacheMap: CacheMap): CacheMap =
    if (value.as[Boolean]) {
      store(AnyTaxableIncomeId.toString, value, cacheMap)
    } else {
      store(AnyTaxableIncomeId.toString, value, cacheMap.copy(data = cacheMap.data - (
        SelectTaxableIncomeId.toString,
        HowMuchRentalIncomeId.toString,
        AnyTaxableRentalIncomeId.toString,
        HowMuchBankInterestId.toString,
        AnyTaxableBankInterestId.toString,
        HowMuchInvestmentsId.toString,
        AnyTaxableInvestmentsId.toString,
        HowMuchForeignIncomeId.toString,
        AnyTaxableForeignIncomeId.toString,
        OtherTaxableIncomeId.toString,
        AnyTaxableOtherIncomeId.toString,
        AnyOtherTaxableIncomeId.toString
      )))
    }

  private def storeBenefit(selectedBenefits: JsValue, cacheMap: CacheMap): CacheMap = {

    val mapToStore = cacheMap.data.get(SelectBenefitsId.toString).map {
      _.as[JsArray].value.foldLeft(cacheMap) {
        (cm, benefit) =>
          if (!selectedBenefits.as[JsArray].value.contains(benefit) && benefit != JsString(Benefits.OTHER_TAXABLE_BENEFIT.toString)) {
            cm copy (data = cm.data - Benefits.getId(benefit.as[String]).toString)
          } else if (!selectedBenefits.as[JsArray].value.contains(JsString(Benefits.OTHER_TAXABLE_BENEFIT.toString))) {
            cm copy (data = cm.data - (OtherBenefitId.toString, AnyOtherBenefitsId.toString))
          } else {
            cm
          }
      }
    }.getOrElse(cacheMap)

    store(SelectBenefitsId.toString, selectedBenefits, mapToStore)
  }

  private def storeCompanyBenefit(selectedBenefits: JsValue, cacheMap: CacheMap): CacheMap = {

    val mapToStore = cacheMap.data.get(SelectCompanyBenefitsId.toString).map {
      _.as[JsArray].value.foldLeft(cacheMap) {
        (cm, benefit) =>
          if (!selectedBenefits.as[JsArray].value.contains(benefit) && benefit != JsString(CompanyBenefits.OTHER_COMPANY_BENEFIT.toString)) {
            cm copy (data = cm.data - CompanyBenefits.getId(benefit.as[String]).toString)
          } else if (!selectedBenefits.as[JsArray].value.contains(JsString(CompanyBenefits.OTHER_COMPANY_BENEFIT.toString))) {
            cm copy (data = cm.data - (OtherCompanyBenefitId.toString, AnyOtherCompanyBenefitsId.toString))
          } else {
            cm
          }
      }
    }.getOrElse(cacheMap)

    store(SelectCompanyBenefitsId.toString, selectedBenefits, mapToStore)
  }

  private def storeTaxableIncome(selectedBenefits: JsValue, cacheMap: CacheMap): CacheMap = {

    val mapToStore = cacheMap.data.get(SelectTaxableIncomeId.toString).map {
      _.as[JsArray].value.foldLeft(cacheMap) {
        (cm, benefit) =>
          if (!selectedBenefits.as[JsArray].value.contains(benefit) && benefit != JsString(TaxableIncome.OTHER_TAXABLE_INCOME.toString)) {
            cm copy (data = cm.data - (TaxableIncome.getId(benefit.as[String])._1.toString, TaxableIncome.getId(benefit.as[String])._2.toString))
          } else if (!selectedBenefits.as[JsArray].value.contains(JsString(TaxableIncome.OTHER_TAXABLE_INCOME.toString))) {
            cm copy (data = cm.data - (OtherTaxableIncomeId.toString, AnyTaxableOtherIncomeId.toString, AnyOtherTaxableIncomeId.toString))
          } else {
            cm
          }
      }
    }.getOrElse(cacheMap)

    store(SelectTaxableIncomeId.toString, selectedBenefits, mapToStore)
  }

  private def claimDetails(value: JsValue, cacheMap: CacheMap): CacheMap = {
    val keysToRemove = Seq[String](
      EnterPayeReferenceId.toString,
      DetailsOfEmploymentOrPensionId.toString
    )
    val mapToStore = value match {
      case JsBoolean(true) => cacheMap copy (data = cacheMap.data.filterKeys(s => !keysToRemove.contains(s)))
      case _ => cacheMap
    }
    store(EmploymentDetailsId.toString, value, mapToStore)
  }

  //Payment details

  private def whereToSendPayment(value: JsValue, cacheMap: CacheMap): CacheMap = {
    if (cacheMap.data.contains(WhereToSendPaymentId.toString) && value != cacheMap.data(WhereToSendPaymentId.toString)) {
      val mapToStore = value match {
        case JsString("myself") =>
          cacheMap copy (data = cacheMap.data - (NomineeFullNameId.toString,
            AnyAgentRefId.toString,
            AgentRefId.toString,
            IsPaymentAddressInTheUKId.toString,
            PaymentLookupAddressId.toString,
            PaymentUKAddressId.toString,
            PaymentInternationalAddressId.toString,
            PaymentAddressCorrectId.toString))
        case JsString("nominee") =>
          cacheMap copy (data = cacheMap.data - (PaymentAddressCorrectId.toString,
            IsPaymentAddressInTheUKId.toString,
            PaymentUKAddressId.toString,
            PaymentInternationalAddressId.toString,
            PaymentAddressCorrectId.toString))
        case _ => cacheMap
      }
      store(WhereToSendPaymentId.toString, value, mapToStore)
    } else {
      store(WhereToSendPaymentId.toString, value, cacheMap)
    }
  }


  private def paymentAddressCorrect(value: JsValue, cacheMap: CacheMap): CacheMap = {
    val keysToRemoveYes = Seq[String](
      IsPaymentAddressInTheUKId.toString,
      PaymentLookupAddressId.toString,
      PaymentUKAddressId.toString,
      PaymentInternationalAddressId.toString
    )
    val mapToStore = value match {
      case JsBoolean(true) => cacheMap copy (data = cacheMap.data.filterKeys(s => !keysToRemoveYes.contains(s)))
      case _ => cacheMap
    }
    store(PaymentAddressCorrectId.toString, value, mapToStore)
  }

  private def isPaymentAddressInTheUK(value: JsValue, cacheMap: CacheMap): CacheMap = {
    val mapToStore = value match {
      case JsBoolean(true) => cacheMap copy (data = cacheMap.data.filterKeys(s => !PaymentInternationalAddressId.toString.contains(s)))
      case JsBoolean(false) => cacheMap copy (data = cacheMap.data.filterKeys(s => !PaymentUKAddressId.toString.contains(s)))
      case _ => cacheMap
    }
    store(IsPaymentAddressInTheUKId.toString, value, mapToStore)
  }

  private def removeOtherBenefit(value: JsValue, cacheMap: CacheMap): CacheMap = {
    val mapToStore = value match {
      case JsBoolean(false) =>
        val cm = cacheMap.data(SelectBenefitsId.toString).as[Seq[JsValue]].dropRight(1)
        if(cm.isEmpty){
          val newCacheMap = cacheMap copy (data = cacheMap.data - (SelectBenefitsId.toString, OtherBenefitId.toString, AnyBenefitsId.toString, AnyOtherBenefitsId.toString))
          store(AnyBenefitsId.toString, false, newCacheMap)
        } else {
          val newCacheMap = cacheMap copy (data = cacheMap.data - (OtherBenefitId.toString, AnyOtherBenefitsId.toString))
          store(SelectBenefitsId.toString, cm, newCacheMap)
        }
      case _ => cacheMap
    }
    store(RemoveOtherSelectedOptionId.toString, value, mapToStore)
  }

  private def removeOtherCompanyBenefit(value: JsValue, cacheMap: CacheMap): CacheMap = {
    val mapToStore = value match {
      case JsBoolean(false) =>
        val cm = cacheMap.data(SelectCompanyBenefitsId.toString).as[Seq[JsValue]].dropRight(1)
        if(cm.isEmpty){
          val newCacheMap = cacheMap copy (data = cacheMap.data - (SelectCompanyBenefitsId.toString, OtherCompanyBenefitId.toString, AnyCompanyBenefitsId.toString, AnyOtherCompanyBenefitsId.toString))
          store(AnyCompanyBenefitsId.toString, false, newCacheMap)
        } else {
          val newCacheMap = cacheMap copy (data = cacheMap.data - (OtherCompanyBenefitId.toString, AnyOtherCompanyBenefitsId.toString))
          store(SelectCompanyBenefitsId.toString, cm, newCacheMap)
        }
      case _ => cacheMap
    }
    store(RemoveOtherSelectedOptionId.toString, value, mapToStore)
  }

  private def removeOtherTaxableIncome(value: JsValue, cacheMap: CacheMap): CacheMap = {
    val mapToStore = value match {
      case JsBoolean(false) =>
        val cm = cacheMap.data(SelectTaxableIncomeId.toString).as[Seq[JsValue]].dropRight(1)
        if(cm.isEmpty){
          val newCacheMap = cacheMap copy (data = cacheMap.data - (SelectTaxableIncomeId.toString, OtherTaxableIncomeId.toString, AnyTaxableIncomeId.toString, AnyOtherTaxableIncomeId.toString))
          store(AnyTaxableIncomeId.toString, false, newCacheMap)
        } else {
          val newCacheMap = cacheMap copy (data = cacheMap.data - (OtherTaxableIncomeId.toString, AnyOtherTaxableIncomeId.toString))
          store(SelectTaxableIncomeId.toString, cm, newCacheMap)
        }
      case _ => cacheMap
    }
    store(RemoveOtherSelectedOptionId.toString, value, mapToStore)
  }

}

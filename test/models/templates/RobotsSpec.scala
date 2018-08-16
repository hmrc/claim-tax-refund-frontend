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
import org.scalatest.mockito.MockitoSugar
import play.api.Logger
import play.twirl.api.XmlFormat
import utils.{MockUserAnswers, RobotsXmlHelper, UserAnswers, WireMockHelper}

import scala.xml.XML._
import scala.xml._

class RobotsSpec extends  SpecBase with WireMockHelper with MockitoSugar {
  val fullUserAnswers: UserAnswers = MockUserAnswers.fullValidUserAnswers
  val fullXml: XmlFormat.Appendable = robots(fullUserAnswers, new RobotsXmlHelper(fullUserAnswers)(messages))
  val xmlToNode: Elem = loadString(fullXml.toString.replaceAll("\\s", "")) // TODO this needs to be removed once XML refactor is complete (remove whitespace)

  "robots Xml" must {

    "have correct sections in userDetails" in {

      val nodeSeq: NodeSeq = xmlToNode \\ "userDetails"

      val name: NodeSeq = nodeSeq.\("name").head
      val nino: NodeSeq = nodeSeq.\("nino").head

      name mustBe <name>TestName</name>
      nino mustBe <nino>ZZ123456A</nino>
    }

    //TODO make false version of this (with PAYE reference)

    "have correct sections in claimSection when employmentDetails are true" in {

      val nodeSeq: NodeSeq = xmlToNode \\ "claimSection"

      val selectedTaxYear: Node = nodeSeq.\("selectedTaxYear").head
      val employmentDetails: Node = nodeSeq.\("employmentDetails").head

      selectedTaxYear mustBe <selectedTaxYear>6April2016to5April2017</selectedTaxYear>
      employmentDetails mustBe <employmentDetails>true</employmentDetails>
    }

    "have correct sections in benefitSection" in {

      val nodeSeq: NodeSeq = xmlToNode \\ "benefitSection"

      val anyBenefits = nodeSeq.\("anyBenefits").head
      val selectBenefits = nodeSeq.\("selectBenefits").head
      val howMuchBereavementAllowance = nodeSeq.\("howMuchBereavementAllowance").head
      val howMuchCarersAllowance = nodeSeq.\("howMuchCarersAllowance").head
      val howMuchJobseekersAllowance = nodeSeq.\("howMuchJobseekersAllowance").head
      val howMuchEmploymentAndSupportAllowance = nodeSeq.\("howMuchEmploymentAndSupportAllowance").head
      val howMuchIncapacityBenefit = nodeSeq.\("howMuchIncapacityBenefit").head
      val howMuchStatePension = nodeSeq.\("howMuchStatePension").head
      val otherBenefits = nodeSeq.\("otherBenefitsSection").\("otherTaxableIncome").\("otherBenefit")

      anyBenefits mustBe <anyBenefits>true</anyBenefits>
      selectBenefits mustBe <selectBenefits>carers-allowancebereavement-allowanceincapacity-benefitemployment-and-support-allowancejobseekers-allowanceother-taxable-benefitstate-pension</selectBenefits>
      howMuchBereavementAllowance mustBe <howMuchBereavementAllowance>1234</howMuchBereavementAllowance>
      howMuchCarersAllowance mustBe <howMuchCarersAllowance>1234</howMuchCarersAllowance>
      howMuchJobseekersAllowance mustBe <howMuchJobseekersAllowance>1234</howMuchJobseekersAllowance>
      howMuchEmploymentAndSupportAllowance mustBe <howMuchEmploymentAndSupportAllowance>1234</howMuchEmploymentAndSupportAllowance>
      howMuchIncapacityBenefit mustBe <howMuchIncapacityBenefit>1234</howMuchIncapacityBenefit>
      howMuchStatePension mustBe <howMuchStatePension>1234</howMuchStatePension>

      otherBenefits.head mustBe <otherBenefit><name>qwerty</name><amount>12</amount></otherBenefit>
      otherBenefits(1) mustBe <otherBenefit><name>qwerty1</name><amount>34</amount></otherBenefit>
      otherBenefits(2) mustBe <otherBenefit><name>qwerty2</name><amount>56</amount></otherBenefit>
    }

    "have correct sections in companyBenefitSection" in {

      val nodeSeq: NodeSeq = xmlToNode \\ "companyBenefitsSection"

      val anyCompanyBenefits = nodeSeq.\("anyCompanyBenefits").head
      val selectCompanyBenefits = nodeSeq.\("selectCompanyBenefits").head
      val howMuchCarBenefits = nodeSeq.\("howMuchCarBenefits").head
      val howMuchFuelBenefit = nodeSeq.\("howMuchFuelBenefit").head
      val howMuchMedicalBenefits = nodeSeq.\("howMuchMedicalBenefits").head
      val otherCompanyBenefitsSection = nodeSeq.\("otherCompanyBenefitsSection").\("otherTaxableIncome").\("companyBenefit")


      otherCompanyBenefitsSection.head mustBe <companyBenefit><name>qwerty</name><amount>12</amount></companyBenefit>
      otherCompanyBenefitsSection(1) mustBe <companyBenefit><name>qwerty1</name><amount>34</amount></companyBenefit>
      otherCompanyBenefitsSection(2) mustBe <companyBenefit><name>qwerty2</name><amount>56</amount></companyBenefit>


    }

    "have correct sections in taxableIncomeSection" in {

      val nodeSeq: NodeSeq = xmlToNode \\ "taxableIncomeSection"

      val anyTaxableIncome = nodeSeq.\("anyTaxableIncome").head

      val rentalIncomeAmount = nodeSeq.\("rentalIncome").\("howMuch").head
      val rentalIncomeAnyTaxPaid = nodeSeq.\("rentalIncome").\("anyTaxPaid").head
      val rentalIncomeTaxPaidAmount = nodeSeq.\("rentalIncome").\("taxPaid").head

      val bankInterestAmount = nodeSeq.\("bankInterest").\("howMuch").head
      val bankInterestAnyTaxPaid = nodeSeq.\("bankInterest").\("anyTaxPaid").head
      val bankInterestTaxPaidAmount = nodeSeq.\("bankInterest").\("taxPaid").head

      val investmentOrDividendIncomeAmount = nodeSeq.\("investmentOrDividend").\("howMuch").head
      val investmentOrDividendIncomeAnyTaxPaid = nodeSeq.\("investmentOrDividend").\("anyTaxPaid").head
      val investmentOrDividendIncomeTaxPaidAmount = nodeSeq.\("investmentOrDividend").\("taxPaid").head

      val foreignIncomeIncomeAmount = nodeSeq.\("foreignIncome").\("howMuch").head
      val foreignIncomeIncomeAnyTaxPaid = nodeSeq.\("foreignIncome").\("anyTaxPaid").head
      val foreignIncomeIncomeTaxPaidAmount = nodeSeq.\("foreignIncome").\("taxPaid").head

      anyTaxableIncome mustBe <anyTaxableIncome>true</anyTaxableIncome>

      rentalIncomeAmount mustBe <howMuch>1234</howMuch>
      rentalIncomeAnyTaxPaid mustBe <anyTaxPaid>Yes</anyTaxPaid>
      rentalIncomeTaxPaidAmount mustBe <taxPaid>123</taxPaid>

      bankInterestAmount mustBe <howMuch>1234</howMuch>
      bankInterestAnyTaxPaid mustBe <anyTaxPaid>Yes</anyTaxPaid>
      bankInterestTaxPaidAmount mustBe <taxPaid>123</taxPaid>

      investmentOrDividendIncomeAmount mustBe <howMuch>1234</howMuch>
      investmentOrDividendIncomeAnyTaxPaid mustBe <anyTaxPaid>Yes</anyTaxPaid>
      investmentOrDividendIncomeTaxPaidAmount mustBe <taxPaid>123</taxPaid>

      foreignIncomeIncomeAmount mustBe <howMuch>1234</howMuch>
      foreignIncomeIncomeAnyTaxPaid mustBe <anyTaxPaid>Yes</anyTaxPaid>
      foreignIncomeIncomeTaxPaidAmount mustBe <taxPaid>123</taxPaid>

    }
  }
}

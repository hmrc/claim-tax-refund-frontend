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
import models.UkAddress
import models.templates.xml.robots
import org.scalatest.mockito.MockitoSugar
import play.twirl.api.XmlFormat
import utils.{MockUserAnswers, RobotsXmlHelper, UserAnswers, WireMockHelper}
import org.mockito.Mockito.when

import scala.xml.XML._
import scala.xml._

class RobotsSpec extends  SpecBase with WireMockHelper with MockitoSugar {

  val fullUserAnswers: UserAnswers = MockUserAnswers.fullValidUserAnswers
  val fullXml: XmlFormat.Appendable = robots(fullUserAnswers, new RobotsXmlHelper(fullUserAnswers)(messages))
  val xmlToNode: Elem = loadString(fullXml.toString)

  def getXpath(elementSectionKey: String, elementKey: String, xmlToNode: Elem = xmlToNode): Node = {

      val nodeSeq = xmlToNode \\ elementSectionKey
      nodeSeq.\(elementKey).head
  }

  "robots Xml" must {

    "have correct sections in userDetails" in {

      getXpath("userDetails", "name") mustBe <name>TestName</name>
      getXpath("userDetails", "nino") mustBe <nino>ZZ123456A</nino>
    }

    "have correct sections in claimSection when employmentDetails are true" in {

      getXpath("claimSection", "selectedTaxYear") mustBe <selectedTaxYear>6 April 2016 to 5 April 2017</selectedTaxYear>
      getXpath("claimSection", "employmentDetails") mustBe <employmentDetails>true</employmentDetails>
    }

    "have correct sections in claimSection when employmentDetails are false" in {
      val fullUserAnswers: UserAnswers = MockUserAnswers.fullValidUserAnswers

      when(fullUserAnswers.employmentDetails) thenReturn Some(false)
      when(fullUserAnswers.enterPayeReference) thenReturn Some("123456789")
      when(fullUserAnswers.detailsOfEmploymentOrPension) thenReturn Some("Employment details")

      val fullXml: XmlFormat.Appendable = robots(fullUserAnswers, new RobotsXmlHelper(fullUserAnswers)(messages))
      val newXmlToNode: Elem = loadString(fullXml.toString)

      getXpath("claimSection", "selectedTaxYear", newXmlToNode) mustBe <selectedTaxYear>6 April 2016 to 5 April 2017</selectedTaxYear>
      getXpath("claimSection", "employmentDetails", newXmlToNode) mustBe <employmentDetails>false</employmentDetails>
      getXpath("claimSection", "payeReference", newXmlToNode) mustBe <payeReference>123456789</payeReference>
      getXpath("claimSection", "detailsOfEmploymentOrPension", newXmlToNode) mustBe <detailsOfEmploymentOrPension>Employment details</detailsOfEmploymentOrPension>

    }

    "have correct sections in benefitSection" in {

      getXpath("benefitSection", "anyBenefits") mustBe <anyBenefits>true</anyBenefits>
      getXpath("benefitSection" ,"selectBenefits") mustBe <selectBenefits>carers-allowance, bereavement-allowance, incapacity-benefit, employment-and-support-allowance, jobseekers-allowance, other-taxable-benefit, state-pension</selectBenefits>
      getXpath("benefitSection", "howMuchBereavementAllowance") mustBe <howMuchBereavementAllowance>1234</howMuchBereavementAllowance>
      getXpath("benefitSection", "howMuchCarersAllowance") mustBe <howMuchCarersAllowance>1234</howMuchCarersAllowance>
      getXpath("benefitSection", "howMuchJobseekersAllowance") mustBe <howMuchJobseekersAllowance>1234</howMuchJobseekersAllowance>
      getXpath("benefitSection", "howMuchEmploymentAndSupportAllowance") mustBe <howMuchEmploymentAndSupportAllowance>1234</howMuchEmploymentAndSupportAllowance>
      getXpath("benefitSection", "howMuchIncapacityBenefit") mustBe <howMuchIncapacityBenefit>1234</howMuchIncapacityBenefit>
      getXpath("benefitSection", "howMuchStatePension") mustBe <howMuchStatePension>1234</howMuchStatePension>

      val otherBenefits = getXpath("otherBenefitsSection", "otherTaxableIncome").\("otherBenefit")

      otherBenefits.head mustBe <otherBenefit><name>qwerty</name><amount>12</amount></otherBenefit>
      otherBenefits(1) mustBe <otherBenefit><name>qwerty1</name><amount>34</amount></otherBenefit>
      otherBenefits(2) mustBe <otherBenefit><name>qwerty2</name><amount>56</amount></otherBenefit>
    }

    "have correct sections in companyBenefitSection" in {

      getXpath("companyBenefitsSection", "anyCompanyBenefits") mustBe <anyCompanyBenefits>true</anyCompanyBenefits>
      getXpath("companyBenefitsSection", "selectCompanyBenefits") mustBe <selectCompanyBenefits>company-car-benefit, medical-benefit, fuel-benefit, other-company-benefit</selectCompanyBenefits>
      getXpath("companyBenefitsSection", "howMuchCarBenefits") mustBe <howMuchCarBenefits>1234</howMuchCarBenefits>
      getXpath("companyBenefitsSection", "howMuchFuelBenefit") mustBe <howMuchFuelBenefit>1234</howMuchFuelBenefit>
      getXpath("companyBenefitsSection", "howMuchMedicalBenefits") mustBe <howMuchMedicalBenefits>1234</howMuchMedicalBenefits>

      val otherCompanyBenefitsSection = getXpath("otherCompanyBenefitsSection", "otherTaxableIncome").\("companyBenefit")

      otherCompanyBenefitsSection.head mustBe <companyBenefit><name>qwerty</name><amount>12</amount></companyBenefit>
      otherCompanyBenefitsSection(1) mustBe <companyBenefit><name>qwerty1</name><amount>34</amount></companyBenefit>
      otherCompanyBenefitsSection(2) mustBe <companyBenefit><name>qwerty2</name><amount>56</amount></companyBenefit>

    }

    "have correct sections in taxableIncomeSection" in {

      getXpath("taxableIncomeSection", "anyTaxableIncome") mustBe <anyTaxableIncome>true</anyTaxableIncome>
      getXpath("taxableIncomeSection", "selectTaxableIncome") mustBe <selectTaxableIncome>rental-income, bank-or-building-society-interest, investment-or-dividends, foreign-income, other-taxable-income</selectTaxableIncome>

      getXpath("taxableIncomeSection", "rentalIncome").\("howMuch").head mustBe <howMuch>1234</howMuch>
      getXpath("taxableIncomeSection", "rentalIncome").\("anyTaxPaid").head mustBe <anyTaxPaid>Yes</anyTaxPaid>
      getXpath("taxableIncomeSection", "rentalIncome").\("taxPaid").head mustBe <taxPaid>123</taxPaid>

      getXpath("taxableIncomeSection", "bankInterest").\("howMuch").head mustBe <howMuch>1234</howMuch>
      getXpath("taxableIncomeSection", "bankInterest").\("anyTaxPaid").head mustBe <anyTaxPaid>Yes</anyTaxPaid>
      getXpath("taxableIncomeSection", "bankInterest").\("taxPaid").head mustBe <taxPaid>123</taxPaid>

      getXpath("taxableIncomeSection", "investmentOrDividend").\("howMuch").head mustBe <howMuch>1234</howMuch>
      getXpath("taxableIncomeSection", "investmentOrDividend").\("anyTaxPaid").head mustBe <anyTaxPaid>Yes</anyTaxPaid>
      getXpath("taxableIncomeSection", "investmentOrDividend").\("taxPaid").head mustBe <taxPaid>123</taxPaid>

      getXpath("taxableIncomeSection", "foreignIncome").\("howMuch").head mustBe <howMuch>1234</howMuch>
      getXpath("taxableIncomeSection", "foreignIncome").\("anyTaxPaid").head mustBe <anyTaxPaid>Yes</anyTaxPaid>
      getXpath("taxableIncomeSection", "foreignIncome").\("taxPaid").head mustBe <taxPaid>123</taxPaid>

    }

    "have correct sections in the paymentSection when payment address is international" in {

      getXpath("paymentSection", "whereToSendThePayment") mustBe <whereToSendThePayment>nominee</whereToSendThePayment>
      getXpath("paymentSection", "nomineeFullname") mustBe <nomineeFullname>Nominee</nomineeFullname>
      getXpath("paymentSection", "anyAgentRef") mustBe <anyAgentRef>Yes</anyAgentRef>
      getXpath("paymentSection", "agentReference") mustBe <agentReference>12341234</agentReference>
      getXpath("paymentSection", "isPaymentAddressInTheUK") mustBe <isPaymentAddressInTheUK>false</isPaymentAddressInTheUK>
      getXpath("paymentSection", "paymentAddress") mustBe <paymentAddress><internationalAddress>1, 2, Country</internationalAddress></paymentAddress>
    }

    "have correct sections in the paymentSection when payment address is in the UK" in {

      val fullUserAnswers: UserAnswers = MockUserAnswers.fullValidUserAnswers

      when(fullUserAnswers.isPaymentAddressInTheUK) thenReturn Some(true)
      when(fullUserAnswers.paymentInternationalAddress) thenReturn None
      when(fullUserAnswers.paymentUKAddress) thenReturn Some(UkAddress("qwerty", "qwerty1", None, None, None, "AB1 0CD"))

      val fullXml: XmlFormat.Appendable = robots(fullUserAnswers, new RobotsXmlHelper(fullUserAnswers)(messages))
      val newXmlToNode: Elem = loadString(fullXml.toString)

      getXpath("paymentSection", "whereToSendThePayment", newXmlToNode) mustBe <whereToSendThePayment>nominee</whereToSendThePayment>
      getXpath("paymentSection", "nomineeFullname", newXmlToNode) mustBe <nomineeFullname>Nominee</nomineeFullname>
      getXpath("paymentSection", "anyAgentRef", newXmlToNode) mustBe <anyAgentRef>Yes</anyAgentRef>
      getXpath("paymentSection", "agentReference", newXmlToNode) mustBe <agentReference>12341234</agentReference>
      getXpath("paymentSection", "isPaymentAddressInTheUK", newXmlToNode) mustBe <isPaymentAddressInTheUK>true</isPaymentAddressInTheUK>
      getXpath("paymentSection", "paymentAddress", newXmlToNode) mustBe <paymentAddress><ukAddress>qwerty, qwerty1, AB1 0CD</ukAddress></paymentAddress>
    }

    "have the correct parts in contact section" in {

      getXpath("contactSection", "anyTelephoneNumber") mustBe  <anyTelephoneNumber>Yes</anyTelephoneNumber>
      getXpath("contactSection", "telephoneNumber") mustBe <telephoneNumber>0191123123</telephoneNumber>
    }
  }
}

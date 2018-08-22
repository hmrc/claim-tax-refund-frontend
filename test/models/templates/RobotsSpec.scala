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
import models.{Address, AddressLookup, Country, UkAddress}
import org.scalatest.mockito.MockitoSugar
import utils.{MockUserAnswers, RobotsXmlHelper, UserAnswers, WireMockHelper}
import org.mockito.Mockito.when
import play.api.Logger

import scala.xml.XML._
import scala.xml._

class RobotsSpec extends  SpecBase with WireMockHelper with MockitoSugar {

  private val fullUserAnswers: UserAnswers = MockUserAnswers.fullValidUserAnswers
  private val fullXml: Elem = new RobotsXmlHelper(fullUserAnswers)(messages).formattedXml

  private val validMinimalXml: Elem = <ctr><userDetails><name>TestName</name><nino>ZZ123456A</nino></userDetails><claimSection><selectedTaxYear>6 April 2016 to 5 April 2017</selectedTaxYear><employmentDetails>true</employmentDetails></claimSection><paymentSection><whereToSendThePayment>myself</whereToSendThePayment><paymentAddressCorrect>true</paymentAddressCorrect><paymentAddress/></paymentSection><contactSection><anyTelephoneNumber>No</anyTelephoneNumber><telephoneNumber/></contactSection></ctr>

  def getXpath(elementSectionKey: String, elementKey: String, fullXml: Elem = fullXml): Node = {

      val nodeSeq = fullXml \\ elementSectionKey
      nodeSeq.\(elementKey).head
  }

  "robots Xml" must {

    "have correct sections in userDetails" in {

      fullXml \ "userDetails" \ "name" must contain(<name>TestName</name>)
      fullXml \ "userDetails" \ "nino" must contain(<nino>ZZ123456A</nino>)
    }

    "have correct sections in claimSection when employmentDetails are true" in {

      getXpath(elementSectionKey = "claimSection", elementKey = "selectedTaxYear") mustBe <selectedTaxYear>6 April 2016 to 5 April 2017</selectedTaxYear>
      getXpath(elementSectionKey = "claimSection", elementKey = "employmentDetails") mustBe <employmentDetails>true</employmentDetails>
    }

    "have correct sections in claimSection when employmentDetails are false" in {
      val fullUserAnswers: UserAnswers = MockUserAnswers.fullValidUserAnswers

      when(fullUserAnswers.employmentDetails) thenReturn Some(false)
      when(fullUserAnswers.enterPayeReference) thenReturn Some("123456789")
      when(fullUserAnswers.detailsOfEmploymentOrPension) thenReturn Some("Employment details")

      val newXmlToNode: Elem = new RobotsXmlHelper(fullUserAnswers)(messages).formattedXml

      getXpath(elementSectionKey = "claimSection", elementKey = "selectedTaxYear", newXmlToNode) mustBe <selectedTaxYear>6 April 2016 to 5 April 2017</selectedTaxYear>
      getXpath(elementSectionKey = "claimSection", elementKey = "employmentDetails", newXmlToNode) mustBe <employmentDetails>false</employmentDetails>
      getXpath(elementSectionKey = "claimSection", elementKey = "payeReference", newXmlToNode) mustBe <payeReference>123456789</payeReference>
      getXpath(elementSectionKey = "claimSection", elementKey = "detailsOfEmploymentOrPension", newXmlToNode) mustBe <detailsOfEmploymentOrPension>Employment details</detailsOfEmploymentOrPension>

    }

    "have correct sections in benefitSection" in {

			Logger.warn (s"\n\n\n\n\n$fullXml")
      getXpath(elementSectionKey = "benefitSection", elementKey = "anyBenefits") mustBe <anyBenefits>true</anyBenefits>
      getXpath(elementSectionKey = "benefitSection", elementKey = "selectBenefits") mustBe <selectBenefits>carers-allowance, bereavement-allowance, incapacity-benefit, employment-and-support-allowance, jobseekers-allowance, other-taxable-benefit, state-pension</selectBenefits>
      getXpath(elementSectionKey = "benefitSection", elementKey = "howMuchBereavementAllowance") mustBe <howMuchBereavementAllowance>1234</howMuchBereavementAllowance>
      getXpath(elementSectionKey = "benefitSection", elementKey = "howMuchCarersAllowance") mustBe <howMuchCarersAllowance>1234</howMuchCarersAllowance>
      getXpath(elementSectionKey = "benefitSection", elementKey = "howMuchJobseekersAllowance") mustBe <howMuchJobseekersAllowance>1234</howMuchJobseekersAllowance>
      getXpath(elementSectionKey = "benefitSection", elementKey = "howMuchEmploymentAndSupportAllowance") mustBe <howMuchEmploymentAndSupportAllowance>1234</howMuchEmploymentAndSupportAllowance>
      getXpath(elementSectionKey = "benefitSection", elementKey = "howMuchIncapacityBenefit") mustBe <howMuchIncapacityBenefit>1234</howMuchIncapacityBenefit>
      getXpath(elementSectionKey = "benefitSection", elementKey = "howMuchStatePension") mustBe <howMuchStatePension>1234</howMuchStatePension>

      val otherBenefits: NodeSeq = fullXml \\ "otherBenefits" \ "otherBenefit"

      otherBenefits.head mustBe <otherBenefit><name>qwerty</name><amount>12</amount></otherBenefit>
      otherBenefits(1) mustBe <otherBenefit><name>qwerty1</name><amount>34</amount></otherBenefit>
      otherBenefits(2) mustBe <otherBenefit><name>qwerty2</name><amount>56</amount></otherBenefit>
    }

    "have correct sections in companyBenefitSection" in {

      getXpath(elementSectionKey = "companyBenefitsSection", elementKey = "anyCompanyBenefits") mustBe <anyCompanyBenefits>true</anyCompanyBenefits>
      getXpath(elementSectionKey = "companyBenefitsSection", elementKey = "selectCompanyBenefits") mustBe <selectCompanyBenefits>company-car-benefit, medical-benefit, fuel-benefit, other-company-benefit</selectCompanyBenefits>
      getXpath(elementSectionKey = "companyBenefitsSection", elementKey = "howMuchCarBenefits") mustBe <howMuchCarBenefits>1234</howMuchCarBenefits>
      getXpath(elementSectionKey = "companyBenefitsSection", elementKey = "howMuchFuelBenefit") mustBe <howMuchFuelBenefit>1234</howMuchFuelBenefit>
      getXpath(elementSectionKey = "companyBenefitsSection", elementKey = "howMuchMedicalBenefits") mustBe <howMuchMedicalBenefits>1234</howMuchMedicalBenefits>

      val otherCompanyBenefitsSection = getXpath(elementSectionKey = "otherCompanyBenefitsSection", elementKey = "otherCompanyBenefit").\("companyBenefit")

      otherCompanyBenefitsSection.head mustBe <companyBenefit><name>qwerty</name><amount>12</amount></companyBenefit>
      otherCompanyBenefitsSection(1) mustBe <companyBenefit><name>qwerty1</name><amount>34</amount></companyBenefit>
      otherCompanyBenefitsSection(2) mustBe <companyBenefit><name>qwerty2</name><amount>56</amount></companyBenefit>

    }

    "have correct sections in taxableIncomeSection" in {

      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "anyTaxableIncome") mustBe <anyTaxableIncome>true</anyTaxableIncome>
      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "selectTaxableIncome") mustBe <selectTaxableIncome>rental-income, bank-or-building-society-interest, investment-or-dividends, foreign-income, other-taxable-income</selectTaxableIncome>

      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "rentalIncome").\("howMuch").head mustBe <howMuch>1234</howMuch>
      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "rentalIncome").\("anyTaxPaid").head mustBe <anyTaxPaid>Yes</anyTaxPaid>
      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "rentalIncome").\("taxPaid").head mustBe <taxPaid>123</taxPaid>

      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "bankInterest").\("howMuch").head mustBe <howMuch>1234</howMuch>
      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "bankInterest").\("anyTaxPaid").head mustBe <anyTaxPaid>Yes</anyTaxPaid>
      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "bankInterest").\("taxPaid").head mustBe <taxPaid>123</taxPaid>

      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "investmentOrDividend").\("howMuch").head mustBe <howMuch>1234</howMuch>
      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "investmentOrDividend").\("anyTaxPaid").head mustBe <anyTaxPaid>Yes</anyTaxPaid>
      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "investmentOrDividend").\("taxPaid").head mustBe <taxPaid>123</taxPaid>

      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "foreignIncome").\("howMuch").head mustBe <howMuch>1234</howMuch>
      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "foreignIncome").\("anyTaxPaid").head mustBe <anyTaxPaid>Yes</anyTaxPaid>
      getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "foreignIncome").\("taxPaid").head mustBe <taxPaid>123</taxPaid>

      val otherTaxableIncomeSection = getXpath(elementSectionKey = "taxableIncomeSection", elementKey = "otherTaxableIncome").\("taxableIncome")

      otherTaxableIncomeSection.head mustBe <taxableIncome><name>qwerty</name><amount>12</amount></taxableIncome>
      otherTaxableIncomeSection(1) mustBe <taxableIncome><name>qwerty1</name><amount>34</amount></taxableIncome>
      otherTaxableIncomeSection(2) mustBe <taxableIncome><name>qwerty2</name><amount>56</amount></taxableIncome>

    }

    "hide sections when not selected" in {
      when (fullUserAnswers.anyBenefits) thenReturn Some(false)
      when (fullUserAnswers.anyCompanyBenefits) thenReturn Some(false)
      when (fullUserAnswers.anyTaxableIncome) thenReturn Some(false)
      val newXmlToNode: Elem = new RobotsXmlHelper(fullUserAnswers)(messages).formattedXml

      newXmlToNode.contains(<anyBenefits></anyBenefits>) mustBe false
      newXmlToNode.contains(<anyCompanyBenefits></anyCompanyBenefits>) mustBe false
      newXmlToNode.contains(<anyTaxableIncome></anyTaxableIncome>) mustBe false
    }

    "have correct sections in the paymentSection when payment address is international" in {

      getXpath(elementSectionKey = "paymentSection", elementKey = "whereToSendThePayment") mustBe <whereToSendThePayment>nominee</whereToSendThePayment>
      getXpath(elementSectionKey = "paymentSection", elementKey = "nomineeFullname") mustBe <nomineeFullname>Nominee</nomineeFullname>
      getXpath(elementSectionKey = "paymentSection", elementKey = "anyAgentRef") mustBe <anyAgentRef>Yes</anyAgentRef>
      getXpath(elementSectionKey = "paymentSection", elementKey = "agentReference") mustBe <agentReference>12341234</agentReference>
      getXpath(elementSectionKey = "paymentSection", elementKey = "isPaymentAddressInTheUK") mustBe <isPaymentAddressInTheUK>false</isPaymentAddressInTheUK>
      getXpath(elementSectionKey = "paymentSection", elementKey = "paymentAddress") mustBe <paymentAddress><internationalAddress>1, 2, Country</internationalAddress></paymentAddress>
    }

    "have correct sections in the paymentSection when payment address is in the UK" in {

      when(fullUserAnswers.isPaymentAddressInTheUK) thenReturn Some(true)
      when(fullUserAnswers.paymentInternationalAddress) thenReturn None
      when(fullUserAnswers.paymentUKAddress) thenReturn Some(UkAddress("qwerty", "qwerty1", None, None, None, "AB1 0CD"))

      val newXmlToNode: Elem = new RobotsXmlHelper(fullUserAnswers)(messages).formattedXml

      getXpath(elementSectionKey = "paymentSection", elementKey = "whereToSendThePayment", newXmlToNode) mustBe <whereToSendThePayment>nominee</whereToSendThePayment>
      getXpath(elementSectionKey = "paymentSection", elementKey = "nomineeFullname", newXmlToNode) mustBe <nomineeFullname>Nominee</nomineeFullname>
      getXpath(elementSectionKey = "paymentSection", elementKey = "anyAgentRef", newXmlToNode) mustBe <anyAgentRef>Yes</anyAgentRef>
      getXpath(elementSectionKey = "paymentSection", elementKey = "agentReference", newXmlToNode) mustBe <agentReference>12341234</agentReference>
      getXpath(elementSectionKey = "paymentSection", elementKey = "isPaymentAddressInTheUK", newXmlToNode) mustBe <isPaymentAddressInTheUK>true</isPaymentAddressInTheUK>
      getXpath(elementSectionKey = "paymentSection", elementKey = "paymentAddress", newXmlToNode) mustBe <paymentAddress><ukAddress>qwerty, qwerty1, AB1 0CD</ukAddress></paymentAddress>
    }

    "have correct sections in the paymentSection when payment address is a lookup" in {
      when(fullUserAnswers.isPaymentAddressInTheUK) thenReturn None
      when(fullUserAnswers.paymentInternationalAddress) thenReturn None
      when(fullUserAnswers.paymentLookupAddress) thenReturn Some(AddressLookup(
        address = Some(
          Address(
            lines = Some(Seq("Line1", "Line2", "Line3", "Line4")),
            postcode = Some("NE1 1LX"),
            country = Some(Country(Some("United Kingdom"),Some("GB")))
          )),
        auditRef = Some("e9e2fb3f-268f-4c4c-b928-3dc0b17259f2")
      ))

      val newXmlToNode: Elem = new RobotsXmlHelper(fullUserAnswers)(messages).formattedXml
      getXpath(elementSectionKey = "paymentSection", elementKey = "paymentAddress", newXmlToNode) mustBe <paymentAddress><ukAddress>qwerty, qwerty1, AB1 0CD</ukAddress><lookupAddress>Line1, Line2, Line3, Line4, NE1 1LX, United Kingdom, GB</lookupAddress></paymentAddress>
    }

    "have the correct parts in contact section" in {

      getXpath(elementSectionKey = "contactSection", elementKey = "anyTelephoneNumber") mustBe  <anyTelephoneNumber>Yes</anyTelephoneNumber>
      getXpath(elementSectionKey = "contactSection", elementKey = "telephoneNumber") mustBe <telephoneNumber>0191123123</telephoneNumber>
    }

    "minimal valid answers must form correctly" in {
      val xml: Elem = new RobotsXmlHelper(MockUserAnswers.minimalValidUserAnswers)(messages).formattedXml

      xml mustBe validMinimalXml
    }
  }
}

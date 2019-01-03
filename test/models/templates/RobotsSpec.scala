/*
 * Copyright 2019 HM Revenue & Customs
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
import models._
import org.mockito.Mockito.when
import uk.gov.hmrc.auth.core.retrieve.{ItmpAddress, ItmpName}
import utils.{MockUserAnswers, UserAnswers}

import scala.xml.Utility._
import scala.xml._

class RobotsSpec extends SpecBase {
  val robotXML = new RobotXML

  private val fullUserAnswers: UserAnswers = MockUserAnswers.fullValidUserAnswers

  private val submissionReference: String = "1234"
  private val timeStamp: String = "1234567890"
  private val name: ItmpName = ItmpName(Some("TestName"),Some("TestMiddleName"),Some("TestLastName"))
  private val address: ItmpAddress = ItmpAddress(
    Some("Address line 1"),
    Some("Address line 2"),
    Some("Address line 3"),
    Some("Address line 4"),
    Some("Address line 5"),
    Some("ZZ11ZZ"),
    Some("United Kingdom"),
    Some("GB")
  )
  private val nino: String = "ZZ123456A"

  private val fullXml: Node =
    robotXML.generateXml(fullUserAnswers, submissionReference, timeStamp, nino, name, address)(messages)

  private def formatXml(userAnswers: UserAnswers): Node =
    robotXML.generateXml(userAnswers, submissionReference, timeStamp, nino, name, address)(messages)

  private val validMinimalXml: Node =
    trim(
      <ctr>
        <submissionReference>1234</submissionReference>
        <dateCreated>1234567890</dateCreated>
        <userDetails>
          <firstName>TestName</firstName>
          <middleName>TestMiddleName</middleName>
          <lastName>TestLastName</lastName>
          <nino>ZZ123456A</nino>
          <itmpAddress>
            <addressLine1>Address line 1</addressLine1>
            <addressLine2>Address line 2</addressLine2>
            <addressLine3>Address line 3</addressLine3>
            <addressLine4>Address line 4</addressLine4>
            <addressLine5>Address line 5</addressLine5>
            <postcode>ZZ11ZZ</postcode>
            <country>United Kingdom</country>
            <countryCode>GB</countryCode>
          </itmpAddress>
        </userDetails>
        <claimSection>
          <selectedTaxYear>6 April 2016 to 5 April 2017</selectedTaxYear>
          <employmentDetails>true</employmentDetails>
        </claimSection>
        <benefitSection>
          <anyBenefits>false</anyBenefits>
        </benefitSection>
        <companyBenefitsSection>
          <anyCompanyBenefits>false</anyCompanyBenefits>
        </companyBenefitsSection>
        <taxableIncomeSection>
          <anyTaxableIncome>false</anyTaxableIncome>
        </taxableIncomeSection>
        <paymentSection>
          <whereToSendThePayment>myself</whereToSendThePayment>
          <paymentAddressCorrect>true</paymentAddressCorrect>
        </paymentSection>
        <contactSection>
          <anyTelephoneNumber>No</anyTelephoneNumber>
        </contactSection>
      </ctr>
    )

  "robots Xml" must {

    "contain submission reference and time stamp" in {

      fullXml \ "submissionReference" must contain(<submissionReference>1234</submissionReference>)
      fullXml \ "dateCreated" must contain(<dateCreated>1234567890</dateCreated>)
    }

    "have correct sections in userDetails" in {
      fullXml \ "userDetails" \ "firstName" must contain(<firstName>TestName</firstName>)
      fullXml \ "userDetails" \ "middleName" must contain(<middleName>TestMiddleName</middleName>)
      fullXml \ "userDetails" \ "lastName" must contain(<lastName>TestLastName</lastName>)
      fullXml \ "userDetails" \ "nino" must contain(<nino>ZZ123456A</nino>)
      fullXml \ "userDetails" \ "itmpAddress" must contain(
        trim(
          <itmpAddress>
            <addressLine1>Address line 1</addressLine1>
            <addressLine2>Address line 2</addressLine2>
            <addressLine3>Address line 3</addressLine3>
            <addressLine4>Address line 4</addressLine4>
            <addressLine5>Address line 5</addressLine5>
            <postcode>ZZ11ZZ</postcode>
            <country>United Kingdom</country>
            <countryCode>GB</countryCode>
          </itmpAddress>
        )
      )
    }

    "have correct sections in claimSection when employmentDetails are true" in {
      fullXml \ "claimSection" \ "selectedTaxYear" must contain(<selectedTaxYear>6 April 2016 to 5 April 2017</selectedTaxYear>)
      fullXml \ "claimSection" \ "employmentDetails" must contain(<employmentDetails>true</employmentDetails>)
    }

    "have correct sections in claimSection when employmentDetails are false" in {

      when(fullUserAnswers.employmentDetails) thenReturn Some(false)
      when(fullUserAnswers.enterPayeReference) thenReturn Some("123456789")
      when(fullUserAnswers.detailsOfEmploymentOrPension) thenReturn Some("Employment details")

      val newXmlToNode = formatXml(fullUserAnswers)

      newXmlToNode \ "claimSection" \ "selectedTaxYear"  must contain(<selectedTaxYear>6 April 2016 to 5 April 2017</selectedTaxYear>)
      newXmlToNode \ "claimSection" \ "employmentDetails" must contain(<employmentDetails>false</employmentDetails>)
      newXmlToNode \ "claimSection" \ "payeReference" must contain(<payeReference>123456789</payeReference>)
      newXmlToNode \ "claimSection" \ "detailsOfEmploymentOrPension" must contain(<detailsOfEmploymentOrPension>Employment details</detailsOfEmploymentOrPension>)
    }

    "have correct sections in benefitSection" in {

      fullXml \ "benefitSection" \ "anyBenefits" must contain(<anyBenefits>true</anyBenefits>)
      fullXml \ "benefitSection" \ "selectBenefits" must contain(<selectBenefits>carers-allowance, bereavement-allowance, incapacity-benefit, employment-and-support-allowance, jobseekers-allowance, other-taxable-benefit, state-pension</selectBenefits>)
      fullXml \ "benefitSection" \ "howMuchBereavementAllowance" must contain(<howMuchBereavementAllowance>1234</howMuchBereavementAllowance>)
      fullXml \ "benefitSection" \ "howMuchCarersAllowance" must contain(<howMuchCarersAllowance>1234</howMuchCarersAllowance>)
      fullXml \ "benefitSection" \ "howMuchJobseekersAllowance" must contain(<howMuchJobseekersAllowance>1234</howMuchJobseekersAllowance>)
      fullXml \ "benefitSection" \ "howMuchEmploymentAndSupportAllowance" must contain(<howMuchEmploymentAndSupportAllowance>1234</howMuchEmploymentAndSupportAllowance>)
      fullXml \ "benefitSection" \ "howMuchIncapacityBenefit" must contain(<howMuchIncapacityBenefit>1234</howMuchIncapacityBenefit>)
      fullXml \ "benefitSection" \ "howMuchStatePension" must contain(<howMuchStatePension>1234</howMuchStatePension>)

      val otherBenefits: NodeSeq = fullXml  \ "benefitSection" \ "otherBenefits" \ "otherBenefit"

      otherBenefits.head mustBe <otherBenefit><name>qwerty</name><amount>12</amount></otherBenefit>
      otherBenefits(1) mustBe <otherBenefit><name>qwerty1</name><amount>34</amount></otherBenefit>
      otherBenefits(2) mustBe <otherBenefit><name>qwerty2</name><amount>56</amount></otherBenefit>
    }

    "have correct sections in companyBenefitSection" in {

      fullXml \ "companyBenefitsSection" \ "anyCompanyBenefits" must contain(<anyCompanyBenefits>true</anyCompanyBenefits>)
      fullXml \ "companyBenefitsSection" \ "selectCompanyBenefits" must contain(<selectCompanyBenefits>company-car-benefit, medical-benefit, fuel-benefit, other-company-benefit</selectCompanyBenefits>)
      fullXml \ "companyBenefitsSection" \ "howMuchCarBenefits" must contain(<howMuchCarBenefits>1234</howMuchCarBenefits>)
      fullXml \ "companyBenefitsSection" \ "howMuchFuelBenefit" must contain(<howMuchFuelBenefit>1234</howMuchFuelBenefit>)
      fullXml \ "companyBenefitsSection" \ "howMuchMedicalBenefits" must contain(<howMuchMedicalBenefits>1234</howMuchMedicalBenefits>)

      val otherCompanyBenefits: NodeSeq = fullXml \ "companyBenefitsSection" \ "otherCompanyBenefits" \ "companyBenefit"

      otherCompanyBenefits.head mustBe <companyBenefit><name>qwerty</name><amount>12</amount></companyBenefit>
      otherCompanyBenefits(1) mustBe <companyBenefit><name>qwerty1</name><amount>34</amount></companyBenefit>
      otherCompanyBenefits(2) mustBe <companyBenefit><name>qwerty2</name><amount>56</amount></companyBenefit>
    }

    "have correct sections in taxableIncomeSection" in {

      fullXml \ "taxableIncomeSection" \ "anyTaxableIncome" must contain(<anyTaxableIncome>true</anyTaxableIncome>)
      fullXml \ "taxableIncomeSection" \ "selectTaxableIncome" must contain(<selectTaxableIncome>rental-income, bank-or-building-society-interest, investment-or-dividends, foreign-income, other-taxable-income</selectTaxableIncome>)

      fullXml \ "taxableIncomeSection" \ "rentalIncome" \ "howMuch" must contain(<howMuch>1234</howMuch>)
      fullXml \ "taxableIncomeSection" \ "rentalIncome" \ "anyTaxPaid" must contain( <anyTaxPaid>Yes</anyTaxPaid>)
      fullXml \ "taxableIncomeSection" \ "rentalIncome" \ "taxPaid" must contain( <taxPaid>123</taxPaid>)

      fullXml \ "taxableIncomeSection" \ "bankInterest" \ "howMuch" must contain( <howMuch>1234</howMuch>)
      fullXml \ "taxableIncomeSection" \ "bankInterest" \ "anyTaxPaid" must contain( <anyTaxPaid>Yes</anyTaxPaid>)
      fullXml \ "taxableIncomeSection" \ "bankInterest" \ "taxPaid" must contain( <taxPaid>123</taxPaid>)

      fullXml \ "taxableIncomeSection" \ "investmentOrDividend" \ "howMuch" must contain( <howMuch>1234</howMuch>)
      fullXml \ "taxableIncomeSection" \ "investmentOrDividend" \ "anyTaxPaid" must contain( <anyTaxPaid>Yes</anyTaxPaid>)
      fullXml \ "taxableIncomeSection" \ "investmentOrDividend" \ "taxPaid" must contain( <taxPaid>123</taxPaid>)

      fullXml \ "taxableIncomeSection" \ "foreignIncome" \ "howMuch" must contain( <howMuch>1234</howMuch>)
      fullXml \ "taxableIncomeSection" \ "foreignIncome" \ "anyTaxPaid" must contain( <anyTaxPaid>Yes</anyTaxPaid>)
      fullXml \ "taxableIncomeSection" \ "foreignIncome" \ "taxPaid" must contain( <taxPaid>123</taxPaid>)

      val otherTaxableIncome = fullXml \ "taxableIncomeSection" \ "otherTaxableIncome" \ "taxableIncome"

      otherTaxableIncome.head mustBe <taxableIncome><name>qwerty</name><amount>12</amount></taxableIncome>
      otherTaxableIncome(1) mustBe <taxableIncome><name>qwerty1</name><amount>34</amount></taxableIncome>
      otherTaxableIncome(2) mustBe <taxableIncome><name>qwerty2</name><amount>56</amount></taxableIncome>
    }

    "hide sections when not selected" in {

      when(fullUserAnswers.anyBenefits) thenReturn Some(false)
      when(fullUserAnswers.anyCompanyBenefits) thenReturn Some(false)
      when(fullUserAnswers.anyTaxableIncome) thenReturn Some(false)

      val newXmlToNode = formatXml(fullUserAnswers)

      newXmlToNode.contains(<anyBenefits></anyBenefits>) mustBe false
      newXmlToNode.contains(<anyCompanyBenefits></anyCompanyBenefits>) mustBe false
      newXmlToNode.contains(<anyTaxableIncome></anyTaxableIncome>) mustBe false
    }

    "have correct sections in the paymentSection when payment address is international" in {

      fullXml \ "paymentSection" \ "whereToSendThePayment" must contain(<whereToSendThePayment>nominee</whereToSendThePayment>)
      fullXml \ "paymentSection" \ "nomineeFullname" must contain(<nomineeFullname>Nominee</nomineeFullname>)
      fullXml \ "paymentSection" \ "anyAgentRef" must contain(<anyAgentRef>Yes</anyAgentRef>)
      fullXml \ "paymentSection" \ "agentReference" must contain(<agentReference>12341234</agentReference>)
      fullXml \ "paymentSection" \ "isPaymentAddressInTheUK" must contain(<isPaymentAddressInTheUK>false</isPaymentAddressInTheUK>)
      fullXml \ "paymentSection" \ "paymentAddress" must contain(
        trim(
          <paymentAddress>
            <internationalAddress>
              <addressLine1>1</addressLine1>
              <addressLine2>2</addressLine2>
              <addressLine3></addressLine3>
              <addressLine4></addressLine4>
              <addressLine5></addressLine5>
              <country>Country</country>
            </internationalAddress>
          </paymentAddress>
        )
      )
    }

    "have correct sections in the paymentSection when payment address is in the UK" in {

      when(fullUserAnswers.isPaymentAddressInTheUK) thenReturn Some(true)
      when(fullUserAnswers.paymentInternationalAddress) thenReturn None
      when(fullUserAnswers.paymentUKAddress) thenReturn Some(UkAddress("qwerty", "qwerty1", None, None, None, "AB1 0CD"))

      val newXmlToNode = formatXml(fullUserAnswers)

      newXmlToNode \ "paymentSection" \ "whereToSendThePayment" must contain(<whereToSendThePayment>nominee</whereToSendThePayment>)
      newXmlToNode \ "paymentSection" \ "nomineeFullname" must contain(<nomineeFullname>Nominee</nomineeFullname>)
      newXmlToNode \ "paymentSection" \ "anyAgentRef" must contain(<anyAgentRef>Yes</anyAgentRef>)
      newXmlToNode \ "paymentSection" \ "agentReference" must contain(<agentReference>12341234</agentReference>)
      newXmlToNode \ "paymentSection" \ "isPaymentAddressInTheUK" must contain(<isPaymentAddressInTheUK>true</isPaymentAddressInTheUK>)
      newXmlToNode \ "paymentSection" \ "paymentAddress" must contain(
        trim(
          <paymentAddress>
            <ukAddress>
              <addressLine1>qwerty</addressLine1>
              <addressLine2>qwerty1</addressLine2>
              <addressLine3></addressLine3>
              <addressLine4></addressLine4>
              <addressLine5></addressLine5>
              <postcode>AB1 0CD</postcode>
            </ukAddress>
          </paymentAddress>
        )
      )
    }

    "have correct sections in the paymentSection when payment address is a lookup" in {

      when(fullUserAnswers.isPaymentAddressInTheUK) thenReturn None
      when(fullUserAnswers.paymentUKAddress) thenReturn None
      when(fullUserAnswers.paymentInternationalAddress) thenReturn None
      when(fullUserAnswers.paymentLookupAddress) thenReturn Some(AddressLookup(
        address = Some(Address(
          lines = Some(Seq("Line1", "Line2", "Line3", "Line4")),
          postcode = Some("NE1 1LX"),
          country = Some(Country(Some("United Kingdom"),Some("GB")))
        )),
        auditRef = Some("e9e2fb3f-268f-4c4c-b928-3dc0b17259f2")
      ))

      val newXmlToNode = formatXml(fullUserAnswers)

      newXmlToNode \ "paymentSection" \ "paymentAddress" must contain(
        trim(
          <paymentAddress>
            <lookupAddress>
              <addressLine1>Line1</addressLine1>
              <addressLine2>Line2</addressLine2>
              <addressLine3>Line3</addressLine3>
              <addressLine4>Line4</addressLine4>
              <addressLine5></addressLine5>
              <postcode>NE1 1LX</postcode>
              <country>United Kingdom</country>
              <countryCode>GB</countryCode>
            </lookupAddress>
          </paymentAddress>
        )
      )
    }

    "have the correct parts in contact section" in {

      fullXml \ "contactSection" \ "anyTelephoneNumber" must contain(<anyTelephoneNumber>Yes</anyTelephoneNumber>)
      fullXml \ "contactSection" \ "telephoneNumber" must contain(<telephoneNumber>0191123123</telephoneNumber>)
    }

    "minimal valid answers must form correctly" in {
      val xml: Node = formatXml(MockUserAnswers.minimalValidUserAnswers)

      xml mustBe validMinimalXml
    }
  }
}

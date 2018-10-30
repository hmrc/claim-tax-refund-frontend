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

import models._
import play.api.i18n.Messages
import uk.gov.hmrc.auth.core.retrieve.{ItmpAddress, ItmpName}
import utils.UserAnswers

import scala.xml._
import scala.xml.Utility.trim

class RobotXML {

  def generateXml(
                   userAnswers: UserAnswers,
                   submissionReference: String ,
                   dateCreated: String,
                   nino: String,
                   itmpName: ItmpName,
                   itmpAddress: ItmpAddress
                 )(implicit messages: Messages): Node =
    trim(
      <ctr>
        <submissionReference>{submissionReference}</submissionReference>
        <dateCreated>{dateCreated}</dateCreated>

        <userDetails>
          {
            itmpName.givenName.map { value =>
              <firstName>{value}</firstName>
            } ++
            itmpName.middleName.map { value =>
              <middleName>{value}</middleName>
            } ++
            itmpName.familyName.map { value =>
              <lastName>{value}</lastName>
            }
          }
          <nino>{nino}</nino>
          <itmpAddress>{ItmpAddressFormat.toXml(itmpAddress)}</itmpAddress>
        </userDetails>

        <claimSection>
          {
            userAnswers.selectTaxYear.toSeq.map { value =>
              <selectedTaxYear>{value.asString}</selectedTaxYear>
            } ++
            userAnswers.employmentDetails.toSeq.map { value =>
              <employmentDetails>{value.toString}</employmentDetails>
            } ++
            userAnswers.enterPayeReference.toSeq.map { value =>
              <payeReference>{value}</payeReference>
            } ++
            userAnswers.detailsOfEmploymentOrPension.toSeq.map { value =>
              <detailsOfEmploymentOrPension>{value}</detailsOfEmploymentOrPension>
            }
          }
        </claimSection>

        <benefitSection>
          {
            userAnswers.anyBenefits.toSeq.map { value =>
              <anyBenefits>{value.toString}</anyBenefits>
            } ++
            userAnswers.selectBenefits.toSeq.map { value =>
              <selectBenefits>{value.mkString(", ")}</selectBenefits>
            } ++
            userAnswers.howMuchBereavementAllowance.toSeq.map { value =>
              <howMuchBereavementAllowance>{value}</howMuchBereavementAllowance>
            } ++
            userAnswers.howMuchCarersAllowance.toSeq.map { value =>
              <howMuchCarersAllowance>{value}</howMuchCarersAllowance>
            } ++
            userAnswers.howMuchJobseekersAllowance.toSeq.map { value =>
              <howMuchJobseekersAllowance>{value}</howMuchJobseekersAllowance>
            } ++
            userAnswers.howMuchEmploymentAndSupportAllowance.toSeq.map{ value =>
              <howMuchEmploymentAndSupportAllowance>{value}</howMuchEmploymentAndSupportAllowance>
            } ++
            userAnswers.howMuchIncapacityBenefit.toSeq.map { value =>
              <howMuchIncapacityBenefit>{value}</howMuchIncapacityBenefit>
            } ++
            userAnswers.howMuchStatePension.toSeq.map{ value =>
              <howMuchStatePension>{value}</howMuchStatePension>
            } ++
            userAnswers.otherBenefit.toSeq.map { value =>
              <otherBenefits>{OtherBenefit.toXml(value)}</otherBenefits>
            }
          }
        </benefitSection>

        <companyBenefitsSection>
          {
            userAnswers.anyCompanyBenefits.toSeq.map { value =>
              <anyCompanyBenefits>{value.toString}</anyCompanyBenefits>
            } ++
            userAnswers.selectCompanyBenefits.toSeq.map { value =>
              <selectCompanyBenefits>{value.mkString(", ")}</selectCompanyBenefits>
            } ++
            userAnswers.howMuchCarBenefits.toSeq.map { value =>
              <howMuchCarBenefits>{value}</howMuchCarBenefits>
            } ++
            userAnswers.howMuchFuelBenefit.toSeq.map { value =>
              <howMuchFuelBenefit>{value}</howMuchFuelBenefit>
            } ++
            userAnswers.howMuchMedicalBenefits.toSeq.map { value =>
              <howMuchMedicalBenefits>{value}</howMuchMedicalBenefits>
            } ++
            userAnswers.otherCompanyBenefit.toSeq.map { value =>
              <otherCompanyBenefits>{OtherCompanyBenefit.toXml(value)}</otherCompanyBenefits>
            }
          }
        </companyBenefitsSection>

        <taxableIncomeSection>
          {
            userAnswers.anyTaxableIncome.toSeq.map { value =>
              <anyTaxableIncome>{value.toString}</anyTaxableIncome>
            } ++
            userAnswers.selectTaxableIncome.toSeq.map { value =>
              <selectTaxableIncome>{value.mkString(", ")}</selectTaxableIncome>
            } ++
            userAnswers.howMuchRentalIncome.toSeq.map { value =>
              <rentalIncome>
                <howMuch>{value}</howMuch>
                {
                  userAnswers.anyTaxableRentalIncome.toSeq.map { value =>
                    AnyTaxPaid.toXml(value)
                  }
                }
              </rentalIncome>
            } ++
            userAnswers.howMuchBankInterest.toSeq.map { value =>
              <bankInterest>
                <howMuch>{value}</howMuch>
                {
                  userAnswers.anyTaxableBankInterest.toSeq.map { value =>
                    AnyTaxPaid.toXml(value)
                  }
                }
              </bankInterest>
            } ++
            userAnswers.howMuchInvestmentOrDividend.toSeq.map { value =>
              <investmentOrDividend>
                <howMuch>{value}</howMuch>
                {
                  userAnswers.anyTaxableInvestments.toSeq.map { value =>
                    AnyTaxPaid.toXml(value)
                  }
                }
              </investmentOrDividend>
            } ++
            userAnswers.howMuchForeignIncome.toSeq.map { value =>
              <foreignIncome>
                <howMuch>{value}</howMuch>
                {
                  userAnswers.anyTaxableForeignIncome.toSeq.map { value =>
                    AnyTaxPaid.toXml(value)
                  }
                }
              </foreignIncome>
            } ++
            userAnswers.otherTaxableIncome.toSeq.map { value =>
              <otherTaxableIncome>{OtherTaxableIncome.toXml(value)}</otherTaxableIncome>
            }
          }
        </taxableIncomeSection>

        <paymentSection>
          {
            userAnswers.whereToSendPayment.toSeq.map { value =>
              <whereToSendThePayment>{value.toString}</whereToSendThePayment>
            } ++
            userAnswers.paymentAddressCorrect.toSeq.map { value =>
              <paymentAddressCorrect>{value.toString}</paymentAddressCorrect>
            } ++
            userAnswers.nomineeFullName.toSeq.map { value =>
              <nomineeFullname>{value}</nomineeFullname>
            } ++
            userAnswers.anyAgentRef.toSeq.map { value =>
              AnyAgentRef.toXml(value)
            } ++
            userAnswers.isPaymentAddressInTheUK.toSeq.map { value =>
              <isPaymentAddressInTheUK>{value.toString}</isPaymentAddressInTheUK>
            } ++
            userAnswers.paymentUKAddress.toSeq.map { value =>
              UkAddress.toXml(value)
            } ++
            userAnswers.paymentInternationalAddress.toSeq.map { value =>
              InternationalAddress.toXml(value)
            } ++
            userAnswers.paymentLookupAddress.toSeq.map { value =>
              AddressLookup.toXml(value)
            }
          }
        </paymentSection>

        <contactSection>
          {
            userAnswers.anyTelephoneNumber.toSeq.map { value =>
              TelephoneOption.toXml(value)
            }
          }
        </contactSection>
      </ctr>
    )
}

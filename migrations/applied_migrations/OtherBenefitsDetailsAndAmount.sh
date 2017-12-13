#!/bin/bash

echo "Applying migration OtherBenefitsDetailsAndAmount"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /otherBenefitsDetailsAndAmount               controllers.OtherBenefitsDetailsAndAmountController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /otherBenefitsDetailsAndAmount               controllers.OtherBenefitsDetailsAndAmountController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOtherBenefitsDetailsAndAmount                        controllers.OtherBenefitsDetailsAndAmountController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOtherBenefitsDetailsAndAmount                        controllers.OtherBenefitsDetailsAndAmountController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "otherBenefitsDetailsAndAmount.title = otherBenefitsDetailsAndAmount" >> ../conf/messages.en
echo "otherBenefitsDetailsAndAmount.heading = otherBenefitsDetailsAndAmount" >> ../conf/messages.en
echo "otherBenefitsDetailsAndAmount.checkYourAnswersLabel = otherBenefitsDetailsAndAmount" >> ../conf/messages.en
echo "otherBenefitsDetailsAndAmount.blank = otherBenefitsDetailsAndAmount" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def otherBenefitsDetailsAndAmount: Option[String] = cacheMap.getEntry[String](OtherBenefitsDetailsAndAmountId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherBenefitsDetailsAndAmount: Option[AnswerRow] = userAnswers.otherBenefitsDetailsAndAmount map {";\
     print "    x => AnswerRow(\"otherBenefitsDetailsAndAmount.checkYourAnswersLabel\", s\"$x\", false, routes.OtherBenefitsDetailsAndAmountController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration OtherBenefitsDetailsAndAmount completed"

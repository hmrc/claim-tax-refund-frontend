#!/bin/bash

echo "Applying migration OtherIncomeDetailsAndAmount"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /otherIncomeDetailsAndAmount               controllers.OtherIncomeDetailsAndAmountController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /otherIncomeDetailsAndAmount               controllers.OtherIncomeDetailsAndAmountController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOtherIncomeDetailsAndAmount                        controllers.OtherIncomeDetailsAndAmountController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOtherIncomeDetailsAndAmount                        controllers.OtherIncomeDetailsAndAmountController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "otherIncomeDetailsAndAmount.title = otherIncomeDetailsAndAmount" >> ../conf/messages.en
echo "otherIncomeDetailsAndAmount.heading = otherIncomeDetailsAndAmount" >> ../conf/messages.en
echo "otherIncomeDetailsAndAmount.checkYourAnswersLabel = otherIncomeDetailsAndAmount" >> ../conf/messages.en
echo "otherIncomeDetailsAndAmount.blank = otherIncomeDetailsAndAmount" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def otherIncomeDetailsAndAmount: Option[String] = cacheMap.getEntry[String](OtherIncomeDetailsAndAmountId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherIncomeDetailsAndAmount: Option[AnswerRow] = userAnswers.otherIncomeDetailsAndAmount map {";\
     print "    x => AnswerRow(\"otherIncomeDetailsAndAmount.checkYourAnswersLabel\", s\"$x\", false, routes.OtherIncomeDetailsAndAmountController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration OtherIncomeDetailsAndAmount completed"

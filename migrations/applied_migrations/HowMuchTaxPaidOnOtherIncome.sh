#!/bin/bash

echo "Applying migration HowMuchTaxPaidOnOtherIncome"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchTaxPaidOnOtherIncome               controllers.HowMuchTaxPaidOnOtherIncomeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchTaxPaidOnOtherIncome               controllers.HowMuchTaxPaidOnOtherIncomeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchTaxPaidOnOtherIncome                        controllers.HowMuchTaxPaidOnOtherIncomeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchTaxPaidOnOtherIncome                        controllers.HowMuchTaxPaidOnOtherIncomeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchTaxPaidOnOtherIncome.title = howMuchTaxPaidOnOtherIncome" >> ../conf/messages.en
echo "howMuchTaxPaidOnOtherIncome.heading = howMuchTaxPaidOnOtherIncome" >> ../conf/messages.en
echo "howMuchTaxPaidOnOtherIncome.checkYourAnswersLabel = howMuchTaxPaidOnOtherIncome" >> ../conf/messages.en
echo "howMuchTaxPaidOnOtherIncome.blank = howMuchTaxPaidOnOtherIncome" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchTaxPaidOnOtherIncome: Option[String] = cacheMap.getEntry[String](HowMuchTaxPaidOnOtherIncomeId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchTaxPaidOnOtherIncome: Option[AnswerRow] = userAnswers.howMuchTaxPaidOnOtherIncome map {";\
     print "    x => AnswerRow(\"howMuchTaxPaidOnOtherIncome.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchTaxPaidOnOtherIncomeController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchTaxPaidOnOtherIncome completed"

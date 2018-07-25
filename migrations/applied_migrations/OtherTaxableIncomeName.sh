#!/bin/bash

echo "Applying migration OtherTaxableIncomeName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /otherTaxableIncomeName               controllers.OtherTaxableIncomeNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /otherTaxableIncomeName               controllers.OtherTaxableIncomeNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOtherTaxableIncomeName                        controllers.OtherTaxableIncomeNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOtherTaxableIncomeName                        controllers.OtherTaxableIncomeNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "otherTaxableIncomeName.title = otherTaxableIncomeName" >> ../conf/messages.en
echo "otherTaxableIncomeName.heading = otherTaxableIncomeName" >> ../conf/messages.en
echo "otherTaxableIncomeName.checkYourAnswersLabel = otherTaxableIncomeName" >> ../conf/messages.en
echo "otherTaxableIncomeName.blank = otherTaxableIncomeName" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def otherTaxableIncomeName: Option[String] = cacheMap.getEntry[String](OtherTaxableIncomeId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherTaxableIncomeName: Option[AnswerRow] = userAnswers.otherTaxableIncomeName map {";\
     print "    x => AnswerRow(\"otherTaxableIncomeName.checkYourAnswersLabel\", s\"$x\", false, routes.OtherTaxableIncomeNameController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration OtherTaxableIncomeName completed"

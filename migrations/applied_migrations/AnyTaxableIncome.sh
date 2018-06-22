#!/bin/bash

echo "Applying migration AnyTaxableIncome"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyTaxableIncome                       controllers.AnyTaxableIncomeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyTaxableIncome                       controllers.AnyTaxableIncomeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOtherIncome                       controllers.AnyTaxableIncomeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOtherIncome                       controllers.AnyTaxableIncomeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyTaxableIncome.title = anyTaxableIncome" >> ../conf/messages.en
echo "anyTaxableIncome.heading = anyTaxableIncome" >> ../conf/messages.en
echo "anyTaxableIncome.checkYourAnswersLabel = anyTaxableIncome" >> ../conf/messages.en
echo "anyTaxableIncome.blank = anyTaxableIncome" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyTaxableIncome: Option[Boolean] = cacheMap.getEntry[Boolean](AnyTaxableIncomeId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyTaxableIncome: Option[AnswerRow] = userAnswers.anyTaxableIncome map {";\
     print "    x => AnswerRow(\"anyTaxableIncome.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyTaxableIncomeController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyTaxableIncome completed"

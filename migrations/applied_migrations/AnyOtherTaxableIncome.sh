#!/bin/bash

echo "Applying migration AnyOtherTaxableIncome"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyOtherTaxableIncome                       controllers.AnyOtherTaxableIncomeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyOtherTaxableIncome                       controllers.AnyOtherTaxableIncomeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyOtherTaxableIncome                       controllers.AnyOtherTaxableIncomeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyOtherTaxableIncome                       controllers.AnyOtherTaxableIncomeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyOtherTaxableIncome.title = anyOtherTaxableIncome" >> ../conf/messages.en
echo "anyOtherTaxableIncome.heading = anyOtherTaxableIncome" >> ../conf/messages.en
echo "anyOtherTaxableIncome.checkYourAnswersLabel = anyOtherTaxableIncome" >> ../conf/messages.en
echo "anyOtherTaxableIncome.blank = anyOtherTaxableIncome" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyOtherTaxableIncome: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherTaxableIncomeId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyOtherTaxableIncome: Option[AnswerRow] = userAnswers.anyOtherTaxableIncome map {";\
     print "    x => AnswerRow(\"anyOtherTaxableIncome.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyOtherTaxableIncome completed"

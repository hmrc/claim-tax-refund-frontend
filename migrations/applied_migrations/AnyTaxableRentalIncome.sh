#!/bin/bash

echo "Applying migration AnyTaxableRentalIncome"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyTaxableRentalIncome                       controllers.AnyTaxableRentalIncomeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyTaxableRentalIncome                       controllers.AnyTaxableRentalIncomeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyTaxableRentalIncome                       controllers.AnyTaxableRentalIncomeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyTaxableRentalIncome                       controllers.AnyTaxableRentalIncomeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyTaxableRentalIncome.title = anyTaxableRentalIncome" >> ../conf/messages.en
echo "anyTaxableRentalIncome.heading = anyTaxableRentalIncome" >> ../conf/messages.en
echo "anyTaxableRentalIncome.checkYourAnswersLabel = anyTaxableRentalIncome" >> ../conf/messages.en
echo "anyTaxableRentalIncome.blank = anyTaxableRentalIncome" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyTaxableRentalIncome: Option[Boolean] = cacheMap.getEntry[Boolean](AnyTaxableRentalIncomeId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyTaxableRentalIncome: Option[AnswerRow] = userAnswers.anyTaxableRentalIncome map {";\
     print "    x => AnswerRow(\"anyTaxableRentalIncome.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyTaxableRentalIncome completed"

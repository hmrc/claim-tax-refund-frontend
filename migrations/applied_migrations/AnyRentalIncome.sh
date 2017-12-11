#!/bin/bash

echo "Applying migration AnyRentalIncome"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyRentalIncome                       controllers.AnyRentalIncomeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyRentalIncome                       controllers.AnyRentalIncomeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyRentalIncome                       controllers.AnyRentalIncomeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyRentalIncome                       controllers.AnyRentalIncomeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyRentalIncome.title = anyRentalIncome" >> ../conf/messages.en
echo "anyRentalIncome.heading = anyRentalIncome" >> ../conf/messages.en
echo "anyRentalIncome.checkYourAnswersLabel = anyRentalIncome" >> ../conf/messages.en
echo "anyRentalIncome.blank = anyRentalIncome" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyRentalIncome: Option[Boolean] = cacheMap.getEntry[Boolean](AnyRentalIncomeId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyRentalIncome: Option[AnswerRow] = userAnswers.anyRentalIncome map {";\
     print "    x => AnswerRow(\"anyRentalIncome.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyRentalIncomeController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyRentalIncome completed"

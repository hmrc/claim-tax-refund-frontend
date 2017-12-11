#!/bin/bash

echo "Applying migration OtherIncome"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /otherIncome                       controllers.OtherIncomeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /otherIncome                       controllers.OtherIncomeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOtherIncome                       controllers.OtherIncomeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOtherIncome                       controllers.OtherIncomeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "otherIncome.title = otherIncome" >> ../conf/messages.en
echo "otherIncome.heading = otherIncome" >> ../conf/messages.en
echo "otherIncome.checkYourAnswersLabel = otherIncome" >> ../conf/messages.en
echo "otherIncome.blank = otherIncome" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def otherIncome: Option[Boolean] = cacheMap.getEntry[Boolean](OtherIncomeId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherIncome: Option[AnswerRow] = userAnswers.otherIncome map {";\
     print "    x => AnswerRow(\"otherIncome.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.OtherIncomeController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration OtherIncome completed"

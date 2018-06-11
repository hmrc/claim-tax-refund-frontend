#!/bin/bash

echo "Applying migration HowMuchForeignIncome"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchForeignIncome               controllers.HowMuchForeignIncomeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchForeignIncome               controllers.HowMuchForeignIncomeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchForeignIncome                        controllers.HowMuchForeignIncomeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchForeignIncome                        controllers.HowMuchForeignIncomeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchForeignIncome.title = howMuchForeignIncome" >> ../conf/messages.en
echo "howMuchForeignIncome.heading = howMuchForeignIncome" >> ../conf/messages.en
echo "howMuchForeignIncome.checkYourAnswersLabel = howMuchForeignIncome" >> ../conf/messages.en
echo "howMuchForeignIncome.blank = howMuchForeignIncome" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchForeignIncome: Option[String] = cacheMap.getEntry[String](HowMuchForeignIncomeId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchForeignIncome: Option[AnswerRow] = userAnswers.howMuchForeignIncome map {";\
     print "    x => AnswerRow(\"howMuchForeignIncome.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchForeignIncomeController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchForeignIncome completed"

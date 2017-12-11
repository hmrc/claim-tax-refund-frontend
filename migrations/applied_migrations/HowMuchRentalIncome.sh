#!/bin/bash

echo "Applying migration HowMuchRentalIncome"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchRentalIncome               controllers.HowMuchRentalIncomeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchRentalIncome               controllers.HowMuchRentalIncomeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchRentalIncome                        controllers.HowMuchRentalIncomeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchRentalIncome                        controllers.HowMuchRentalIncomeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchRentalIncome.title = howMuchRentalIncome" >> ../conf/messages.en
echo "howMuchRentalIncome.heading = howMuchRentalIncome" >> ../conf/messages.en
echo "howMuchRentalIncome.checkYourAnswersLabel = howMuchRentalIncome" >> ../conf/messages.en
echo "howMuchRentalIncome.blank = howMuchRentalIncome" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchRentalIncome: Option[String] = cacheMap.getEntry[String](HowMuchRentalIncomeId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchRentalIncome: Option[AnswerRow] = userAnswers.howMuchRentalIncome map {";\
     print "    x => AnswerRow(\"howMuchRentalIncome.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchRentalIncomeController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchRentalIncome completed"

#!/bin/bash

echo "Applying migration HowMuchCarersAllowance"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchCarersAllowance               controllers.HowMuchCarersAllowanceController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchCarersAllowance               controllers.HowMuchCarersAllowanceController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchCarersAllowance                        controllers.HowMuchCarersAllowanceController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchCarersAllowance                        controllers.HowMuchCarersAllowanceController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchCarersAllowance.title = howMuchCarersAllowance" >> ../conf/messages.en
echo "howMuchCarersAllowance.heading = howMuchCarersAllowance" >> ../conf/messages.en
echo "howMuchCarersAllowance.checkYourAnswersLabel = howMuchCarersAllowance" >> ../conf/messages.en
echo "howMuchCarersAllowance.blank = howMuchCarersAllowance" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchCarersAllowance: Option[String] = cacheMap.getEntry[String](HowMuchCarersAllowanceId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchCarersAllowance: Option[AnswerRow] = userAnswers.howMuchCarersAllowance map {";\
     print "    x => AnswerRow(\"howMuchCarersAllowance.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchCarersAllowanceController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchCarersAllowance completed"

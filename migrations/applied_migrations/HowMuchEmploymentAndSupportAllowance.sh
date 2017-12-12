#!/bin/bash

echo "Applying migration HowMuchEmploymentAndSupportAllowance"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchEmploymentAndSupportAllowance               controllers.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchEmploymentAndSupportAllowance               controllers.HowMuchEmploymentAndSupportAllowanceController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchEmploymentAndSupportAllowance                        controllers.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchEmploymentAndSupportAllowance                        controllers.HowMuchEmploymentAndSupportAllowanceController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchEmploymentAndSupportAllowance.title = howMuchEmploymentAndSupportAllowance" >> ../conf/messages.en
echo "howMuchEmploymentAndSupportAllowance.heading = howMuchEmploymentAndSupportAllowance" >> ../conf/messages.en
echo "howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel = howMuchEmploymentAndSupportAllowance" >> ../conf/messages.en
echo "howMuchEmploymentAndSupportAllowance.blank = howMuchEmploymentAndSupportAllowance" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchEmploymentAndSupportAllowance: Option[String] = cacheMap.getEntry[String](HowMuchEmploymentAndSupportAllowanceId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchEmploymentAndSupportAllowance: Option[AnswerRow] = userAnswers.howMuchEmploymentAndSupportAllowance map {";\
     print "    x => AnswerRow(\"howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchEmploymentAndSupportAllowance completed"

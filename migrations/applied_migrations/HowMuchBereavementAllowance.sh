#!/bin/bash

echo "Applying migration HowMuchBereavementAllowance"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchBereavementAllowance               controllers.HowMuchBereavementAllowanceController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchBereavementAllowance               controllers.HowMuchBereavementAllowanceController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchBereavementAllowance                        controllers.HowMuchBereavementAllowanceController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchBereavementAllowance                        controllers.HowMuchBereavementAllowanceController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchBereavementAllowance.title = howMuchBereavementAllowance" >> ../conf/messages.en
echo "howMuchBereavementAllowance.heading = howMuchBereavementAllowance" >> ../conf/messages.en
echo "howMuchBereavementAllowance.checkYourAnswersLabel = howMuchBereavementAllowance" >> ../conf/messages.en
echo "howMuchBereavementAllowance.blank = howMuchBereavementAllowance" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchBereavementAllowance: Option[String] = cacheMap.getEntry[String](HowMuchBereavementAllowanceId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchBereavementAllowance: Option[AnswerRow] = userAnswers.howMuchBereavementAllowance map {";\
     print "    x => AnswerRow(\"howMuchBereavementAllowance.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchBereavementAllowanceController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchBereavementAllowance completed"

#!/bin/bash

echo "Applying migration HowMuchStatePension"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchStatePension               controllers.HowMuchStatePensionController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchStatePension               controllers.HowMuchStatePensionController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchStatePension                        controllers.HowMuchStatePensionController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchStatePension                        controllers.HowMuchStatePensionController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchStatePension.title = howMuchStatePension" >> ../conf/messages.en
echo "howMuchStatePension.heading = howMuchStatePension" >> ../conf/messages.en
echo "howMuchStatePension.checkYourAnswersLabel = howMuchStatePension" >> ../conf/messages.en
echo "howMuchStatePension.blank = howMuchStatePension" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchStatePension: Option[String] = cacheMap.getEntry[String](HowMuchStatePensionId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchStatePension: Option[AnswerRow] = userAnswers.howMuchStatePension map {";\
     print "    x => AnswerRow(\"howMuchStatePension.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchStatePensionController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchStatePension completed"

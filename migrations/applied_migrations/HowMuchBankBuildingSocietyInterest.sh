#!/bin/bash

echo "Applying migration HowMuchBankBuildingSocietyInterest"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchBankBuildingSocietyInterest               controllers.HowMuchBankBuildingSocietyInterestController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchBankBuildingSocietyInterest               controllers.HowMuchBankBuildingSocietyInterestController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchBankBuildingSocietyInterest                        controllers.HowMuchBankBuildingSocietyInterestController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchBankBuildingSocietyInterest                        controllers.HowMuchBankBuildingSocietyInterestController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchBankBuildingSocietyInterest.title = howMuchBankBuildingSocietyInterest" >> ../conf/messages.en
echo "howMuchBankBuildingSocietyInterest.heading = howMuchBankBuildingSocietyInterest" >> ../conf/messages.en
echo "howMuchBankBuildingSocietyInterest.checkYourAnswersLabel = howMuchBankBuildingSocietyInterest" >> ../conf/messages.en
echo "howMuchBankBuildingSocietyInterest.blank = howMuchBankBuildingSocietyInterest" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchBankBuildingSocietyInterest: Option[String] = cacheMap.getEntry[String](HowMuchBankBuildingSocietyInterestId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchBankBuildingSocietyInterest: Option[AnswerRow] = userAnswers.howMuchBankBuildingSocietyInterest map {";\
     print "    x => AnswerRow(\"howMuchBankBuildingSocietyInterest.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchBankBuildingSocietyInterestController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchBankBuildingSocietyInterest completed"

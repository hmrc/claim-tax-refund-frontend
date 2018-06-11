#!/bin/bash

echo "Applying migration HowMuchBankInterest"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchBankInterest               controllers.HowMuchBankInterestController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchBankInterest               controllers.HowMuchBankInterestController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchBankInterest                        controllers.HowMuchBankInterestController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchBankInterest                        controllers.HowMuchBankInterestController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchBankInterest.title = howMuchBankInterest" >> ../conf/messages.en
echo "howMuchBankInterest.heading = howMuchBankInterest" >> ../conf/messages.en
echo "howMuchBankInterest.checkYourAnswersLabel = howMuchBankInterest" >> ../conf/messages.en
echo "howMuchBankInterest.blank = howMuchBankInterest" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchBankInterest: Option[String] = cacheMap.getEntry[String](HowMuchBankInterestId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchBankInterest: Option[AnswerRow] = userAnswers.howMuchBankInterest map {";\
     print "    x => AnswerRow(\"howMuchBankInterest.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchBankInterestController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchBankInterest completed"

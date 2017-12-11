#!/bin/bash

echo "Applying migration HowMuchCarBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchCarBenefits               controllers.HowMuchCarBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchCarBenefits               controllers.HowMuchCarBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchCarBenefits                        controllers.HowMuchCarBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchCarBenefits                        controllers.HowMuchCarBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchCarBenefits.title = howMuchCarBenefits" >> ../conf/messages.en
echo "howMuchCarBenefits.heading = howMuchCarBenefits" >> ../conf/messages.en
echo "howMuchCarBenefits.checkYourAnswersLabel = howMuchCarBenefits" >> ../conf/messages.en
echo "howMuchCarBenefits.blank = howMuchCarBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchCarBenefits: Option[String] = cacheMap.getEntry[String](HowMuchCarBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchCarBenefits: Option[AnswerRow] = userAnswers.howMuchCarBenefits map {";\
     print "    x => AnswerRow(\"howMuchCarBenefits.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchCarBenefitsController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchCarBenefits completed"

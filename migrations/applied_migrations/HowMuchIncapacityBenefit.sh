#!/bin/bash

echo "Applying migration HowMuchIncapacityBenefit"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchIncapacityBenefit               controllers.HowMuchIncapacityBenefitController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchIncapacityBenefit               controllers.HowMuchIncapacityBenefitController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchIncapacityBenefit                        controllers.HowMuchIncapacityBenefitController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchIncapacityBenefit                        controllers.HowMuchIncapacityBenefitController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchIncapacityBenefit.title = howMuchIncapacityBenefit" >> ../conf/messages.en
echo "howMuchIncapacityBenefit.heading = howMuchIncapacityBenefit" >> ../conf/messages.en
echo "howMuchIncapacityBenefit.checkYourAnswersLabel = howMuchIncapacityBenefit" >> ../conf/messages.en
echo "howMuchIncapacityBenefit.blank = howMuchIncapacityBenefit" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchIncapacityBenefit: Option[String] = cacheMap.getEntry[String](HowMuchIncapacityBenefitId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchIncapacityBenefit: Option[AnswerRow] = userAnswers.howMuchIncapacityBenefit map {";\
     print "    x => AnswerRow(\"howMuchIncapacityBenefit.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchIncapacityBenefitController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchIncapacityBenefit completed"

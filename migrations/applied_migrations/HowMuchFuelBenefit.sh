#!/bin/bash

echo "Applying migration HowMuchFuelBenefit"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchFuelBenefit               controllers.HowMuchFuelBenefitController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchFuelBenefit               controllers.HowMuchFuelBenefitController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchFuelBenefit                        controllers.HowMuchFuelBenefitController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchFuelBenefit                        controllers.HowMuchFuelBenefitController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchFuelBenefit.title = howMuchFuelBenefit" >> ../conf/messages.en
echo "howMuchFuelBenefit.heading = howMuchFuelBenefit" >> ../conf/messages.en
echo "howMuchFuelBenefit.checkYourAnswersLabel = howMuchFuelBenefit" >> ../conf/messages.en
echo "howMuchFuelBenefit.blank = howMuchFuelBenefit" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchFuelBenefit: Option[String] = cacheMap.getEntry[String](HowMuchFuelBenefitId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchFuelBenefit: Option[AnswerRow] = userAnswers.howMuchFuelBenefit map {";\
     print "    x => AnswerRow(\"howMuchFuelBenefit.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchFuelBenefitController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchFuelBenefit completed"

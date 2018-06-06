#!/bin/bash

echo "Applying migration HowMuchOtherBenefit"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchOtherBenefit               controllers.HowMuchOtherBenefitController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchOtherBenefit               controllers.HowMuchOtherBenefitController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchOtherBenefit                        controllers.HowMuchOtherBenefitController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchOtherBenefit                        controllers.HowMuchOtherBenefitController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchOtherBenefit.title = howMuchOtherBenefit" >> ../conf/messages.en
echo "howMuchOtherBenefit.heading = howMuchOtherBenefit" >> ../conf/messages.en
echo "howMuchOtherBenefit.checkYourAnswersLabel = howMuchOtherBenefit" >> ../conf/messages.en
echo "howMuchOtherBenefit.blank = howMuchOtherBenefit" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchOtherBenefit: Option[String] = cacheMap.getEntry[String](HowMuchOtherBenefitId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchOtherBenefit: Option[AnswerRow] = userAnswers.howMuchOtherBenefit map {";\
     print "    x => AnswerRow(\"howMuchOtherBenefit.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchOtherBenefitController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchOtherBenefit completed"

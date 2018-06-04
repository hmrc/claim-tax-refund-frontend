#!/bin/bash

echo "Applying migration HowMuchOtherCompanyBenefit"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchOtherCompanyBenefit               controllers.HowMuchOtherCompanyBenefitController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchOtherCompanyBenefit               controllers.HowMuchOtherCompanyBenefitController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchOtherCompanyBenefit                        controllers.HowMuchOtherCompanyBenefitController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchOtherCompanyBenefit                        controllers.HowMuchOtherCompanyBenefitController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchOtherCompanyBenefit.title = howMuchOtherCompanyBenefit" >> ../conf/messages.en
echo "howMuchOtherCompanyBenefit.heading = howMuchOtherCompanyBenefit" >> ../conf/messages.en
echo "howMuchOtherCompanyBenefit.checkYourAnswersLabel = howMuchOtherCompanyBenefit" >> ../conf/messages.en
echo "howMuchOtherCompanyBenefit.blank = howMuchOtherCompanyBenefit" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchOtherCompanyBenefit: Option[String] = cacheMap.getEntry[String](HowMuchOtherCompanyBenefitId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchOtherCompanyBenefit: Option[AnswerRow] = userAnswers.howMuchOtherCompanyBenefit map {";\
     print "    x => AnswerRow(\"howMuchOtherCompanyBenefit.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchOtherCompanyBenefitController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchOtherCompanyBenefit completed"

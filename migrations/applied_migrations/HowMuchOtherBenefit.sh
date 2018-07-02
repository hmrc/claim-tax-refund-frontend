#!/bin/bash

echo "Applying migration HowMuchOtherBenefit"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchOtherTaxableBenefit               controllers.HowMuchOtherBenefitController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchOtherTaxableBenefit               controllers.HowMuchOtherBenefitController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchOtherBenefit                        controllers.HowMuchOtherBenefitController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchOtherBenefit                        controllers.HowMuchOtherBenefitController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchOtherTaxableBenefit.title = howMuchOtherTaxableBenefit" >> ../conf/messages.en
echo "howMuchOtherTaxableBenefit.heading = howMuchOtherTaxableBenefit" >> ../conf/messages.en
echo "howMuchOtherTaxableBenefit.checkYourAnswersLabel = howMuchOtherTaxableBenefit" >> ../conf/messages.en
echo "howMuchOtherTaxableBenefit.blank = howMuchOtherTaxableBenefit" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchOtherTaxableBenefit: Option[String] = cacheMap.getEntry[String](HowMuchOtherBenefitId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchOtherTaxableBenefit: Option[AnswerRow] = userAnswers.howMuchOtherTaxableBenefit map {";\
     print "    x => AnswerRow(\"howMuchOtherTaxableBenefit.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchOtherBenefitController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchOtherBenefit completed"

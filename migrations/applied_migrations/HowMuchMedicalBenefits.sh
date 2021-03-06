#!/bin/bash

echo "Applying migration HowMuchMedicalBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchMedicalBenefits               controllers.HowMuchMedicalBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchMedicalBenefits               controllers.HowMuchMedicalBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchMedicalBenefits                        controllers.HowMuchMedicalBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchMedicalBenefits                        controllers.HowMuchMedicalBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchMedicalBenefits.title = howMuchMedicalBenefits" >> ../conf/messages.en
echo "howMuchMedicalBenefits.heading = howMuchMedicalBenefits" >> ../conf/messages.en
echo "howMuchMedicalBenefits.checkYourAnswersLabel = howMuchMedicalBenefits" >> ../conf/messages.en
echo "howMuchMedicalBenefits.blank = howMuchMedicalBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchMedicalBenefits: Option[String] = cacheMap.getEntry[String](HowMuchMedicalBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchMedicalBenefits: Option[AnswerRow] = userAnswers.howMuchMedicalBenefits map {";\
     print "    x => AnswerRow(\"howMuchMedicalBenefits.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchMedicalBenefitsController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchMedicalBenefits completed"

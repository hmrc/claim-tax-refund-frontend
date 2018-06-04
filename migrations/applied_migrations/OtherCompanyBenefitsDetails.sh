#!/bin/bash

echo "Applying migration OtherCompanyBenefitsDetails"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /otherCompanyBenefitsDetails               controllers.OtherCompanyBenefitsDetailsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /otherCompanyBenefitsDetails               controllers.OtherCompanyBenefitsDetailsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOtherCompanyBenefitsDetails                        controllers.OtherCompanyBenefitsDetailsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOtherCompanyBenefitsDetails                        controllers.OtherCompanyBenefitsDetailsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "otherCompanyBenefitsDetails.title = otherCompanyBenefitsDetails" >> ../conf/messages.en
echo "otherCompanyBenefitsDetails.heading = otherCompanyBenefitsDetails" >> ../conf/messages.en
echo "otherCompanyBenefitsDetails.checkYourAnswersLabel = otherCompanyBenefitsDetails" >> ../conf/messages.en
echo "otherCompanyBenefitsDetails.blank = otherCompanyBenefitsDetails" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def otherCompanyBenefitsDetails: Option[String] = cacheMap.getEntry[String](OtherCompanyBenefitsDetailsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherCompanyBenefitsDetails: Option[AnswerRow] = userAnswers.otherCompanyBenefitsDetails map {";\
     print "    x => AnswerRow(\"otherCompanyBenefitsDetails.checkYourAnswersLabel\", s\"$x\", false, routes.OtherCompanyBenefitsDetailsController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration OtherCompanyBenefitsDetails completed"

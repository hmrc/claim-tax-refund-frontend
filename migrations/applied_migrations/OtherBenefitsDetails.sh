#!/bin/bash

echo "Applying migration OtherBenefitsDetails"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /otherBenefitsDetails               controllers.OtherBenefitsDetailsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /otherBenefitsDetails               controllers.OtherBenefitsDetailsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOtherBenefitsDetails                        controllers.OtherBenefitsDetailsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOtherBenefitsDetails                        controllers.OtherBenefitsDetailsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "otherBenefitsDetails.title = otherBenefitsDetails" >> ../conf/messages.en
echo "otherBenefitsDetails.heading = otherBenefitsDetails" >> ../conf/messages.en
echo "otherBenefitsDetails.checkYourAnswersLabel = otherBenefitsDetails" >> ../conf/messages.en
echo "otherBenefitsDetails.blank = otherBenefitsDetails" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def otherBenefitsDetails: Option[String] = cacheMap.getEntry[String](OtherBenefitsDetailsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherBenefitsDetails: Option[AnswerRow] = userAnswers.otherBenefitsDetails map {";\
     print "    x => AnswerRow(\"otherBenefitsDetails.checkYourAnswersLabel\", s\"$x\", false, routes.OtherBenefitsDetailsController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration OtherBenefitsDetails completed"

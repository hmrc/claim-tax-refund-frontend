#!/bin/bash

echo "Applying migration OtherBenefitsName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /otherBenefitsName               controllers.OtherBenefitsNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /otherBenefitsName               controllers.OtherBenefitsNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOtherBenefitsName                        controllers.OtherBenefitsNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOtherBenefitsName                        controllers.OtherBenefitsNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "otherBenefitsName.title = otherBenefitsName" >> ../conf/messages.en
echo "otherBenefitsName.heading = otherBenefitsName" >> ../conf/messages.en
echo "otherBenefitsName.checkYourAnswersLabel = otherBenefitsName" >> ../conf/messages.en
echo "otherBenefitsName.blank = otherBenefitsName" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def otherBenefitsName: Option[String] = cacheMap.getEntry[String](OtherBenefitsNameId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherBenefitsName: Option[AnswerRow] = userAnswers.otherBenefitsName map {";\
     print "    x => AnswerRow(\"otherBenefitsName.checkYourAnswersLabel\", s\"$x\", false, routes.OtherBenefitsNameController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration OtherBenefitsName completed"

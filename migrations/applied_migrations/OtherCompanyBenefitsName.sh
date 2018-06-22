#!/bin/bash

echo "Applying migration OtherCompanyBenefitsName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /otherCompanyBenefitsName               controllers.OtherCompanyBenefitsNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /otherCompanyBenefitsName               controllers.OtherCompanyBenefitsNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOtherCompanyBenefitsName                        controllers.OtherCompanyBenefitsNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOtherCompanyBenefitsName                        controllers.OtherCompanyBenefitsNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "otherCompanyBenefitsName.title = otherCompanyBenefitsName" >> ../conf/messages.en
echo "otherCompanyBenefitsName.heading = otherCompanyBenefitsName" >> ../conf/messages.en
echo "otherCompanyBenefitsName.checkYourAnswersLabel = otherCompanyBenefitsName" >> ../conf/messages.en
echo "otherCompanyBenefitsName.blank = otherCompanyBenefitsName" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def otherCompanyBenefitsName: Option[String] = cacheMap.getEntry[String](OtherCompanyBenefitsNameId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherCompanyBenefitsName: Option[AnswerRow] = userAnswers.otherCompanyBenefitsName map {";\
     print "    x => AnswerRow(\"otherCompanyBenefitsName.checkYourAnswersLabel\", s\"$x\", false, routes.OtherCompanyBenefitsNameController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration OtherCompanyBenefitsName completed"

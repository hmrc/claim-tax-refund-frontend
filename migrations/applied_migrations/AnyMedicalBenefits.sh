#!/bin/bash

echo "Applying migration AnyMedicalBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyMedicalBenefits                       controllers.AnyMedicalBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyMedicalBenefits                       controllers.AnyMedicalBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyMedicalBenefits                       controllers.AnyMedicalBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyMedicalBenefits                       controllers.AnyMedicalBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyMedicalBenefits.title = anyMedicalBenefits" >> ../conf/messages.en
echo "anyMedicalBenefits.heading = anyMedicalBenefits" >> ../conf/messages.en
echo "anyMedicalBenefits.checkYourAnswersLabel = anyMedicalBenefits" >> ../conf/messages.en
echo "anyMedicalBenefits.blank = anyMedicalBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyMedicalBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyMedicalBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyMedicalBenefits: Option[AnswerRow] = userAnswers.anyMedicalBenefits map {";\
     print "    x => AnswerRow(\"anyMedicalBenefits.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyMedicalBenefitsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyMedicalBenefits completed"

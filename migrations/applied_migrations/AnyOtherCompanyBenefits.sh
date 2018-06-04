#!/bin/bash

echo "Applying migration AnyOtherCompanyBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyOtherCompanyBenefits                       controllers.AnyOtherCompanyBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyOtherCompanyBenefits                       controllers.AnyOtherCompanyBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyOtherCompanyBenefits                       controllers.AnyOtherCompanyBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyOtherCompanyBenefits                       controllers.AnyOtherCompanyBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyOtherCompanyBenefits.title = anyOtherCompanyBenefits" >> ../conf/messages.en
echo "anyOtherCompanyBenefits.heading = anyOtherCompanyBenefits" >> ../conf/messages.en
echo "anyOtherCompanyBenefits.checkYourAnswersLabel = anyOtherCompanyBenefits" >> ../conf/messages.en
echo "anyOtherCompanyBenefits.blank = anyOtherCompanyBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyOtherCompanyBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherCompanyBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyOtherCompanyBenefits: Option[AnswerRow] = userAnswers.anyOtherCompanyBenefits map {";\
     print "    x => AnswerRow(\"anyOtherCompanyBenefits.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyOtherCompanyBenefitsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyOtherCompanyBenefits completed"

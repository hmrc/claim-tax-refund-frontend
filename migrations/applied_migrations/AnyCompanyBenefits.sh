#!/bin/bash

echo "Applying migration AnyCompanyBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyCompanyBenefits                       controllers.AnyCompanyBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyCompanyBenefits                       controllers.AnyCompanyBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyCompanyBenefits                       controllers.AnyCompanyBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyCompanyBenefits                       controllers.AnyCompanyBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyCompanyBenefits.title = anyCompanyBenefits" >> ../conf/messages.en
echo "anyCompanyBenefits.heading = anyCompanyBenefits" >> ../conf/messages.en
echo "anyCompanyBenefits.checkYourAnswersLabel = anyCompanyBenefits" >> ../conf/messages.en
echo "anyCompanyBenefits.blank = anyCompanyBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyCompanyBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyCompanyBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyCompanyBenefits: Option[AnswerRow] = userAnswers.anyCompanyBenefits map {";\
     print "    x => AnswerRow(\"anyCompanyBenefits.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyCompanyBenefitsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyCompanyBenefits completed"

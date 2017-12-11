#!/bin/bash

echo "Applying migration AnyCarBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyCarBenefits                       controllers.AnyCarBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyCarBenefits                       controllers.AnyCarBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyCarBenefits                       controllers.AnyCarBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyCarBenefits                       controllers.AnyCarBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyCarBenefits.title = anyCarBenefits" >> ../conf/messages.en
echo "anyCarBenefits.heading = anyCarBenefits" >> ../conf/messages.en
echo "anyCarBenefits.checkYourAnswersLabel = anyCarBenefits" >> ../conf/messages.en
echo "anyCarBenefits.blank = anyCarBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyCarBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyCarBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyCarBenefits: Option[AnswerRow] = userAnswers.anyCarBenefits map {";\
     print "    x => AnswerRow(\"anyCarBenefits.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyCarBenefitsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyCarBenefits completed"

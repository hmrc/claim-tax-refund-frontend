#!/bin/bash

echo "Applying migration AnyOtherBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyOtherBenefits                       controllers.AnyOtherBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyOtherBenefits                       controllers.AnyOtherBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyOtherBenefits                       controllers.AnyOtherBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyOtherBenefits                       controllers.AnyOtherBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyOtherBenefits.title = anyOtherBenefits" >> ../conf/messages.en
echo "anyOtherBenefits.heading = anyOtherBenefits" >> ../conf/messages.en
echo "anyOtherBenefits.checkYourAnswersLabel = anyOtherBenefits" >> ../conf/messages.en
echo "anyOtherBenefits.blank = anyOtherBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyOtherBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyOtherBenefits: Option[AnswerRow] = userAnswers.anyOtherBenefits map {";\
     print "    x => AnswerRow(\"anyOtherBenefits.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyOtherBenefitsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyOtherBenefits completed"

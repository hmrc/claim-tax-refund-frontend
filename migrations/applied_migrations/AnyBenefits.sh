#!/bin/bash

echo "Applying migration AnyBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyBenefits                       controllers.AnyBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyBenefits                       controllers.AnyBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyBenefits                       controllers.AnyBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyBenefits                       controllers.AnyBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyBenefits.title = anyBenefits" >> ../conf/messages.en
echo "anyBenefits.heading = anyBenefits" >> ../conf/messages.en
echo "anyBenefits.checkYourAnswersLabel = anyBenefits" >> ../conf/messages.en
echo "anyBenefits.blank = anyBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyBenefits: Option[AnswerRow] = userAnswers.anyBenefits map {";\
     print "    x => AnswerRow(\"anyBenefits.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyBenefitsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyBenefits completed"

#!/bin/bash

echo "Applying migration AnyIncapacityBenefit"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyIncapacityBenefit                       controllers.AnyIncapacityBenefitController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyIncapacityBenefit                       controllers.AnyIncapacityBenefitController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyIncapacityBenefit                       controllers.AnyIncapacityBenefitController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyIncapacityBenefit                       controllers.AnyIncapacityBenefitController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyIncapacityBenefit.title = anyIncapacityBenefit" >> ../conf/messages.en
echo "anyIncapacityBenefit.heading = anyIncapacityBenefit" >> ../conf/messages.en
echo "anyIncapacityBenefit.checkYourAnswersLabel = anyIncapacityBenefit" >> ../conf/messages.en
echo "anyIncapacityBenefit.blank = anyIncapacityBenefit" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyIncapacityBenefit: Option[Boolean] = cacheMap.getEntry[Boolean](AnyIncapacityBenefitId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyIncapacityBenefit: Option[AnswerRow] = userAnswers.anyIncapacityBenefit map {";\
     print "    x => AnswerRow(\"anyIncapacityBenefit.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyIncapacityBenefitController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyIncapacityBenefit completed"

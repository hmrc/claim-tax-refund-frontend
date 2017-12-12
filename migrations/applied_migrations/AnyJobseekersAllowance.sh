#!/bin/bash

echo "Applying migration AnyJobseekersAllowance"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyJobseekersAllowance                       controllers.AnyJobseekersAllowanceController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyJobseekersAllowance                       controllers.AnyJobseekersAllowanceController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyJobseekersAllowance                       controllers.AnyJobseekersAllowanceController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyJobseekersAllowance                       controllers.AnyJobseekersAllowanceController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyJobseekersAllowance.title = anyJobseekersAllowance" >> ../conf/messages.en
echo "anyJobseekersAllowance.heading = anyJobseekersAllowance" >> ../conf/messages.en
echo "anyJobseekersAllowance.checkYourAnswersLabel = anyJobseekersAllowance" >> ../conf/messages.en
echo "anyJobseekersAllowance.blank = anyJobseekersAllowance" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyJobseekersAllowance: Option[Boolean] = cacheMap.getEntry[Boolean](AnyJobseekersAllowanceId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyJobseekersAllowance: Option[AnswerRow] = userAnswers.anyJobseekersAllowance map {";\
     print "    x => AnswerRow(\"anyJobseekersAllowance.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyJobseekersAllowanceController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyJobseekersAllowance completed"

#!/bin/bash

echo "Applying migration AnyEmploymentAndSupportAllowance"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyEmploymentAndSupportAllowance                       controllers.AnyEmploymentAndSupportAllowanceController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyEmploymentAndSupportAllowance                       controllers.AnyEmploymentAndSupportAllowanceController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyEmploymentAndSupportAllowance                       controllers.AnyEmploymentAndSupportAllowanceController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyEmploymentAndSupportAllowance                       controllers.AnyEmploymentAndSupportAllowanceController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyEmploymentAndSupportAllowance.title = anyEmploymentAndSupportAllowance" >> ../conf/messages.en
echo "anyEmploymentAndSupportAllowance.heading = anyEmploymentAndSupportAllowance" >> ../conf/messages.en
echo "anyEmploymentAndSupportAllowance.checkYourAnswersLabel = anyEmploymentAndSupportAllowance" >> ../conf/messages.en
echo "anyEmploymentAndSupportAllowance.blank = anyEmploymentAndSupportAllowance" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyEmploymentAndSupportAllowance: Option[Boolean] = cacheMap.getEntry[Boolean](AnyEmploymentAndSupportAllowanceId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyEmploymentAndSupportAllowance: Option[AnswerRow] = userAnswers.anyEmploymentAndSupportAllowance map {";\
     print "    x => AnswerRow(\"anyEmploymentAndSupportAllowance.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyEmploymentAndSupportAllowanceController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyEmploymentAndSupportAllowance completed"

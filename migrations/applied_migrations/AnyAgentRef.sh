#!/bin/bash

echo "Applying migration AnyAgentRef"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyAgentRef                       controllers.AnyAgentRefController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyAgentRef                       controllers.AnyAgentRefController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyAgentRef                       controllers.AnyAgentRefController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyAgentRef                       controllers.AnyAgentRefController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyAgentRef.title = anyAgentRef" >> ../conf/messages.en
echo "anyAgentRef.heading = anyAgentRef" >> ../conf/messages.en
echo "anyAgentRef.checkYourAnswersLabel = anyAgentRef" >> ../conf/messages.en
echo "anyAgentRef.blank = anyAgentRef" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyAgentRef: Option[Boolean] = cacheMap.getEntry[Boolean](AnyAgentRefId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyAgentRef: Option[AnswerRow] = userAnswers.anyAgentRef map {";\
     print "    x => AnswerRow(\"anyAgentRef.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyAgentRefController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyAgentRef completed"

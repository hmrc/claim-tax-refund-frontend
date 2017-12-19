#!/bin/bash

echo "Applying migration IsAgentAddressInUK"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /isAgentAddressInUK                       controllers.IsAgentAddressInUKController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /isAgentAddressInUK                       controllers.IsAgentAddressInUKController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeIsAgentAddressInUK                       controllers.IsAgentAddressInUKController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeIsAgentAddressInUK                       controllers.IsAgentAddressInUKController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "isAgentAddressInUK.title = isAgentAddressInUK" >> ../conf/messages.en
echo "isAgentAddressInUK.heading = isAgentAddressInUK" >> ../conf/messages.en
echo "isAgentAddressInUK.checkYourAnswersLabel = isAgentAddressInUK" >> ../conf/messages.en
echo "isAgentAddressInUK.blank = isAgentAddressInUK" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def isAgentAddressInUK: Option[Boolean] = cacheMap.getEntry[Boolean](IsAgentAddressInUKId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def isAgentAddressInUK: Option[AnswerRow] = userAnswers.isAgentAddressInUK map {";\
     print "    x => AnswerRow(\"isAgentAddressInUK.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.IsAgentAddressInUKController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration IsAgentAddressInUK completed"

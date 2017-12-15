#!/bin/bash

echo "Applying migration AgentReferenceNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /agentReferenceNumber               controllers.AgentReferenceNumberController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /agentReferenceNumber               controllers.AgentReferenceNumberController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAgentReferenceNumber                        controllers.AgentReferenceNumberController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAgentReferenceNumber                        controllers.AgentReferenceNumberController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "agentReferenceNumber.title = agentReferenceNumber" >> ../conf/messages.en
echo "agentReferenceNumber.heading = agentReferenceNumber" >> ../conf/messages.en
echo "agentReferenceNumber.checkYourAnswersLabel = agentReferenceNumber" >> ../conf/messages.en
echo "agentReferenceNumber.blank = agentReferenceNumber" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def agentReferenceNumber: Option[String] = cacheMap.getEntry[String](AgentReferenceNumberId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def agentReferenceNumber: Option[AnswerRow] = userAnswers.agentReferenceNumber map {";\
     print "    x => AnswerRow(\"agentReferenceNumber.checkYourAnswersLabel\", s\"$x\", false, routes.AgentReferenceNumberController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AgentReferenceNumber completed"

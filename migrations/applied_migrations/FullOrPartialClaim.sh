#!/bin/bash

echo "Applying migration FullOrPartialClaim"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /fullOrPartialClaim               controllers.FullOrPartialClaimController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /fullOrPartialClaim               controllers.FullOrPartialClaimController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeFullOrPartialClaim               controllers.FullOrPartialClaimController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeFullOrPartialClaim               controllers.FullOrPartialClaimController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "fullOrPartialClaim.title = fullOrPartialClaim" >> ../conf/messages.en
echo "fullOrPartialClaim.heading = fullOrPartialClaim" >> ../conf/messages.en
echo "fullOrPartialClaim.option1 = fullOrPartialClaim" Option 1 >> ../conf/messages.en
echo "fullOrPartialClaim.option2 = fullOrPartialClaim" Option 2 >> ../conf/messages.en
echo "fullOrPartialClaim.checkYourAnswersLabel = fullOrPartialClaim" >> ../conf/messages.en
echo "fullOrPartialClaim.blank = fullOrPartialClaim" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def fullOrPartialClaim: Option[FullOrPartialClaim] = cacheMap.getEntry[FullOrPartialClaim](FullOrPartialClaimId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def fullOrPartialClaim: Option[AnswerRow] = userAnswers.fullOrPartialClaim map {";\
     print "    x => AnswerRow(\"fullOrPartialClaim.checkYourAnswersLabel\", s\"fullOrPartialClaim.$x\", true, routes.FullOrPartialClaimController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration FullOrPartialClaim completed"

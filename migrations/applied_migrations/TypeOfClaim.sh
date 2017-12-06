#!/bin/bash

echo "Applying migration TypeOfClaim"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /typeOfClaim               controllers.TypeOfClaimController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /typeOfClaim               controllers.TypeOfClaimController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTypeOfClaim               controllers.TypeOfClaimController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTypeOfClaim               controllers.TypeOfClaimController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "typeOfClaim.title = typeOfClaim" >> ../conf/messages.en
echo "typeOfClaim.heading = typeOfClaim" >> ../conf/messages.en
echo "typeOfClaim.option1 = typeOfClaim" Option 1 >> ../conf/messages.en
echo "typeOfClaim.option2 = typeOfClaim" Option 2 >> ../conf/messages.en
echo "typeOfClaim.checkYourAnswersLabel = typeOfClaim" >> ../conf/messages.en
echo "typeOfClaim.blank = typeOfClaim" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def typeOfClaim: Option[TypeOfClaim] = cacheMap.getEntry[TypeOfClaim](TypeOfClaimId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def typeOfClaim: Option[AnswerRow] = userAnswers.typeOfClaim map {";\
     print "    x => AnswerRow(\"typeOfClaim.checkYourAnswersLabel\", s\"typeOfClaim.$x\", true, routes.TypeOfClaimController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration TypeOfClaim completed"

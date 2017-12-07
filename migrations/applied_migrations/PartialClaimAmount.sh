#!/bin/bash

echo "Applying migration PartialClaimAmount"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /partialClaimAmount               controllers.PartialClaimAmountController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /partialClaimAmount               controllers.PartialClaimAmountController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePartialClaimAmount                        controllers.PartialClaimAmountController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePartialClaimAmount                        controllers.PartialClaimAmountController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "partialClaimAmount.title = partialClaimAmount" >> ../conf/messages.en
echo "partialClaimAmount.heading = partialClaimAmount" >> ../conf/messages.en
echo "partialClaimAmount.checkYourAnswersLabel = partialClaimAmount" >> ../conf/messages.en
echo "partialClaimAmount.blank = partialClaimAmount" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def partialClaimAmount: Option[String] = cacheMap.getEntry[String](PartialClaimAmountId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def partialClaimAmount: Option[AnswerRow] = userAnswers.partialClaimAmount map {";\
     print "    x => AnswerRow(\"partialClaimAmount.checkYourAnswersLabel\", s\"$x\", false, routes.PartialClaimAmountController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration PartialClaimAmount completed"

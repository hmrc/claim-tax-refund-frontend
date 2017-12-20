#!/bin/bash

echo "Applying migration IsPayeeAddressInTheUK"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /isPayeeAddressInTheUK                       controllers.IsPayeeAddressInTheUKController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /isPayeeAddressInTheUK                       controllers.IsPayeeAddressInTheUKController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeIsPayeeAddressInTheUK                       controllers.IsPayeeAddressInTheUKController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeIsPayeeAddressInTheUK                       controllers.IsPayeeAddressInTheUKController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "isPayeeAddressInTheUK.title = isPayeeAddressInTheUK" >> ../conf/messages.en
echo "isPayeeAddressInTheUK.heading = isPayeeAddressInTheUK" >> ../conf/messages.en
echo "isPayeeAddressInTheUK.checkYourAnswersLabel = isPayeeAddressInTheUK" >> ../conf/messages.en
echo "isPayeeAddressInTheUK.blank = isPayeeAddressInTheUK" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def isPayeeAddressInTheUK: Option[Boolean] = cacheMap.getEntry[Boolean](IsPayeeAddressInTheUKId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def isPayeeAddressInTheUK: Option[AnswerRow] = userAnswers.isPayeeAddressInTheUK map {";\
     print "    x => AnswerRow(\"isPayeeAddressInTheUK.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.IsPayeeAddressInTheUKController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration IsPayeeAddressInTheUK completed"

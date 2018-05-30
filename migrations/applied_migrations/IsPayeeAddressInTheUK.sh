#!/bin/bash

echo "Applying migration IsPaymentAddressInTheUK"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /isPaymentAddressInTheUK                       controllers.IsPaymentAddressInTheUKController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /isPaymentAddressInTheUK                       controllers.IsPaymentAddressInTheUKController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeIsPaymentAddressInTheUK                       controllers.IsPaymentAddressInTheUKController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeIsPaymentAddressInTheUK                       controllers.IsPaymentAddressInTheUKController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "isPaymentAddressInTheUK.title = isPaymentAddressInTheUK" >> ../conf/messages.en
echo "isPaymentAddressInTheUK.heading = isPaymentAddressInTheUK" >> ../conf/messages.en
echo "isPaymentAddressInTheUK.checkYourAnswersLabel = isPaymentAddressInTheUK" >> ../conf/messages.en
echo "isPaymentAddressInTheUK.blank = isPaymentAddressInTheUK" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def isPaymentAddressInTheUK: Option[Boolean] = cacheMap.getEntry[Boolean](IsPaymentAddressInTheUKId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def isPaymentAddressInTheUK: Option[AnswerRow] = userAnswers.isPaymentAddressInTheUK map {";\
     print "    x => AnswerRow(\"isPaymentAddressInTheUK.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration IsPaymentAddressInTheUK completed"

#!/bin/bash

echo "Applying migration PaymentAddressCorrect"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /paymentAddressCorrect                       controllers.PaymentAddressCorrectController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /paymentAddressCorrect                       controllers.PaymentAddressCorrectController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePaymentAddressCorrect                       controllers.PaymentAddressCorrectController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePaymentAddressCorrect                       controllers.PaymentAddressCorrectController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "paymentAddressCorrect.title = paymentAddressCorrect" >> ../conf/messages.en
echo "paymentAddressCorrect.heading = paymentAddressCorrect" >> ../conf/messages.en
echo "paymentAddressCorrect.checkYourAnswersLabel = paymentAddressCorrect" >> ../conf/messages.en
echo "paymentAddressCorrect.blank = paymentAddressCorrect" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def paymentAddressCorrect: Option[Boolean] = cacheMap.getEntry[Boolean](PaymentAddressCorrectId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def paymentAddressCorrect: Option[AnswerRow] = userAnswers.paymentAddressCorrect map {";\
     print "    x => AnswerRow(\"paymentAddressCorrect.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.PaymentAddressCorrectController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration PaymentAddressCorrect completed"

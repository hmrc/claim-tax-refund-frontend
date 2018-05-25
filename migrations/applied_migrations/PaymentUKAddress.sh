#!/bin/bash

echo "Applying migration PaymentUKAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /paymentUKAddress                       controllers.PaymentUKAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /paymentUKAddress                       controllers.PaymentUKAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePaymentUKAddress                       controllers.PaymentUKAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePaymentUKAddress                       controllers.PaymentUKAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "paymentUKAddress.title = paymentUKAddress" >> ../conf/messages.en
echo "paymentUKAddress.heading = paymentUKAddress" >> ../conf/messages.en
echo "paymentUKAddress.field1 = Field 1" >> ../conf/messages.en
echo "paymentUKAddress.field2 = Field 2" >> ../conf/messages.en
echo "paymentUKAddress.checkYourAnswersLabel = paymentUKAddress" >> ../conf/messages.en
echo "paymentUKAddress.field1.blank = paymentUKAddress field 1 blank" >> ../conf/messages.en
echo "paymentUKAddress.field2.blank = paymentUKAddress field 2 blank" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def paymentUKAddress: Option[PaymentUKAddress] = cacheMap.getEntry[PaymentUKAddress](PaymentUKAddressId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def paymentUKAddress: Option[AnswerRow] = userAnswers.paymentUKAddress map {";\
     print "    x => AnswerRow(\"paymentUKAddress.checkYourAnswersLabel\", s\"${x.field1} ${x.field2}\", false, routes.PaymentUKAddressController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration PaymentUKAddress completed"

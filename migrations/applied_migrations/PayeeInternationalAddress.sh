#!/bin/bash

echo "Applying migration PaymentInternationalAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /paymentInternationalAddress                       controllers.PaymentInternationalAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /paymentInternationalAddress                       controllers.PaymentInternationalAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePaymentInternationalAddress                       controllers.PaymentInternationalAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePaymentInternationalAddress                       controllers.PaymentInternationalAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "paymentInternationalAddress.title = paymentInternationalAddress" >> ../conf/messages.en
echo "paymentInternationalAddress.heading = paymentInternationalAddress" >> ../conf/messages.en
echo "paymentInternationalAddress.field1 = Field 1" >> ../conf/messages.en
echo "paymentInternationalAddress.field2 = Field 2" >> ../conf/messages.en
echo "paymentInternationalAddress.checkYourAnswersLabel = paymentInternationalAddress" >> ../conf/messages.en
echo "paymentInternationalAddress.field1.blank = paymentInternationalAddress field 1 blank" >> ../conf/messages.en
echo "paymentInternationalAddress.field2.blank = paymentInternationalAddress field 2 blank" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def paymentInternationalAddress: Option[PaymentInternationalAddress] = cacheMap.getEntry[PaymentInternationalAddress](PaymentInternationalAddressId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def paymentInternationalAddress: Option[AnswerRow] = userAnswers.paymentInternationalAddress map {";\
     print "    x => AnswerRow(\"paymentInternationalAddress.checkYourAnswersLabel\", s\"${x.field1} ${x.field2}\", false, routes.PaymentInternationalAddressController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration PaymentInternationalAddress completed"

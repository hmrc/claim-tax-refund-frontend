#!/bin/bash

echo "Applying migration PayeeInternationalAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /payeeInternationalAddress                       controllers.PayeeInternationalAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /payeeInternationalAddress                       controllers.PayeeInternationalAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePayeeInternationalAddress                       controllers.PayeeInternationalAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePayeeInternationalAddress                       controllers.PayeeInternationalAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "payeeInternationalAddress.title = payeeInternationalAddress" >> ../conf/messages.en
echo "payeeInternationalAddress.heading = payeeInternationalAddress" >> ../conf/messages.en
echo "payeeInternationalAddress.field1 = Field 1" >> ../conf/messages.en
echo "payeeInternationalAddress.field2 = Field 2" >> ../conf/messages.en
echo "payeeInternationalAddress.checkYourAnswersLabel = payeeInternationalAddress" >> ../conf/messages.en
echo "payeeInternationalAddress.field1.blank = payeeInternationalAddress field 1 blank" >> ../conf/messages.en
echo "payeeInternationalAddress.field2.blank = payeeInternationalAddress field 2 blank" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def payeeInternationalAddress: Option[PayeeInternationalAddress] = cacheMap.getEntry[PayeeInternationalAddress](PayeeInternationalAddressId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def payeeInternationalAddress: Option[AnswerRow] = userAnswers.payeeInternationalAddress map {";\
     print "    x => AnswerRow(\"payeeInternationalAddress.checkYourAnswersLabel\", s\"${x.field1} ${x.field2}\", false, routes.PayeeInternationalAddressController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration PayeeInternationalAddress completed"

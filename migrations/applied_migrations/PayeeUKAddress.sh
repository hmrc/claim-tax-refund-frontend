#!/bin/bash

echo "Applying migration PayeeUKAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /payeeUKAddress                       controllers.PayeeUKAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /payeeUKAddress                       controllers.PayeeUKAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePayeeUKAddress                       controllers.PayeeUKAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePayeeUKAddress                       controllers.PayeeUKAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "payeeUKAddress.title = payeeUKAddress" >> ../conf/messages.en
echo "payeeUKAddress.heading = payeeUKAddress" >> ../conf/messages.en
echo "payeeUKAddress.field1 = Field 1" >> ../conf/messages.en
echo "payeeUKAddress.field2 = Field 2" >> ../conf/messages.en
echo "payeeUKAddress.checkYourAnswersLabel = payeeUKAddress" >> ../conf/messages.en
echo "payeeUKAddress.field1.blank = payeeUKAddress field 1 blank" >> ../conf/messages.en
echo "payeeUKAddress.field2.blank = payeeUKAddress field 2 blank" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def payeeUKAddress: Option[PayeeUKAddress] = cacheMap.getEntry[PayeeUKAddress](PayeeUKAddressId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def payeeUKAddress: Option[AnswerRow] = userAnswers.payeeUKAddress map {";\
     print "    x => AnswerRow(\"payeeUKAddress.checkYourAnswersLabel\", s\"${x.field1} ${x.field2}\", false, routes.PayeeUKAddressController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration PayeeUKAddress completed"

#!/bin/bash

echo "Applying migration UkAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /ukAddress                       controllers.UkAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /ukAddress                       controllers.UkAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeUkAddress                       controllers.UkAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeUkAddress                       controllers.UkAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "ukAddress.title = ukAddress" >> ../conf/messages.en
echo "ukAddress.heading = ukAddress" >> ../conf/messages.en
echo "ukAddress.field1 = Field 1" >> ../conf/messages.en
echo "ukAddress.field2 = Field 2" >> ../conf/messages.en
echo "ukAddress.checkYourAnswersLabel = ukAddress" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def ukAddress: Option[UkAddress] = cacheMap.getEntry[UkAddress](UkAddressId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def ukAddress: Option[AnswerRow] = userAnswers.ukAddress map {";\
     print "    x => AnswerRow(\"ukAddress.checkYourAnswersLabel\", s\"${x.field1} ${x.field2}\", false, routes.UkAddressController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration UkAddress completed"

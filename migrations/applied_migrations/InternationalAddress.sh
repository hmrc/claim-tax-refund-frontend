#!/bin/bash

echo "Applying migration InternationalAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /internationalAddress                       controllers.InternationalAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /internationalAddress                       controllers.InternationalAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeInternationalAddress                       controllers.InternationalAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeInternationalAddress                       controllers.InternationalAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "internationalAddress.title = internationalAddress" >> ../conf/messages.en
echo "internationalAddress.heading = internationalAddress" >> ../conf/messages.en
echo "internationalAddress.field1 = Field 1" >> ../conf/messages.en
echo "internationalAddress.field2 = Field 2" >> ../conf/messages.en
echo "internationalAddress.checkYourAnswersLabel = internationalAddress" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def internationalAddress: Option[InternationalAddress] = cacheMap.getEntry[InternationalAddress](InternationalAddressId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def internationalAddress: Option[AnswerRow] = userAnswers.internationalAddress map {";\
     print "    x => AnswerRow(\"internationalAddress.checkYourAnswersLabel\", s\"${x.field1} ${x.field2}\", false, routes.InternationalAddressController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration InternationalAddress completed"

#!/bin/bash

echo "Applying migration TelephoneNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /telephoneNumber               controllers.TelephoneNumberController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /telephoneNumber               controllers.TelephoneNumberController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTelephoneNumber                        controllers.TelephoneNumberController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTelephoneNumber                        controllers.TelephoneNumberController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "telephoneNumber.title = telephoneNumber" >> ../conf/messages.en
echo "telephoneNumber.heading = telephoneNumber" >> ../conf/messages.en
echo "telephoneNumber.checkYourAnswersLabel = telephoneNumber" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def telephoneNumber: Option[String] = cacheMap.getEntry[String](TelephoneNumberId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def telephoneNumber: Option[AnswerRow] = userAnswers.telephoneNumber map {";\
     print "    x => AnswerRow(\"telephoneNumber.checkYourAnswersLabel\", s\"$x\", false, routes.TelephoneNumberController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration TelephoneNumber completed"

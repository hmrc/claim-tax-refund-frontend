#!/bin/bash

echo "Applying migration NationalInsuranceNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /nationalInsuranceNumber               controllers.NationalInsuranceNumberController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /nationalInsuranceNumber               controllers.NationalInsuranceNumberController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeNationalInsuranceNumber                        controllers.NationalInsuranceNumberController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeNationalInsuranceNumber                        controllers.NationalInsuranceNumberController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "nationalInsuranceNumber.title = nationalInsuranceNumber" >> ../conf/messages.en
echo "nationalInsuranceNumber.heading = nationalInsuranceNumber" >> ../conf/messages.en
echo "nationalInsuranceNumber.checkYourAnswersLabel = nationalInsuranceNumber" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def nationalInsuranceNumber: Option[String] = cacheMap.getEntry[String](NationalInsuranceNumberId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def nationalInsuranceNumber: Option[AnswerRow] = userAnswers.nationalInsuranceNumber map {";\
     print "    x => AnswerRow(\"nationalInsuranceNumber.checkYourAnswersLabel\", s\"$x\", false, routes.NationalInsuranceNumberController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration NationalInsuranceNumber completed"

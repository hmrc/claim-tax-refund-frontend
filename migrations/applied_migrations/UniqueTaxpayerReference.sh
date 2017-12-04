#!/bin/bash

echo "Applying migration UniqueTaxpayerReference"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /uniqueTaxpayerReference               controllers.UniqueTaxpayerReferenceController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /uniqueTaxpayerReference               controllers.UniqueTaxpayerReferenceController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeUniqueTaxpayerReference                        controllers.UniqueTaxpayerReferenceController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeUniqueTaxpayerReference                        controllers.UniqueTaxpayerReferenceController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "uniqueTaxpayerReference.title = uniqueTaxpayerReference" >> ../conf/messages.en
echo "uniqueTaxpayerReference.heading = uniqueTaxpayerReference" >> ../conf/messages.en
echo "uniqueTaxpayerReference.checkYourAnswersLabel = uniqueTaxpayerReference" >> ../conf/messages.en
echo "uniqueTaxpayerReference.blank = uniqueTaxpayerReference" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def uniqueTaxpayerReference: Option[String] = cacheMap.getEntry[String](UniqueTaxpayerReferenceId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def uniqueTaxpayerReference: Option[AnswerRow] = userAnswers.uniqueTaxpayerReference map {";\
     print "    x => AnswerRow(\"uniqueTaxpayerReference.checkYourAnswersLabel\", s\"$x\", false, routes.UniqueTaxpayerReferenceController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration UniqueTaxpayerReference completed"

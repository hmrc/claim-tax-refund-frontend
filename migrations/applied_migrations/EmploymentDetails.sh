#!/bin/bash

echo "Applying migration EmploymentDetails"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /employmentDetails               controllers.EmploymentDetailsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /employmentDetails               controllers.EmploymentDetailsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeEmploymentDetails                        controllers.EmploymentDetailsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeEmploymentDetails                        controllers.EmploymentDetailsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "employmentDetails.title = employmentDetails" >> ../conf/messages.en
echo "employmentDetails.heading = employmentDetails" >> ../conf/messages.en
echo "employmentDetails.checkYourAnswersLabel = employmentDetails" >> ../conf/messages.en
echo "employmentDetails.blank = employmentDetails" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def employmentDetails: Option[String] = cacheMap.getEntry[String](EmploymentDetailsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def employmentDetails: Option[AnswerRow] = userAnswers.employmentDetails map {";\
     print "    x => AnswerRow(\"employmentDetails.checkYourAnswersLabel\", s\"$x\", false, routes.EmploymentDetailsController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration EmploymentDetails completed"

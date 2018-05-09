#!/bin/bash

echo "Applying migration TaiEmploymentDetails"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /taiEmploymentDetails                       controllers.TaiEmploymentDetailsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /taiEmploymentDetails                       controllers.TaiEmploymentDetailsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTaiEmploymentDetails                       controllers.TaiEmploymentDetailsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTaiEmploymentDetails                       controllers.TaiEmploymentDetailsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "taiEmploymentDetails.title = taiEmploymentDetails" >> ../conf/messages.en
echo "taiEmploymentDetails.heading = taiEmploymentDetails" >> ../conf/messages.en
echo "taiEmploymentDetails.checkYourAnswersLabel = taiEmploymentDetails" >> ../conf/messages.en
echo "taiEmploymentDetails.blank = taiEmploymentDetails" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def taiEmploymentDetails: Option[Boolean] = cacheMap.getEntry[Boolean](TaiEmploymentDetailsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def taiEmploymentDetails: Option[AnswerRow] = userAnswers.taiEmploymentDetails map {";\
     print "    x => AnswerRow(\"taiEmploymentDetails.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.TaiEmploymentDetailsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration TaiEmploymentDetails completed"

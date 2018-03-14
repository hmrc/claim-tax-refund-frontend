#!/bin/bash

echo "Applying migration UserDetails"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /userDetails               controllers.UserDetailsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /userDetails               controllers.UserDetailsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeUserDetails                        controllers.UserDetailsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeUserDetails                        controllers.UserDetailsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "userDetails.title = userDetails" >> ../conf/messages.en
echo "userDetails.heading = userDetails" >> ../conf/messages.en
echo "userDetails.checkYourAnswersLabel = userDetails" >> ../conf/messages.en
echo "userDetails.blank = userDetails" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def userDetails: Option[String] = cacheMap.getEntry[String](UserDetailsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def userDetails: Option[AnswerRow] = userAnswers.userDetails map {";\
     print "    x => AnswerRow(\"userDetails.checkYourAnswersLabel\", s\"$x\", false, routes.UserDetailsController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration UserDetails completed"

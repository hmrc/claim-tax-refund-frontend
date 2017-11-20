#!/bin/bash

echo "Applying migration IsTheAddressInTheUK"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /isTheAddressInTheUK                       controllers.IsTheAddressInTheUKController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /isTheAddressInTheUK                       controllers.IsTheAddressInTheUKController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeIsTheAddressInTheUK                       controllers.IsTheAddressInTheUKController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeIsTheAddressInTheUK                       controllers.IsTheAddressInTheUKController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "isTheAddressInTheUK.title = isTheAddressInTheUK" >> ../conf/messages.en
echo "isTheAddressInTheUK.heading = isTheAddressInTheUK" >> ../conf/messages.en
echo "isTheAddressInTheUK.checkYourAnswersLabel = isTheAddressInTheUK" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def isTheAddressInTheUK: Option[Boolean] = cacheMap.getEntry[Boolean](IsTheAddressInTheUKId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def isTheAddressInTheUK: Option[AnswerRow] = userAnswers.isTheAddressInTheUK map {";\
     print "    x => AnswerRow(\"isTheAddressInTheUK.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.IsTheAddressInTheUKController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration IsTheAddressInTheUK completed"

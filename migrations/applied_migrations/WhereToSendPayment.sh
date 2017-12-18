#!/bin/bash

echo "Applying migration WhereToSendPayment"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whereToSendPayment                       controllers.WhereToSendPaymentController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whereToSendPayment                       controllers.WhereToSendPaymentController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhereToSendPayment                       controllers.WhereToSendPaymentController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhereToSendPayment                       controllers.WhereToSendPaymentController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whereToSendPayment.title = whereToSendPayment" >> ../conf/messages.en
echo "whereToSendPayment.heading = whereToSendPayment" >> ../conf/messages.en
echo "whereToSendPayment.checkYourAnswersLabel = whereToSendPayment" >> ../conf/messages.en
echo "whereToSendPayment.blank = whereToSendPayment" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def whereToSendPayment: Option[Boolean] = cacheMap.getEntry[Boolean](WhereToSendPaymentId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def whereToSendPayment: Option[AnswerRow] = userAnswers.whereToSendPayment map {";\
     print "    x => AnswerRow(\"whereToSendPayment.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.WhereToSendPaymentController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration WhereToSendPayment completed"

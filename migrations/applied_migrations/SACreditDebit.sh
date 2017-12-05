#!/bin/bash

echo "Applying migration SACreditDebit"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /sACreditDebit                       controllers.SACreditDebitController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /sACreditDebit                       controllers.SACreditDebitController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSACreditDebit                       controllers.SACreditDebitController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSACreditDebit                       controllers.SACreditDebitController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "sACreditDebit.title = sACreditDebit" >> ../conf/messages.en
echo "sACreditDebit.heading = sACreditDebit" >> ../conf/messages.en
echo "sACreditDebit.checkYourAnswersLabel = sACreditDebit" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def sACreditDebit: Option[Boolean] = cacheMap.getEntry[Boolean](SACreditDebitId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def sACreditDebit: Option[AnswerRow] = userAnswers.sACreditDebit map {";\
     print "    x => AnswerRow(\"sACreditDebit.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.SACreditDebitController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SACreditDebit completed"

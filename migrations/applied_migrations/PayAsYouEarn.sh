#!/bin/bash

echo "Applying migration PayAsYouEarn"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /payAsYouEarn               controllers.PayAsYouEarnController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /payAsYouEarn               controllers.PayAsYouEarnController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePayAsYouEarn                        controllers.PayAsYouEarnController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePayAsYouEarn                        controllers.PayAsYouEarnController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "payAsYouEarn.title = payAsYouEarn" >> ../conf/messages.en
echo "payAsYouEarn.heading = payAsYouEarn" >> ../conf/messages.en
echo "payAsYouEarn.checkYourAnswersLabel = payAsYouEarn" >> ../conf/messages.en
echo "payAsYouEarn.blank = payAsYouEarn" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def payAsYouEarn: Option[String] = cacheMap.getEntry[String](PayAsYouEarnId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def payAsYouEarn: Option[AnswerRow] = userAnswers.payAsYouEarn map {";\
     print "    x => AnswerRow(\"payAsYouEarn.checkYourAnswersLabel\", s\"$x\", false, routes.PayAsYouEarnController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration PayAsYouEarn completed"

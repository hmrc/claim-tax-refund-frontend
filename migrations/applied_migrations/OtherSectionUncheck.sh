#!/bin/bash

echo "Applying migration RemoveOtherSelectedOption"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /RemoveOtherSelectedOption                       controllers.RemoveOtherSelectedOptionController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /RemoveOtherSelectedOption                       controllers.RemoveOtherSelectedOptionController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeRemoveOtherSelectedOption                       controllers.RemoveOtherSelectedOptionController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeRemoveOtherSelectedOption                       controllers.RemoveOtherSelectedOptionController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "RemoveOtherSelectedOption.title = RemoveOtherSelectedOption" >> ../conf/messages.en
echo "RemoveOtherSelectedOption.heading = RemoveOtherSelectedOption" >> ../conf/messages.en
echo "RemoveOtherSelectedOption.checkYourAnswersLabel = RemoveOtherSelectedOption" >> ../conf/messages.en
echo "RemoveOtherSelectedOption.blank = RemoveOtherSelectedOption" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def RemoveOtherSelectedOption: Option[Boolean] = cacheMap.getEntry[Boolean](RemoveOtherSelectedOptionId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def RemoveOtherSelectedOption: Option[AnswerRow] = userAnswers.RemoveOtherSelectedOption map {";\
     print "    x => AnswerRow(\"RemoveOtherSelectedOption.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.RemoveOtherSelectedOptionController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration RemoveOtherSelectedOption completed"

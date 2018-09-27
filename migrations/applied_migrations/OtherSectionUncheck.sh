#!/bin/bash

echo "Applying migration OtherSectionUncheck"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /otherSectionUncheck                       controllers.OtherSectionUncheckController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /otherSectionUncheck                       controllers.OtherSectionUncheckController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOtherSectionUncheck                       controllers.OtherSectionUncheckController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOtherSectionUncheck                       controllers.OtherSectionUncheckController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "otherSectionUncheck.title = otherSectionUncheck" >> ../conf/messages.en
echo "otherSectionUncheck.heading = otherSectionUncheck" >> ../conf/messages.en
echo "otherSectionUncheck.checkYourAnswersLabel = otherSectionUncheck" >> ../conf/messages.en
echo "otherSectionUncheck.blank = otherSectionUncheck" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def otherSectionUncheck: Option[Boolean] = cacheMap.getEntry[Boolean](OtherSectionUncheckId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherSectionUncheck: Option[AnswerRow] = userAnswers.otherSectionUncheck map {";\
     print "    x => AnswerRow(\"otherSectionUncheck.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.OtherSectionUncheckController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration OtherSectionUncheck completed"

#!/bin/bash

echo "Applying migration DeleteOther"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /deleteOther                       controllers.DeleteOtherController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /deleteOther                       controllers.DeleteOtherController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDeleteOther                       controllers.DeleteOtherController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDeleteOther                       controllers.DeleteOtherController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "deleteOther.title = deleteOther" >> ../conf/messages.en
echo "deleteOther.heading = deleteOther" >> ../conf/messages.en
echo "deleteOther.checkYourAnswersLabel = deleteOther" >> ../conf/messages.en
echo "deleteOther.blank = deleteOther" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def deleteOther: Option[Boolean] = cacheMap.getEntry[Boolean](DeleteOtherId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def deleteOther: Option[AnswerRow] = userAnswers.deleteOther map {";\
     print "    x => AnswerRow(\"deleteOther.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.DeleteOtherController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration DeleteOther completed"

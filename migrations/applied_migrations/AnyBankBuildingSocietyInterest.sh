#!/bin/bash

echo "Applying migration AnyBankBuildingSocietyInterest"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyBankBuildingSocietyInterest                       controllers.AnyBankBuildingSocietyInterestController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyBankBuildingSocietyInterest                       controllers.AnyBankBuildingSocietyInterestController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyBankBuildingSocietyInterest                       controllers.AnyBankBuildingSocietyInterestController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyBankBuildingSocietyInterest                       controllers.AnyBankBuildingSocietyInterestController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyBankBuildingSocietyInterest.title = anyBankBuildingSocietyInterest" >> ../conf/messages.en
echo "anyBankBuildingSocietyInterest.heading = anyBankBuildingSocietyInterest" >> ../conf/messages.en
echo "anyBankBuildingSocietyInterest.checkYourAnswersLabel = anyBankBuildingSocietyInterest" >> ../conf/messages.en
echo "anyBankBuildingSocietyInterest.blank = anyBankBuildingSocietyInterest" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyBankBuildingSocietyInterest: Option[Boolean] = cacheMap.getEntry[Boolean](AnyBankBuildingSocietyInterestId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyBankBuildingSocietyInterest: Option[AnswerRow] = userAnswers.anyBankBuildingSocietyInterest map {";\
     print "    x => AnswerRow(\"anyBankBuildingSocietyInterest.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyBankBuildingSocietyInterestController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyBankBuildingSocietyInterest completed"

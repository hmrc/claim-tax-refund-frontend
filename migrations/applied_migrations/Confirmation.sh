#!/bin/bash

echo "Applying migration Confirmation"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /confirmation               controllers.ConfirmationController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /confirmation               controllers.ConfirmationController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeConfirmation                        controllers.ConfirmationController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeConfirmation                        controllers.ConfirmationController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "confirmation.title = confirmation" >> ../conf/messages.en
echo "confirmation.heading = confirmation" >> ../conf/messages.en
echo "confirmation.checkYourAnswersLabel = confirmation" >> ../conf/messages.en
echo "confirmation.blank = confirmation" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def confirmation: Option[String] = cacheMap.getEntry[String](ConfirmationId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def confirmation: Option[AnswerRow] = userAnswers.confirmation map {";\
     print "    x => AnswerRow(\"confirmation.checkYourAnswersLabel\", s\"$x\", false, routes.ConfirmationController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration Confirmation completed"

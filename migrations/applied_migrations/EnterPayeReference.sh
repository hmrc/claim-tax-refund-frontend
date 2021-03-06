#!/bin/bash

echo "Applying migration EnterPayeReference"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /enterPayeReference               controllers.EnterPayeReferenceController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /enterPayeReference               controllers.EnterPayeReferenceController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeEnterPayeReference                        controllers.EnterPayeReferenceController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeEnterPayeReference                        controllers.EnterPayeReferenceController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "enterPayeReference.title = enterPayeReference" >> ../conf/messages.en
echo "enterPayeReference.heading = enterPayeReference" >> ../conf/messages.en
echo "enterPayeReference.checkYourAnswersLabel = enterPayeReference" >> ../conf/messages.en
echo "enterPayeReference.blank = enterPayeReference" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def enterPayeReference: Option[String] = cacheMap.getEntry[String](EnterPayeReferenceId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def enterPayeReference: Option[AnswerRow] = userAnswers.enterPayeReference map {";\
     print "    x => AnswerRow(\"enterPayeReference.checkYourAnswersLabel\", s\"$x\", false, routes.EnterPayeReferenceController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration EnterPayeReference completed"

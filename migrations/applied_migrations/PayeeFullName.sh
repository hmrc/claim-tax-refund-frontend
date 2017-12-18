#!/bin/bash

echo "Applying migration PayeeFullName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /payeeFullName               controllers.PayeeFullNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /payeeFullName               controllers.PayeeFullNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePayeeFullName                        controllers.PayeeFullNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePayeeFullName                        controllers.PayeeFullNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "payeeFullName.title = payeeFullName" >> ../conf/messages.en
echo "payeeFullName.heading = payeeFullName" >> ../conf/messages.en
echo "payeeFullName.checkYourAnswersLabel = payeeFullName" >> ../conf/messages.en
echo "payeeFullName.blank = payeeFullName" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def payeeFullName: Option[String] = cacheMap.getEntry[String](PayeeFullNameId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def payeeFullName: Option[AnswerRow] = userAnswers.payeeFullName map {";\
     print "    x => AnswerRow(\"payeeFullName.checkYourAnswersLabel\", s\"$x\", false, routes.PayeeFullNameController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration PayeeFullName completed"

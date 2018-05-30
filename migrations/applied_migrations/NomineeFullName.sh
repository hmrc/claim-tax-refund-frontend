#!/bin/bash

echo "Applying migration NomineeFullName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /nomineeFullName               controllers.NomineeFullNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /nomineeFullName               controllers.NomineeFullNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeNomineeFullName                        controllers.NomineeFullNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeNomineeFullName                        controllers.NomineeFullNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "nomineeFullName.title = nomineeFullName" >> ../conf/messages.en
echo "nomineeFullName.heading = nomineeFullName" >> ../conf/messages.en
echo "nomineeFullName.checkYourAnswersLabel = nomineeFullName" >> ../conf/messages.en
echo "nomineeFullName.blank = nomineeFullName" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def nomineeFullName: Option[String] = cacheMap.getEntry[String](NomineeFullNameId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def nomineeFullName: Option[AnswerRow] = userAnswers.nomineeFullName map {";\
     print "    x => AnswerRow(\"nomineeFullName.checkYourAnswersLabel\", s\"$x\", false, routes.NomineeFullNameController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration NomineeFullName completed"

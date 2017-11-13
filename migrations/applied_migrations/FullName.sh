#!/bin/bash

echo "Applying migration FullName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /fullName               controllers.FullNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /fullName               controllers.FullNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeFullName               controllers.FullNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeFullName               controllers.FullNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "fullName.title = fullName" >> ../conf/messages.en
echo "fullName.heading = fullName" >> ../conf/messages.en
echo "fullName.checkYourAnswersLabel = fullName" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def fullName: Option[String] = cacheMap.getEntry[String](FullNameId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def fullName: Option[AnswerRow] = userAnswers.fullName map {";\
     print "    x => AnswerRow(\"fullName.checkYourAnswersLabel\", s\"$x\", false, routes.FullNameController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration FullName completed"

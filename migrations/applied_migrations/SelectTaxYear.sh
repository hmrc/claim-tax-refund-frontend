#!/bin/bash

echo "Applying migration SelectTaxYear"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /selectTaxYear               controllers.SelectTaxYearController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /selectTaxYear               controllers.SelectTaxYearController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSelectTaxYear               controllers.SelectTaxYearController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSelectTaxYear               controllers.SelectTaxYearController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "selectTaxYear.title = selectTaxYear" >> ../conf/messages.en
echo "selectTaxYear.heading = selectTaxYear" >> ../conf/messages.en
echo "selectTaxYear.option1 = selectTaxYear" Option 1 >> ../conf/messages.en
echo "selectTaxYear.option2 = selectTaxYear" Option 2 >> ../conf/messages.en
echo "selectTaxYear.checkYourAnswersLabel = selectTaxYear" >> ../conf/messages.en
echo "selectTaxYear.blank = selectTaxYear" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def selectTaxYear: Option[SelectTaxYear] = cacheMap.getEntry[SelectTaxYear](SelectTaxYearId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def selectTaxYear: Option[AnswerRow] = userAnswers.selectTaxYear map {";\
     print "    x => AnswerRow(\"selectTaxYear.checkYourAnswersLabel\", s\"selectTaxYear.$x\", true, routes.SelectTaxYearController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SelectTaxYear completed"

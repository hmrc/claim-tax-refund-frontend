#!/bin/bash

echo "Applying migration SelectBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /selectBenefits               controllers.SelectBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /selectBenefits               controllers.SelectBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSelectBenefits                        controllers.SelectBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSelectBenefits                        controllers.SelectBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "selectBenefits.title = selectBenefits" >> ../conf/messages.en
echo "selectBenefits.heading = selectBenefits" >> ../conf/messages.en
echo "selectBenefits.checkYourAnswersLabel = selectBenefits" >> ../conf/messages.en
echo "selectBenefits.blank = selectBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def selectBenefits: Option[String] = cacheMap.getEntry[String](SelectBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def selectBenefits: Option[AnswerRow] = userAnswers.selectBenefits map {";\
     print "    x => AnswerRow(\"selectBenefits.checkYourAnswersLabel\", s\"$x\", false, routes.SelectBenefitsController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SelectBenefits completed"

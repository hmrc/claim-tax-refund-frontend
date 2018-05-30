#!/bin/bash

echo "Applying migration SelectCompanyBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /selectCompanyBenefits               controllers.SelectCompanyBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /selectCompanyBenefits               controllers.SelectCompanyBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSelectCompanyBenefits                        controllers.SelectCompanyBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSelectCompanyBenefits                        controllers.SelectCompanyBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "selectCompanyBenefits.title = selectCompanyBenefits" >> ../conf/messages.en
echo "selectCompanyBenefits.heading = selectCompanyBenefits" >> ../conf/messages.en
echo "selectCompanyBenefits.checkYourAnswersLabel = selectCompanyBenefits" >> ../conf/messages.en
echo "selectCompanyBenefits.blank = selectCompanyBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def selectCompanyBenefits: Option[String] = cacheMap.getEntry[String](SelectCompanyBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def selectCompanyBenefits: Option[AnswerRow] = userAnswers.selectCompanyBenefits map {";\
     print "    x => AnswerRow(\"selectCompanyBenefits.checkYourAnswersLabel\", s\"$x\", false, routes.SelectCompanyBenefitsController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SelectCompanyBenefits completed"

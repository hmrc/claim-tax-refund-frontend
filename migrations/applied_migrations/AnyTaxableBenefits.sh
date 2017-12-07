#!/bin/bash

echo "Applying migration AnyTaxableBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /anyTaxableBenefits                       controllers.AnyTaxableBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /anyTaxableBenefits                       controllers.AnyTaxableBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAnyTaxableBenefits                       controllers.AnyTaxableBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAnyTaxableBenefits                       controllers.AnyTaxableBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyTaxableBenefits.title = anyTaxableBenefits" >> ../conf/messages.en
echo "anyTaxableBenefits.heading = anyTaxableBenefits" >> ../conf/messages.en
echo "anyTaxableBenefits.checkYourAnswersLabel = anyTaxableBenefits" >> ../conf/messages.en
echo "anyTaxableBenefits.blank = anyTaxableBenefits" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def anyTaxableBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyTaxableBenefitsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def anyTaxableBenefits: Option[AnswerRow] = userAnswers.anyTaxableBenefits map {";\
     print "    x => AnswerRow(\"anyTaxableBenefits.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AnyTaxableBenefitsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration AnyTaxableBenefits completed"

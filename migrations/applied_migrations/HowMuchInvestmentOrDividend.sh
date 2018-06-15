#!/bin/bash

echo "Applying migration HowMuchInvestmentOrDividend"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howMuchInvestmentOrDividend               controllers.HowMuchInvestmentOrDividendController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howMuchInvestmentOrDividend               controllers.HowMuchInvestmentOrDividendController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowMuchInvestmentOrDividend                        controllers.HowMuchInvestmentOrDividendController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowMuchInvestmentOrDividend                        controllers.HowMuchInvestmentOrDividendController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howMuchInvestmentOrDividend.title = howMuchInvestmentOrDividend" >> ../conf/messages.en
echo "howMuchInvestmentOrDividend.heading = howMuchInvestmentOrDividend" >> ../conf/messages.en
echo "howMuchInvestmentOrDividend.checkYourAnswersLabel = howMuchInvestmentOrDividend" >> ../conf/messages.en
echo "howMuchInvestmentOrDividend.blank = howMuchInvestmentOrDividend" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def howMuchInvestmentOrDividend: Option[String] = cacheMap.getEntry[String](HowMuchInvestmentOrDividendId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howMuchInvestmentOrDividend: Option[AnswerRow] = userAnswers.howMuchInvestmentOrDividend map {";\
     print "    x => AnswerRow(\"howMuchInvestmentOrDividend.checkYourAnswersLabel\", s\"$x\", false, routes.HowMuchInvestmentOrDividendController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration HowMuchInvestmentOrDividend completed"

# Claim a tax refund frontend

CTR is a microservice for the tax form R39. It interacts with Digital Mail Service and Robotics.
<br/>A submission is sent to the Robotics service and then submitted to DMS for manual processing.

## Information

This project is a Scala web application using [code scaffolds](https://github.com/hmrc/hmrc-frontend-scaffold.g8)
<br/>Scala templates (views) use hmrc standard UI Components (https://github.com/hmrc/play-frontend-hmrc)
<br/>CTR is build using the Play framework: https://www.playframework.com/documentation/2.8.x

## Running the service

Service Manager: CTR_ALL

Mandatory Auth Information:
- Nino
- Confidence Level 200
- Itmp Given name

ITMP address information is optional and will be displayed back to the user via the frontend, but
is required for the change of address details page.

|Repositories|Link|
|------------|----|
|Backend|https://github.com/hmrc/claim-tax-refund|
|Stub|https://github.com/hmrc/claim-tax-refund-stubs|
|Journey tests|https://github.com/hmrc/claim-tax-refund-journey-tests|

### Dependencies

This service is dependant on these other services:
- [Auth](https://github.com/hmrc/auth)
- [Address lookup frontend](https://github.com/hmrc/address-lookup-frontend)
- [Tai](https://github.com/hmrc/tai)

### Routes

Start the service locally by going to http://localhost:9969/claim-tax-refund

Address lookup partial call back to either
- /address-callback-normal-mode
- /address-callback-check-mode

# Claim a tax refund frontend

CTR is a microservice for the tax form R39, it interacts with Digital Mail Service and Robotics. The submission is proccessed by robotics and sent to DMS where the case is add to a worklist and manually worked by back office staff.

## Info

This project is a Scala web application using [code scaffolds](https://github.com/hmrc/hmrc-frontend-scaffold.g8)

## Running the service

Service Manager: CTR_ALL

Mandatory auth information:
- Nino
- Confidence Level 200
- Itmp name

Itmp address is optional and will be displayed back to the user where applicable if provided.

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

Address lookup partial callsback to either
- /address-callback-normal-mode
- /address-callback-check-mode

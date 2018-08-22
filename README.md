# Claim a tax refund frontend
[![Build Status](https://travis-ci.org/hmrc/claim-tax-refund-frontend.svg)](https://travis-ci.org/hmrc/claim-tax-refund-frontend) [ ![Download](https://api.bintray.com/packages/hmrc/releases/claim-tax-refund-frontend/images/download.svg) ](https://bintray.com/hmrc/releases/claim-tax-refund-frontend/_latestVersion)

This project is a Scala web application for the tax form R39 using [code scaffolds](https://github.com/hmrc/hmrc-frontend-scaffold.g8)

## Get started

Follow these instructions so you can get a copy of the project on your local machine.  You can use this to develop and test the service.

### Before you get started

This service written in Scala and Play 2.5.  It needs:

- at least a [JRE 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) to run
- [sbt](https://www.scala-sbt.org/) to test, build and run a development version
- a MongoDB instance running locally or in a docker container with open ports

### How to install

1. Clone this repository to your development environment.
2. Start the MongoDB instance.
3. Start all CTR services using serive manager `sm --start CTR_ALL -fo`
4. Inside the claim-tax-refund-frontend folder run `sbt run` to start the service on port 9969.
5. Open [http://localhost:9969/claim-tax-refund](http://localhost:9969/claim-tax-refund).
6. Complete authentication wizard, 
```diff
- Nino / Confidence Level 200 / Itmp name are all mandatory fields
```

These steps will open the first page in the service.

### Dependencies

This service is dependant on these other services:
- [Auth](https://github.com/hmrc/auth)
- [Address lookup frontend](https://github.com/hmrc/address-lookup-frontend)
- [Tai](https://github.com/hmrc/tai)

## Adding new pages using scaffolds

In your service's root directory is a hidden directory `.g8`, which contains all of the scaffolds available for you to use.  Each will add a new screen or suite of related screens into your service.

To use a scaffold, run `sbt` in interactive mode and issue the command `g8Scaffold scaffoldName`, e.g. `g8Scaffold yesNoPage`.  The scaffold will ask you for some inputs, and create some new files (e.g. the `yesNoPage` scaffold will create a controller, view, and some specs).

Exit out of `sbt` and run the bash script `migrate.sh` in the root directory of your service.  This will modify a couple of files in the service, including routes and messages.

## Tests

### Unit tests

- Inside the claim-tax-refund-frontend folder run `sbt test` to run unit tests for the service.

## Licence

This project is licensed under the [Apache 2.0 License](LICENSE).

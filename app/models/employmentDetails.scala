package models

import play.api.libs.json.Json

case class employmentDetails(name: String,
                             districtNumber: Option[String],
                             payeNumber: Option[String])

object employmentDetails {
  implicit val format = Json.format[employmentDetails]
}
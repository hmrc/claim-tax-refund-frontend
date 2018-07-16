package models

import play.api.libs.json.Json

case class AddressLookupResponse(url : String)

object AddressLookupResponse {
  implicit val format = Json.format[AddressLookupResponse]
}

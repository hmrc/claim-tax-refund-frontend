/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package viewmodels

import play.api.i18n.Messages

import scala.language.implicitConversions

case class Message(key: String, args: Any*) {

	def print(implicit messages: Messages): String =
		messages(key, args: _*)
}

object Message {

	implicit def fromString(str: String): Message =
		Message(str)
}


case class AnswerRow(label: Message,
										 answer: Message,
										 url: Option[String],
										 changeLabel: String,
										 deleteUrl: Option[String],
										 itemName: Option[String],
										 isHeadingRow: Boolean,
										 isDeleteLinkRow: Boolean)

object AnswerRow {
	def apply(label: String,
						answer: String,
						url: Option[String] = None,
						changeLabel: String,
            deleteUrl: Option[String] = None,
						itemName: Option[String] = None,
						isHeadingRow: Boolean = false,
						isDeleteLinkRow: Boolean = false
					 ): AnswerRow =
		AnswerRow(Message(label), Message(answer), url, changeLabel, deleteUrl, itemName, isHeadingRow, isDeleteLinkRow)
}
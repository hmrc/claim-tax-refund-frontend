/*
 * Copyright 2018 HM Revenue & Customs
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

package utils

import models.Index

class SequenceUtil {
  def updateSeq(seq: Seq[String], index: Index, value: String): Seq[String] = {
    if (index.id >= seq.length) {
      val updatedSeq: Seq[String] = seq :+ value
      updatedSeq
    } else {
      val updatedSeq: Seq[String] = seq.updated(index.id, value)
      updatedSeq
    }
  }

  def updateMap(map: Map[String, String], name: String, value: String): Map[String, String] = {
    val updatedMap: Map[String, String] = map + (name -> value)
    updatedMap
  }
}
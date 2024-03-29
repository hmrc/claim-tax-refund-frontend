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

package utils

import java.io.{ByteArrayInputStream, InputStream}

import javax.xml.parsers.{DocumentBuilder, DocumentBuilderFactory}
import org.w3c.dom.Document
import java.security.MessageDigest

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64
import com.sun.org.apache.xml.internal.security.transforms.Transforms
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput
import com.sun.org.apache.xml.internal.security.Init

trait SubmissionMark {

  final val DEFAULT_SEC_HASH_ALGORITHM: String = "SHA1"

  def createMark(in: InputStream): String = {
    toBase64(getMarkBytes(in))
  }

  private def getMarkBytes(in: InputStream): Array[Byte] = {

    Init.init()

    // Parse the transform details to create a document
    val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance
    dbf.setNamespaceAware(true)
    val db: DocumentBuilder = dbf.newDocumentBuilder
    val doc: Document = db.parse(new ByteArrayInputStream(getAlgorithm.getBytes))

    // Construct a Apache security Transforms object from that document
    val transforms: Transforms = new Transforms(doc.getDocumentElement, null)

    // Now perform the transform on the input to get the results.
    val input: XMLSignatureInput = new XMLSignatureInput(in)
    val result: XMLSignatureInput = transforms.performTransforms(input)

    val md: MessageDigest = MessageDigest.getInstance(DEFAULT_SEC_HASH_ALGORITHM)
    md.update(result.getBytes)
    md.digest
  }

  def getAlgorithm: String = "<?xml version='1.0'?>\n" +
    "<dsig:Transforms xmlns:dsig='http://www.w3.org/2000/09/xmldsig#' xmlns:xdp='http://ns.adobe.com/xdp/'>\n" +
    "<dsig:Transform Algorithm='http://www.w3.org/TR/1999/REC-xpath-19991116'>\n" +
    "<dsig:XPath>count(ancestor-or-self::node()|/xdp:xdp)=count(ancestor-or-self::node())</dsig:XPath>\n" +
    "</dsig:Transform>\n" +
    "<dsig:Transform Algorithm='http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments'/>\n" +
    "</dsig:Transforms>"

  private def toBase64(irMarkBytes: Array[Byte]): String = {
    new String(Base64.encode(irMarkBytes))
  }
}

object SubmissionMark extends SubmissionMark {
  def getSfMark(formData: String): String = {
    createMark(new ByteArrayInputStream(formData.getBytes))
  }
}
/*
 * Copyright 2020 HM Revenue & Customs
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

package uk.gov.hmrc.homeofficesettledstatus.controllers

import play.api.data.Forms.{of, optional, text}
import play.api.data.Mapping
import play.api.data.format.Formats._
import play.api.data.validation.{Constraint, Constraints, Invalid, Valid, ValidationError}
import DateFieldHelper._
import uk.gov.hmrc.domain.Nino

object FormFieldMappings {

  def dateOfBirthMapping: Mapping[String] = dateFieldsMapping(validDobDateFormat)

  def validNino(
    nonEmptyFailure: String = "error.nino.required",
    invalidFailure: String = "error.nino.invalid-format"): Constraint[String] =
    ValidateHelper.validateField(nonEmptyFailure, invalidFailure)(nino => Nino.isValid(nino))

  val normalizedText: Mapping[String] = of[String].transform(_.replaceAll("\\s", ""), identity)
  val uppercaseNormalizedText: Mapping[String] = normalizedText.transform(_.toUpperCase, identity)
  val trimmedUppercaseName: Mapping[String] = of[String]
    .transform[String](_.trim.take(64).toUpperCase, identity)

  def validName(fieldName: String, minLenInc: Int): Constraint[String] =
    Constraint[String] { fieldValue: String =>
      Constraints.nonEmpty(errorMessage = s"error.$fieldName.required")(fieldValue) match {
        case i @ Invalid(_) =>
          i
        case Valid =>
          if (fieldValue.matches("^[a-zA-Z -']"))
            Valid
          else if (fieldValue.length >= minLenInc)
            Valid
          else
            Invalid(ValidationError(s"error.$fieldName.invalid-format"))
      }
    }
}
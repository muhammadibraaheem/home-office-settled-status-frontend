package uk.gov.hmrc.homeofficesettledstatus.stubs

import java.time.LocalDate

import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.homeofficesettledstatus.models.{ImmigrationStatus, StatusCheckByNinoRequest, StatusCheckResult}

trait JourneyTestData {

  val correlationId: String = scala.util.Random.alphanumeric.take(64).toString()

  val validQuery =
    StatusCheckByNinoRequest("2001-01-31", "JANE", "DOE", Nino("RJ301829A"))

  val expectedResultWithSingleStatus = StatusCheckResult(
    fullName = "Jane Doe",
    dateOfBirth = LocalDate.parse("2001-01-31"),
    nationality = "IRL",
    statuses = List(
      ImmigrationStatus(
        statusStartDate = LocalDate.parse("2018-12-12"),
        statusEndDate = Some(LocalDate.parse("2018-01-31")),
        productType = "EUS",
        immigrationStatus = "ILR",
        noRecourseToPublicFunds = true
      )
    )
  )

  val expectedResultWithMultipleStatuses = StatusCheckResult(
    fullName = "Jane Doe",
    dateOfBirth = LocalDate.parse("2001-01-31"),
    nationality = "IRL",
    statuses = List(
      ImmigrationStatus(
        statusStartDate = LocalDate.parse("2018-12-12"),
        productType = "EUS",
        immigrationStatus = "ILR",
        noRecourseToPublicFunds = true
      ),
      ImmigrationStatus(
        statusStartDate = LocalDate.parse("2018-01-01"),
        statusEndDate = Some(LocalDate.parse("2018-12-11")),
        productType = "EUS",
        immigrationStatus = "LTR",
        noRecourseToPublicFunds = false
      )
    )
  )

}

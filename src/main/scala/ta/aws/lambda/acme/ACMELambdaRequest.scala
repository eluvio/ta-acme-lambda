package ta.aws.lambda.acme

import fm.common.{Enum, EnumEntry}

object ACMELambdaRequest {
  sealed trait Action extends EnumEntry

  object Action extends Enum[Action] {
    val values: IndexedSeq[Action] = findValues
  }

  case object HTTPChallenge extends Action
  case object GetCertificate extends Action
  case object GetWildCardCertificate extends Action
  case object ListCertificates extends Action
}

trait ACMELambdaRequest {
  def action: ACMELambdaRequest.Action
}

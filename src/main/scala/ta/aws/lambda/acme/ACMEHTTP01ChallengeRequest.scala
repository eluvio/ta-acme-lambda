package ta.aws.lambda.acme

/**
  * Instead of using API-Gateway, allow calling the Lambda with a JSON-encoded request the
  * @param fqdn The fully-qualified domain name for the requested certificate.
  * @param token The ACME Challenge token
  */
final case class ACMEHTTP01ChallengeRequest(
  fqdn: String,
  token: String
) extends ACMELambdaRequest {
  def action: ACMELambdaRequest.Action = ACMELambdaRequest.HTTPChallenge
}
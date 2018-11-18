package ta.aws.lambda.acme

/**
  * Request
  *
  * @param fqdn The fully-qualified domain name for the requested certificate.
  */
final case class GetWildCardCertificateRequest(
  domain: String
) extends ACMELambdaRequest {
  def action: ACMELambdaRequest.Action = ACMELambdaRequest.GetCertificate
}
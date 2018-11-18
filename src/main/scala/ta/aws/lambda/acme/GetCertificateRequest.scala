package ta.aws.lambda.acme

/**
  * Request a fully qualified domain certificate.  If already created, returns the key
  *
  * @param domain The fully-qualified domain name for the requested certificate.
  */
final case class GetCertificateRequest(

  domain: String
) extends ACMELambdaRequest {
  def action: ACMELambdaRequest.Action = ACMELambdaRequest.GetCertificate
}
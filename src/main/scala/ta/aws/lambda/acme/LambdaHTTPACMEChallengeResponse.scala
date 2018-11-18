package ta.aws.lambda.acme
/**
  * @param authorization The ACME HTTP Challenge
  */
case class LambdaHTTPACMEChallengeResponse(
  authorization: String,
)
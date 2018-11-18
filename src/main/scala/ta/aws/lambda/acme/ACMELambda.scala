package ta.aws.lambda

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import java.io.{File, InputStream, OutputStream, StringWriter}
import java.nio.charset.StandardCharsets
import fm.common.Implicits._
import fm.common.{FileUtil, InputStreamResource, Resource}
import fm.serializer.json.JSON
import ta.aws.lambda.utils._

class ACMELambda extends RequestStreamHandler {


  /**
    *
    * Lambda Request supports three different invokations:
    *
    * 1. AWS API Gateway Proxy HTTP Challenge.
    *
    * AWS API Gateway Proxy Delivers a "wrapped" HTTP Payload that includes details of the original request.
    *
    * Example:
    * {{{
    * {
    *   "body": "",
    *   "resource": "/.well-known/acme-challenge/{token+}",
    *   "path": "/.well-known/acme-challenge/fFCxCpEYRDWwBY0PMgAVljisoVdnc5wer6TblEA5pk8",
    *   "requestContext": {
    *     ...
    *     "identity": {
    *       ...
    *     },
    *     "stage": "prod"
    *   },
    *   "queryStringParameters": {
    *     ...
    *   },
    *   "headers": {
    *     ...
    *   },
    *   "pathParameters": {
    *     "token": "fFCxCpEYRDWwBY0PMgAVljisoVdnc5wer6TblEA5pk8"
    *   },
    *   "httpMethod": "GET"
    * }
    * }}}
    *
    * If the request format is proxied from API Gateway, the lambda will parse both the `path` and check for a `token`
    * in the `pathParameters`
    *
    * Lambda response will be formatted in an API Gateway Response and contain the ACME Challenge
    *
    * 2. AWS Lambda Invoke Request (Token Handler)
    *
    *  If not using AWS API Gateway, a custom HTTP ACME Request handler at the FQDN can directly invoke the AWS Lambda
    *
    *  The AWS ACME Lambda expects the request in the following JSON-format:
    *
    *  Request:
    *  {{{
    *
    *  {
    *    "fqdn": "bar.foo.com",
    *    "token": "fFCxCpEYRDWwBY0PMgAVljisoVdnc5wer6TblEA5pk8"
    *  }
    *
    *  }}}
    *
    *  If the Challenge Token is found, the ACME Lambda will return back an HTTP Status 200 with the HTTP Body
    *  containing the ACME Authorization.
    *
    *  Note: The request is in JSON, but the response is the plaintext authorization.
    *
    *  Response:
    *  {{{
    *  <authorization example>
    *  }}}
    *
    *  If the Challenge Token is not found, the ACME Lambda will return back HTTP Status 400
    *
    * 3. AWS Lambda Invoke Request (Check Certificates)
    *
    * The ACME Lambda can be directly invoked to check
    *
    * {{{
    * }}}

    *
    * @param lambdaInputStream
    * @param lambdaOutputStream
    * @param context
    */
  def handleRequest(lambdaInputStream: InputStream, lambdaOutputStream: OutputStream,  context: Context): Unit = {
    val requestBody: String = InputStreamResource.forInputStream(lambdaInputStream).readToString(StandardCharsets.UTF_8)

    if (requestBody.isNullOrBlank) {

    }
    val lambdaRequest: Option[ProxyRequest[String]] = JSON.fromJSON(requestBody)

    // /.well-known/acme-challenge/{token}

    // Handle HTTP-Challenge
    lambdaRequest.foreach{ request: ProxyRequest[String] =>
      request.path

      ???

    }

    // Handle Renewals
    if (lambdaRequest.isEmpty) {

      lambdaOutputStream.close
    }
  }
}

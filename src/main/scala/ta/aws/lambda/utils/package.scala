/*
MIT License

Copyright (c) 2018 AWS Lambda Scala contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

// Based upon: https://github.com/mkotsur/aws-lambda-scala/blob/master/src/main/scala/io/github/mkotsur/aws/proxy/package.scala


package ta.aws.lambda

/**
  * This contains case classes to deserialize API-gateway requests and serialize API-gateway compatible responses
  */
package object utils {
  case class RequestContextAuthorizer(
      principalId: String
  )

  case class RequestContext(
      authorizer: Option[RequestContextAuthorizer] = None
  )

  object RequestContext {
    def withPrincipalId(id: String) = RequestContext(Some(RequestContextAuthorizer(id)))
  }

  case class RequestInput(body: String)

  case class ProxyRequest[T](
      path: String,
      httpMethod: String,
      headers: Option[Map[String, String]] = None,
      queryStringParameters: Option[Map[String, String]] = None,
      stageVariables: Option[Map[String, String]] = None,
      requestContext: RequestContext = RequestContext(),
      body: Option[T] = None
  )

  case class ProxyResponse[T](
      statusCode: Int,
      headers: Option[Map[String, String]] = None,
      body: Option[T] = None
  )

  object ProxyResponse {
    def success[B](body: Option[B] = None): ProxyResponse[B] = ProxyResponse[B](
      statusCode = 200,
      headers = Some(Map("Access-Control-Allow-Origin" -> "*")),
      body = body
    )
  }
}

# ACME Lambda (Let's Encrypt-compatible)

Use [AWS Lambda](https://aws.amazon.com/lambda/) to manage SSL certificates for ACME providers.

# How does it work?

This project utilizes AWS Lambda to periodically (once per day) check a set of certificates for expiration, and then if they're about to expire or invalid/missing, it will request a new certificate from the ACME infrastructure.

Certificates are stored in S3, which can easily be configured to send an SNS notification based upon a PUT event into the configured bucket, or trigger other TLS-frontends to refresh their certificates.

* Supports AMCE `HTTP-01` (requires proxying `/.well-known/acme-challenge/*` requests to the lambda via [AWS API Gateway](https://aws.amazon.com/api-gateway/) or invoking the lambda from your front-end & responding with the authorization challenge)
* Supports ACME `DNS-01` via Route53

# Configuration



## Lambda Entry Points

The Lambda can be invoked for either responding to `HTTP-01` challenges, as part of the periodic certificates, or as a combination of a health check for proper configuration and certificate management.

### Proxying ACME HTTP-01 Challenge

#### AWS API Gateway Lambda Proxy HTTP Challenge

AWS API Gateway Proxy Delivers a "wrapped" HTTP Payload that includes details of the original request.

Example:

```
{
  "body": "",
  "resource": "/.well-known/acme-challenge/{token+}",
  "path": "/.well-known/acme-challenge/fFCxCpEYRDWwBY0PMgAVljisoVdnc5wer6TblEA5pk8",
  "requestContext": {
    ...
    "identity": {
       ...
    },
    "stage": "prod"
  },
  "queryStringParameters": {
     ...
  },
  "headers": {
    ...
  },
  "pathParameters": {
    "token": "fFCxCpEYRDWwBY0PMgAVljisoVdnc5wer6TblEA5pk8"
  },
  "httpMethod": "GET"
}
```

If the lambda request is invoked from [AWS API Gateway Lambda Proxy](https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-create-api-as-simple-proxy-for-lambda.html), the lambda will parse both the `path` and check for a `token` in the `pathParameters`.

In your front-end (either application, load balancer (e.g. HA Proxy), or ingress) you can simply proxy any requests from `/.well-known/acme-challenge/*` to your API Gateway using the same request path (e.g. https://wt6mne2s9k.execute-api.us-west-2.amazonaws.com/.well-known/acme-challenge/fFCxCpEYRDWwBY0PMgAVljisoVdnc5wer6TblEA5pk8`)

TODO: Add additional configuration instructions for setting up the API Gateway Lambda Proxy, and expected error codes.

#### AWS Lambda HTTP Challenge

Alternatively, implement a `/.well-known/acme-challenge/*` request handler that will directly invoke the lambda with the challenge token and return back the HTTP Challenge response.

The lambda will accept the token as the payload, and return back an HTTP Status:
  * `200` indicating a successful lookup; with the authentication challenge response as the response content
  * `404` if the token was not found
  * `500` if there was an environment, permission, or execution issue; with the error details as the response content

*Example:*

```
var AWS = require('aws-sdk');
var lambda = new AWS.Lambda({ region: 'us-west-2' });

router.get('/.well-known/acme-challenge/:token', function (req, res) {
  lambda.invoke({
    FunctionName: 'ACMELambda',
    InvocationType: 'RequestResponse',
    Payload: req.param.token
  }, function (err, data) {
     if (error) {
       res.status(500).send(`Error occured while invoking lambda: ${err}`);
     } else {
       res.send(data);
     }
   });
})
```

#### AWS Lambda Update

To process the current configuration (check for renewals, create new certificates, etc) simply invoke the method.

If the payload is blank, the Lambda function assumes the certbot mode, and processes updates or renewals.

##### Scheduled Task

You can schedule AWS Lambda events using AWS Cloud Watch.  For more information see: [Tutorial: Schedule AWS Lambda Functions Using CloudWatch Events](https://docs.aws.amazon.com/AmazonCloudWatch/latest/events/RunLambdaSchedule.html)


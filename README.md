# HMAC
self development HMAC and sha-256.

There are three bash scripts for Ubuntu: sha256, hmac, runDemo

`sha256` uses self-implemented sha-256 algorithm to create password hash and store it in keyFile, you can specify the password and keyFile
`
./sha256 $password $keyFile`

`hmac create` uses self-implemented hmac algorithm to create hmac. You can specify keyFile, messageFile, and outputFile.

`./hmac create $keyFile $messageFile $outputFile`

`hmac verify` uses javax.crypto library to create hmac. You can specify keyFile, messageFile, and outputFile

`./hmac verify $keyFile $messageFile $outputFile`

`runDemo` will run `sha256` first and then run `hmac create`, `hmac verify` and test the two hmac has the same result.

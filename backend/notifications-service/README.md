
// add certificates

1) Use a browser or a tool like openssl to download the certificate from the SMTP server.
openssl s_client -connect smtp-relay.gmail.com:465 -showcerts

2) Save the certificate in a file (e.g., server.crt).
3) Import the certificate into the Java keystore:
keytool -import -alias smtp-relay-gmail -keystore $JAVA_HOME/lib/security/cacerts -file server.crt


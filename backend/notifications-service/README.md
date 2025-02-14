
// add certificates

1) Use a browser or a tool like openssl to download the certificate from the SMTP server.
openssl s_client -connect smtp-relay.gmail.com:465 -showcerts

2) Save the certificate in a file (e.g., server.crt).
3) Import the certificate into the Java keystore:
keytool -import -alias smtp-relay-gmail -keystore $JAVA_HOME/lib/security/cacerts -file server.crt

Here's a well-formatted **README.md** file with proper markdown syntax for better readability:

---

## **Fix: Mail Server Connection Failed - SSLHandshakeException**

If you encounter the following error while connecting to an SMTP server:

```
Mail server connection failed. Failed messages: jakarta.mail.MessagingException: Could not convert socket to TLS;
  nested exception is:
  javax.net.ssl.SSLHandshakeException: (certificate_unknown) PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
```

Follow these steps to **import the SMTP certificate** and configure **Docker Compose** correctly.

---

## **📌 Steps to Import the Certificate and Use it in Docker Compose**

### **1️⃣ Extract the SMTP Certificate**
Run this command on your **host machine** to fetch the certificate:

```sh
openssl s_client -connect smtp-relay.gmail.com:465 -showcerts
```

Copy the part between:
```
-----BEGIN CERTIFICATE-----
...
-----END CERTIFICATE-----
```
Then save it as **`server.crt`**.

---

### **2️⃣ Create the Keystore and Import the Certificate**
Run the following command to add the certificate to a new **Java keystore**:

```sh
keytool -import -trustcacerts -alias smtp-relay-gmail \
  -keystore certs/cacerts -file server.crt \
  -storepass changeit -noprompt
```

This will create a `cacerts` file inside the **certs/** directory.

---

### **3️⃣ Ensure the `certs/` Directory Exists**
If the directory does not exist, create it:

```sh
mkdir -p certs
```

Then move the `cacerts` file inside:

```sh
mv cacerts certs/
```

---

### **4️⃣ Rebuild and Restart Docker Compose**
Restart your **Docker Compose** services to apply the changes:

```sh
docker-compose down
docker-compose up --build
```

---

### **5️⃣ Verify the Certificate Inside the Containers**
To check if the certificate was successfully imported into **Java's keystore** inside the container, run:

```sh
docker exec -it api_service keytool -list -keystore /etc/ssl/cacerts -storepass changeit | grep smtp-relay-gmail
```

If successful, you should see output similar to:

```yaml
smtp-relay-gmail, Jan 01, 2025, trustedCertEntry,
```

---

## **✅ Done!**
Now the Spring Boot application should be able to connect to the SMTP server **without SSL handshake issues.** 🎉

---

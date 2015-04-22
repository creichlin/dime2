package ch.kerbtier.dime2.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

public class User {

  private Authentication auth;

  private String username;
  private String password;
  private String salt;

  User(Authentication auth, String username, String password) {
    this.auth = auth;
    this.username = username;
    byte[] saltBytes = new byte[128];
    new SecureRandom().nextBytes(saltBytes);

    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(saltBytes);

      this.salt = DatatypeConverter.printHexBinary(md.digest());
    } catch (NoSuchAlgorithmException e) {
      assert (false);
    }
    setPassword(password);
  }

  public void setPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update((salt + password).getBytes("UTF-8"));
      this.password = DatatypeConverter.printHexBinary(md.digest());
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      assert (false);
    }
    auth.save();
  }

  User(Authentication auth, String username, String password, String salt) {
    this.auth = auth;
    this.username = username;
    this.password = password;
    this.salt = salt;
  }

  public boolean authenticates(String pw) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update((salt + pw).getBytes("UTF-8"));
      String pwh = DatatypeConverter.printHexBinary(md.digest());
      if (this.password.equals(pwh)) {
        return true;
      }
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      assert (false);
    }
    return false;
  }

  public String getUsername() {
    return username;
  }

  String getSalt() {
    return salt;
  }

  String getPassword() {
    return password;
  }
}

package com.spring.websellspringmvc.passkey;

import com.yubico.webauthn.data.ByteArray;

import java.nio.ByteBuffer;

public class YubicoUtils {

  public static ByteArray toByteArray(int value) {
    ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES); // Allocate 4 bytes for an int
    buffer.putInt(value); // Write the integer into the buffer
    return new ByteArray(buffer.array()); // Create ByteArray from the buffer's byte array
  }

  // Convert a ByteArray to Integer
  public static int toInt(ByteArray byteArray) {
    ByteBuffer buffer = ByteBuffer.wrap(byteArray.getBytes()); // Wrap ByteArray's bytes into a ByteBuffer
    return buffer.getInt(); // Read the integer from the buffer
  }

}

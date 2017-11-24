package com.twitter.zipkin.gen;

import javax.annotation.Generated;

//参数值类型
@Generated("thrift")
public enum AnnotationType {
  BOOL(0),//boolean
  BYTES(1),//字节数组
  I16(2),//2个字节的整数
  I32(3),//4个字节的整数
  I64(4),//8个字节的整数
  DOUBLE(5),//double类型
  STRING(6);//字符串类型

  private final int value;

  AnnotationType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /** Returns {@link AnnotationType#BYTES} if unknown. */
  public static AnnotationType fromValue(int value) {
    switch (value) {
      case 0:
        return BOOL;
      case 1:
        return BYTES;
      case 2:
        return I16;
      case 3:
        return I32;
      case 4:
        return I64;
      case 5:
        return DOUBLE;
      case 6:
        return STRING;
      default:
        return BYTES;
    }
  }
}

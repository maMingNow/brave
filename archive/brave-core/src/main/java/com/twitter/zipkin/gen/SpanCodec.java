package com.twitter.zipkin.gen;

import com.github.kristofa.brave.internal.DefaultSpanCodec;
import java.util.List;

//如何对span的内容进行编码
public interface SpanCodec {
  SpanCodec THRIFT = DefaultSpanCodec.THRIFT;
  SpanCodec JSON = DefaultSpanCodec.JSON;

  //如何对一个span对象转换成字节数组
  byte[] writeSpan(Span span);

  byte[] writeSpans(List<Span> spans);

  /** throws {@linkplain IllegalArgumentException} if the span couldn't be decoded */
  //字节数组转换成span对象
  Span readSpan(byte[] bytes);
}

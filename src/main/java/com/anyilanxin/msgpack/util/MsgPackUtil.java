/*
 * Copyright © 2025 anyilanxin zxh (anyilanxin@aliyun.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anyilanxin.msgpack.util;

import com.anyilanxin.msgpack.JsonSerializable;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.agrona.DirectBuffer;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.anyilanxin.msgpack.util.BufferUtil.bufferAsArray;
import static com.anyilanxin.msgpack.util.StringUtil.getBytes;

public final class MsgPackUtil {
  private static final StreamReadConstraints JSON_STREAM_CONSTRAINTS =
      StreamReadConstraints.builder()
          .maxStringLength(Integer.MAX_VALUE)
          .maxNumberLength(Integer.MAX_VALUE)
          .maxNestingDepth(Integer.MAX_VALUE)
          .build();
  private static final JsonEncoding JSON_ENCODING = JsonEncoding.UTF8;
  private static final Charset JSON_CHARSET = StandardCharsets.UTF_8;
  private static final TypeReference<HashMap<String, Object>> OBJECT_MAP_TYPE_REFERENCE =
      new TypeReference<>() {};

  private static final JsonFactory MESSAGE_PACK_FACTORY =
      new MessagePackFactory()
          .setReuseResourceInGenerator(false)
          .setReuseResourceInParser(false)
          .setStreamReadConstraints(JSON_STREAM_CONSTRAINTS);

  private static final ObjectMapper MESSAGE_PACK_OBJECT_MAPPER =
      new ObjectMapper(MESSAGE_PACK_FACTORY).registerModule(new JavaTimeModule());

  private static final JsonFactory JSON_FACTORY =
      new MappingJsonFactory()
          .configure(Feature.ALLOW_SINGLE_QUOTES, true)
          .setStreamReadConstraints(JSON_STREAM_CONSTRAINTS);

  private static final ObjectMapper JSON_OBJECT_MAPPER =
      new ObjectMapper(JSON_FACTORY).registerModule(new JavaTimeModule());

  // prevent instantiation
  private MsgPackUtil() {}

  public static byte[] convertToMsgPack(final String json) {
    final byte[] jsonBytes = getBytes(json, JSON_CHARSET);
    final ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
    return convertToMsgPack(inputStream);
  }

  public static String convertToJson(final DirectBuffer buffer) {
    return convertToJson(bufferAsArray(buffer));
  }

  public static String convertToJson(final byte[] msgPack) {
    return convertToJson(new ByteArrayInputStream(msgPack));
  }

  private static String convertToJson(final InputStream msgPackInputStream) {
    final byte[] jsonBytes = convertToJsonBytes(msgPackInputStream);
    return new String(jsonBytes, JSON_CHARSET);
  }

  private static byte[] convertToJsonBytes(final byte[] msgPack) {
    final InputStream inputStream = new ByteArrayInputStream(msgPack);
    return convertToJsonBytes(inputStream);
  }

  private static byte[] convertToJsonBytes(final InputStream msgPackInputStream) {
    try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      convert(msgPackInputStream, outputStream, MESSAGE_PACK_FACTORY, JSON_FACTORY);
      return outputStream.toByteArray();
    } catch (final Exception e) {
      throw new RuntimeException("Failed to convert MessagePack to JSON", e);
    }
  }

  private static void convert(
      final InputStream in,
      final OutputStream out,
      final JsonFactory inFormat,
      final JsonFactory outFormat)
      throws Exception {
    try (final JsonParser parser = inFormat.createParser(in);
        final JsonGenerator generator = outFormat.createGenerator(out, JSON_ENCODING)) {
      final JsonToken token = parser.nextToken();
      if (!token.isStructStart() && !token.isScalarValue()) {
        throw new RuntimeException(
            "Document does not begin with an object, an array, or a scalar value");
      }
      generator.copyCurrentStructure(parser);
      generator.flush();
    }
  }

  public static Map<String, Object> convertToMap(final DirectBuffer buffer) {
    return convertToMap(OBJECT_MAP_TYPE_REFERENCE, buffer);
  }

  private static <T, U> Map<U, T> convertToMap(
      final TypeReference<HashMap<U, T>> typeRef, final DirectBuffer buffer) {
    final byte[] msgpackBytes = bufferAsArray(buffer);
    try {
      return MESSAGE_PACK_OBJECT_MAPPER.readValue(msgpackBytes, typeRef);
    } catch (final IOException e) {
      throw new RuntimeException("Failed to deserialize MessagePack to Map", e);
    }
  }

  public static byte[] convertToMsgPack(final Object value) {
    try {
      return MESSAGE_PACK_OBJECT_MAPPER.writeValueAsBytes(value);
    } catch (final IOException e) {
      throw new RuntimeException(
          String.format("Failed to serialize object '%s' to MessagePack", value), e);
    }
  }

  public static Object convertToMsgPack(final DirectBuffer buffer) {
    try {
      final byte[] msgpackBytes = bufferAsArray(buffer);
      return MESSAGE_PACK_OBJECT_MAPPER.readValue(msgpackBytes, Object.class);
    } catch (final IOException e) {
      throw new RuntimeException("Failed to serialize object to MessagePack", e);
    }
  }

  public static String convertJsonSerializableObjectToJson(final JsonSerializable recordValue) {
    try {
      return JSON_OBJECT_MAPPER.writeValueAsString(recordValue);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T convertToObject(final DirectBuffer buffer, final Class<T> clazz) {
    final byte[] msgpackBytes = bufferAsArray(buffer);
    try {
      return MESSAGE_PACK_OBJECT_MAPPER.readValue(msgpackBytes, clazz);
    } catch (final IOException e) {
      throw new RuntimeException("Failed to deserialize MessagePack to Map", e);
    }
  }
}

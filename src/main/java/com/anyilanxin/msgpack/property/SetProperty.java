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
package com.anyilanxin.msgpack.property;

import com.anyilanxin.msgpack.value.BaseValue;
import com.anyilanxin.msgpack.value.SetValue;
import com.anyilanxin.msgpack.value.StringValue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author zxuanhong
 */
public final class SetProperty<T extends BaseValue> extends BaseProperty<SetValue<T>> {
  public SetProperty(final String keyString, final Supplier<T> valueFactory) {
    super(keyString, new SetValue<>(valueFactory));
    isSet = true;
  }

  public SetProperty(final StringValue key, final Supplier<T> valueFactory) {
    super(key, new SetValue<>(valueFactory));
    isSet = true;
  }

  public SetProperty(
      final int initialCapacity, final String keyString, final Supplier<T> valueFactory) {
    super(keyString, new SetValue<>(initialCapacity, valueFactory));
    isSet = true;
  }

  public SetProperty(
      final int initialCapacity, final StringValue key, final Supplier<T> valueFactory) {
    super(key, new SetValue<>(initialCapacity, valueFactory));
    isSet = true;
  }

  @Override
  public void reset() {
    super.reset();
    isSet = true;
  }

  public void add(final Consumer<T> value) {
    this.value.add(value);
  }

  public void remove(final T value) {
    this.value.remove(value);
  }

  public boolean isEmpty() {
    return value.isEmpty();
  }

  public int size() {
    return value.size();
  }
}

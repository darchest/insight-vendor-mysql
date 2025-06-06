/*
 * Copyright 2021-2024, Darchest and contributors.
 * Licensed under the Apache License, Version 2.0
 */

package org.darchest.insight.vendor.mysql

import org.darchest.insight.SqlType

open class MySqlType(name: String): SqlType(name)

open class BooleanType: MySqlType("bool")

open class ByteaType: MySqlType("bytea")

open class NumericType(name: String): MySqlType(name)

open class SmallIntType: NumericType("smallint")

open class IntType: NumericType("integer")

open class BigIntType: NumericType("bigint")

open class DatetimeType: NumericType("datetime")

open class UuidType: MySqlType("uuid")

open class StringType(name: String): MySqlType(name)

open class CharType: StringType("char")

open class VarCharType: StringType("varchar")

/*
 * Copyright 2021-2024, Darchest and contributors.
 * Licensed under the Apache License, Version 2.0
 */

package org.darchest.insight.vendor.mysql

import org.darchest.insight.*
import java.time.Instant
import java.util.*

open class MySqlColumn<javaType: Any, sqlT: MySqlType>(name: String, javaClass: Class<javaType>, sqlType: sqlT): TableColumn<javaType, sqlT>(name, javaClass, sqlType)

class MySqlComparison(left: SqlValue<*, *>, operator: Operator, right: SqlValue<*, *>): ComparisonOperation<Boolean, BooleanType>(left, operator, right, Boolean::class.java, BooleanType())

class MySqlLogical(operator: Operator, values: Collection<SqlValue<*, *>>): LogicalOperation<Boolean, BooleanType>(operator, values, Boolean::class.java, BooleanType())


open class BoolColumn(name: String): MySqlColumn<Boolean, BooleanType>(name, Boolean::class.java, BooleanType()) {
	override fun default(): SqlValue<*, BooleanType>? = SqlConst(false, Boolean::class.java, BooleanType())
}

open class ShortColumn(name: String): MySqlColumn<Short, SmallIntType>(name, Short::class.java, SmallIntType())

open class IntColumn(name: String): MySqlColumn<Int, IntType>(name, Int::class.java, IntType()) {
	override fun default(): SqlValue<*, IntType>? = SqlConst(0, Int::class.java, IntType())
}

open class LongColumn(name: String): MySqlColumn<Long, BigIntType>(name, Long::class.java, BigIntType()) {
	override fun default(): SqlValue<*, BigIntType>? = SqlConst(0L, Long::class.java, BigIntType())
}

open class UuidColumn(name: String): MySqlColumn<UUID, UuidType>(name, UUID::class.java, UuidType()) {
	override fun default(): SqlValue<*, UuidType>? = SqlConst(UUID.fromString("00000000-0000-0000-0000-000000000000"), UUID::class.java, UuidType())
}

open class VarCharColumn(name: String): MySqlColumn<String, StringType>(name, String::class.java, VarCharType()) {
	override fun default(): SqlValue<*, StringType> = SqlConst("", String::class.java, VarCharType())
}

open class ByteaTextColumn(name: String): MySqlColumn<String, ByteaType>(name, String::class.java, ByteaType())

open class BinaryColumn(name: String): MySqlColumn<ByteArray, ByteaType>(name, ByteArray::class.java, ByteaType()) {
	override fun default(): SqlValue<*, ByteaType> = SqlConst("\\x", String::class.java, ByteaType())
}

open class DatetimeColumn(name: String): MySqlColumn<Instant, DatetimeType>(name, Instant::class.java, DatetimeType()) {
	override fun default(): SqlValue<*, DatetimeType>? = SqlConst(Long.MIN_VALUE, Long::class.java, DatetimeType())
}

abstract class MySqlExpression<javaType: Any, sqlT: MySqlType>(javaClass: Class<javaType>, sqlType: sqlT): Expression<javaType, sqlT>(javaClass, sqlType), SqlValueNotNullGetter<javaType>

abstract class MySqlLocalExpression<javaType: Any>(javaClass: Class<javaType>, innerColumns: List<TableColumn<*, *>>, fn: () -> javaType?): LocalExpression<javaType>(javaClass, innerColumns, fn)

open class StringExpression(private val value: String): MySqlExpression<String, StringType>(String::class.java, VarCharType()) {
	override suspend fun writeSql(builder: StringBuilder, vendor: Vendor, params: MutableList<SqlValue<*, *>>) {
		builder.append("'$value'")
	}
}

open class BooleanExpression(private val value: Boolean): MySqlExpression<Boolean, BooleanType>(Boolean::class.java, BooleanType()) {
	override suspend fun writeSql(builder: StringBuilder, vendor: Vendor, params: MutableList<SqlValue<*, *>>) {
		builder.append(if (value) "1" else "0")
	}
}

class CountExpression: MySqlExpression<Long, BigIntType>(Long::class.java, BigIntType()) {
	override suspend fun writeSql(builder: StringBuilder, vendor: Vendor, params: MutableList<SqlValue<*, *>>) {
		builder.append("COUNT(1)")
	}
}
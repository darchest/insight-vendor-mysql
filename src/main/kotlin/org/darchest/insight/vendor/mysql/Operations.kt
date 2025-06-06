/*
 * Copyright 2021-2024, Darchest and contributors.
 * Licensed under the Apache License, Version 2.0
 */

package org.darchest.insight.vendor.mysql

import org.darchest.insight.ComparisonOperation
import org.darchest.insight.LogicalOperation
import org.darchest.insight.SqlConst
import org.darchest.insight.SqlValue
import java.time.Instant

infix fun<sqlT: MySqlType> SqlValue<*, out sqlT>.eq(right: SqlValue<*, out sqlT>) = MySqlComparison(this, ComparisonOperation.Operator.EQ, right)
infix fun<sqlT: MySqlType> SqlValue<*, out sqlT>.neq(right: SqlValue<*, out sqlT>) = MySqlComparison(this, ComparisonOperation.Operator.NEQ, right)

infix fun<sqlT: MySqlType> SqlValue<*, out sqlT>.gt(right: SqlValue<*, out sqlT>) = MySqlComparison(this, ComparisonOperation.Operator.GT, right)
infix fun<sqlT: MySqlType> SqlValue<*, out sqlT>.ge(right: SqlValue<*, out sqlT>) = MySqlComparison(this, ComparisonOperation.Operator.GE, right)

inline infix fun<reified J: Any, reified sqlT: MySqlType> SqlValue<J, out sqlT>.eq(right: J?) = MySqlComparison(this, ComparisonOperation.Operator.EQ, SqlConst(right, J::class.java, this.sqlType))
inline infix fun<reified J: Any, reified sqlT: MySqlType> SqlValue<J, out sqlT>.neq(right: J?) = MySqlComparison(this, ComparisonOperation.Operator.NEQ, SqlConst(right, J::class.java, this.sqlType))

inline infix fun<reified J: Any, reified sqlT: MySqlType> SqlValue<J, out sqlT>.gt(right: J?) = MySqlComparison(this, ComparisonOperation.Operator.GT, SqlConst(right, J::class.java, this.sqlType))
inline infix fun<reified J: Any, reified sqlT: MySqlType> SqlValue<J, out sqlT>.ge(right: J?) = MySqlComparison(this, ComparisonOperation.Operator.GE, SqlConst(right, J::class.java, this.sqlType))

inline infix fun SqlValue<Instant, *>.eq(right: Long) = MySqlComparison(this, ComparisonOperation.Operator.EQ, SqlConst(right, Long::class.java, this.sqlType))
inline infix fun SqlValue<Instant, *>.neq(right: Long) = MySqlComparison(this, ComparisonOperation.Operator.NEQ, SqlConst(right, Long::class.java, this.sqlType))

infix fun SqlValue<*, out BooleanType>.and(right: SqlValue<*, out BooleanType>) = MySqlLogical(LogicalOperation.Operator.AND, listOf(this, right))
infix fun SqlValue<*, out BooleanType>.or(right: SqlValue<*, out BooleanType>) = MySqlLogical(LogicalOperation.Operator.OR, listOf(this, right))

fun and(vararg values: SqlValue<*, out BooleanType>) = MySqlLogical(LogicalOperation.Operator.AND, values.toList())
fun or(vararg values: SqlValue<*, out BooleanType>) = MySqlLogical(LogicalOperation.Operator.OR, values.toList())

fun and(values: List<SqlValue<*, out BooleanType>>) = MySqlLogical(LogicalOperation.Operator.AND, values)
fun or(values: List<SqlValue<*, out BooleanType>>) = MySqlLogical(LogicalOperation.Operator.OR, values)
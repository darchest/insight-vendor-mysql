/*
 * Copyright 2021-2024, Darchest and contributors.
 * Licensed under the Apache License, Version 2.0
 */

package org.darchest.insight.vendor.mysql

import org.darchest.insight.Join
import org.darchest.insight.Table
import org.darchest.insight.TableColumn
import org.darchest.insight.Vendor
import java.util.*
import kotlin.reflect.KProperty

open class MySqlTable(name: String): Table(name) {

	override fun vendor(): Vendor = MySqlVendor

	class JoinDelegate<T: MySqlTable>(private val tableFactory: () -> T, private val setup: Join<T>.(T) -> Unit) {
		lateinit var join: Join<T>

		operator fun provideDelegate(thisRef: MySqlTable, prop: KProperty<*>): JoinDelegate<T> {
			join = Join(thisRef, tableFactory)
			join.codeName = (if (thisRef.joined != null) thisRef.joined!!.codeName + prop.name else prop.name) + "."
			thisRef.joinByNames[prop.name] = join
			return this
		}

		operator fun getValue(thisRef: MySqlTable, prop: KProperty<*>): Join<T> {
			if (!join.inited) {
				join.codeName = (if (thisRef.joined != null) thisRef.joined!!.codeName + prop.name else prop.name) + "."
				val t = join()
				setup.invoke(join, t)
			}
			return join
		}
	}

	open class ColDelegate<T: MySqlColumn<*, *>>(private val col: T) {

		operator fun provideDelegate(thisRef: MySqlTable, prop: KProperty<*>): ColDelegate<T> {
			thisRef.registerColumn(col)
			col.sqlDataSource = thisRef
			col.codeName = if (thisRef.joined != null) thisRef.joined!!.codeName + prop.name else prop.name
			thisRef.sqlByNames[prop.name] = col
			thisRef.namesBySql[col] = prop.name
			return this
		}

		operator fun getValue(thisRef: MySqlTable, property: KProperty<*>): T {
			return col
		}
	}

	class UUIDCol(name: String): ColDelegate<UuidColumn>(UuidColumn(name))

	class VarCharCol(name: String): ColDelegate<VarCharColumn>(VarCharColumn(name))

	class ByteaTextCol(name: String): ColDelegate<ByteaTextColumn>(ByteaTextColumn(name))

	class BinaryCol(name: String): ColDelegate<BinaryColumn>(BinaryColumn(name))

	class ShortCol(name: String): ColDelegate<ShortColumn>(ShortColumn(name))

	class IntCol(name: String): ColDelegate<IntColumn>(IntColumn(name))

	class LongCol(name: String): ColDelegate<LongColumn>(LongColumn(name))

	class BoolCol(name: String): ColDelegate<BoolColumn>(BoolColumn(name))

	class DatetimeCol(name: String): ColDelegate<DatetimeColumn>(DatetimeColumn(name))

	fun <T: MySqlTable> countExpr() = CountExpression()

	open class ExprDelegate<T: MySqlExpression<*, *>>(private val expr: T) {

		operator fun provideDelegate(thisRef: MySqlTable, prop: KProperty<*>): ExprDelegate<T> {
			expr.sqlDataSource = thisRef
			expr.codeName = if (thisRef.joined != null) thisRef.joined!!.codeName + prop.name else prop.name
			thisRef.sqlByNames[prop.name] = expr
			thisRef.namesBySql[expr] = prop.name
			return this
		}

		operator fun getValue(thisRef: MySqlTable, property: KProperty<*>): T {
			return expr
		}
	}

	class StringExpr(value: String): ExprDelegate<StringExpression>(StringExpression(value))

	class BoolExpr(value: Boolean): ExprDelegate<BooleanExpression>(BooleanExpression(value))

	class StringLocalExpression(innerColumns: List<TableColumn<*, *>>, fn: () -> String?): MySqlLocalExpression<String>(String::class.java, innerColumns, fn)

	class BooleanLocalExpression(innerColumns: List<TableColumn<*, *>>, fn: () -> Boolean?): MySqlLocalExpression<Boolean>(Boolean::class.java, innerColumns, fn)

	class UuidLocalExpression(innerColumns: List<TableColumn<*, *>>, fn: () -> UUID?): MySqlLocalExpression<UUID>(UUID::class.java, innerColumns, fn)

	open class LocalExprDelegate<T: MySqlLocalExpression<*>>(private val expr: T) {

		operator fun provideDelegate(thisRef: MySqlTable, prop: KProperty<*>): LocalExprDelegate<T> {
			expr.sqlDataSource = thisRef
			expr.codeName = if (thisRef.joined != null) thisRef.joined!!.codeName + prop.name else prop.name
			thisRef.sqlByNames[prop.name] = expr
			thisRef.namesBySql[expr] = prop.name
			return this
		}

		operator fun getValue(thisRef: MySqlTable, property: KProperty<*>): T {
			return expr
		}
	}

	class StringLocalExpr(innerColumns: List<TableColumn<*, *>>, fn: () -> String?): LocalExprDelegate<StringLocalExpression>(StringLocalExpression(innerColumns, fn))

	class BoolLocalExpr(innerColumns: List<TableColumn<*, *>>, fn: () -> Boolean?): LocalExprDelegate<BooleanLocalExpression>(BooleanLocalExpression(innerColumns, fn))

	class UuidLocalExpr(innerColumns: List<TableColumn<*, *>>, fn: () -> UUID?): LocalExprDelegate<UuidLocalExpression>(UuidLocalExpression(innerColumns, fn))
}
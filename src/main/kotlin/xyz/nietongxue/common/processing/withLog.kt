package xyz.nietongxue.common.processing

import xyz.nietongxue.common.log.Log
import xyz.nietongxue.common.validate.ValidateResult

typealias ProcessingWithLog<V> = Processing<Log, ValidateResult, V>
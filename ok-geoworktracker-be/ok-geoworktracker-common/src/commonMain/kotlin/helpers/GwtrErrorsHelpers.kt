package ru.otus.otuskotlin.marketplace.common.helpers

import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.models.GwtrError
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.logging.common.LogLevel

fun Throwable.asGwtrError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = GwtrError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun GwtrContext.addError(error: GwtrError) = errors.add(error)
inline fun GwtrContext.addErrors(error: Collection<GwtrError>) = errors.addAll(error)

inline fun GwtrContext.fail(error: GwtrError) {
    addError(error)
    state = GwtrState.FAILING
}

inline fun GwtrContext.fail(errors: Collection<GwtrError>) {
    addErrors(errors)
    state = GwtrState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = GwtrError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = GwtrError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)

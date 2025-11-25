package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.common.models.GwtrCommand
import kotlin.test.Test

// смотрим пример теста валидации, собранного из тестовых функций-оберток
class BizValidationCreateTest: BaseBizValidationTest() {
    override val command: GwtrCommand = GwtrCommand.CREATE

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)
}

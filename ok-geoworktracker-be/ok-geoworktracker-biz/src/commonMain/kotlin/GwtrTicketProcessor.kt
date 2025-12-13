package ru.otus.otuskotlin.marketplace.biz

import ru.otus.otuskotlin.marketplace.biz.general.getTicketState
import ru.otus.otuskotlin.marketplace.biz.general.getTicketStates
import ru.otus.otuskotlin.marketplace.biz.general.initStatus
import ru.otus.otuskotlin.marketplace.biz.general.operation
import ru.otus.otuskotlin.marketplace.biz.general.stubs
import ru.otus.otuskotlin.marketplace.biz.repo.*
import ru.otus.otuskotlin.marketplace.biz.stubs.*
import ru.otus.otuskotlin.marketplace.biz.validation.*
import ru.otus.otuskotlin.marketplace.common.GwtrContext
import ru.otus.otuskotlin.marketplace.common.GwtrCorSettings
import ru.otus.otuskotlin.marketplace.common.models.GwtrCommand
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketId
import ru.otus.otuskotlin.marketplace.common.models.GwtrTicketLock
import ru.otus.otuskotlin.marketplace.common.models.GwtrState
import ru.otus.otuskotlin.marketplace.cor.chain
import ru.otus.otuskotlin.marketplace.cor.rootChain
import ru.otus.otuskotlin.marketplace.cor.worker

class GwtrTicketProcessor(
    private val corSettings: GwtrCorSettings = GwtrCorSettings.NONE
) {
    suspend fun exec(ctx: GwtrContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<GwtrContext> {
        initStatus("Инициализация статуса")

        operation("Создание тикета", GwtrCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketValidating") { ticketValidating = ticketRequest.deepCopy() }
                worker("Очистка id") { ticketValidating.id = GwtrTicketId.NONE }
                worker("Очистка заголовка") { ticketValidating.title = ticketValidating.title.trim() }
                worker("Очистка описания") { ticketValidating.description = ticketValidating.description.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateTitleHasContent("Проверка символов")
                validateDescriptionNotEmpty("Проверка, что описание не пусто")
                validateDescriptionHasContent("Проверка символов")

                finishTicketValidation("Завершение проверок")
            }

            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                //accessValidation("Вычисление прав доступа")
                repoCreate("Создание объявления в БД")
            }
            //frontPermissions("Вычисление пользовательских разрешений для фронтенда")
            getTicketState("Вычисление состояния объявления")
            prepareResult("Подготовка ответа")
        }
        operation("Получить тикет", GwtrCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketValidating") { ticketValidating = ticketRequest.deepCopy() }
                worker("Очистка id") { ticketValidating.id = GwtrTicketId(ticketValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishTicketValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика чтения"
                repoRead("Чтение объявления из БД")
                //accessValidation("Вычисление прав доступа")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == GwtrState.RUNNING }
                    handle { ticketRepoDone = ticketRepoRead }
                }
            }
            //frontPermissions("Вычисление пользовательских разрешений для фронтенда")
            getTicketState("Вычисление состояния объявления")
            prepareResult("Подготовка ответа")
        }
        operation("Изменить тикет", GwtrCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketValidating") { ticketValidating = ticketRequest.deepCopy() }
                worker("Очистка id") { ticketValidating.id = GwtrTicketId(ticketValidating.id.asString().trim()) }
                worker("Очистка lock") { ticketValidating.lock = GwtrTicketLock(ticketValidating.lock.asString().trim()) }
                worker("Очистка заголовка") { ticketValidating.title = ticketValidating.title.trim() }
                worker("Очистка описания") { ticketValidating.description = ticketValidating.description.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateTitleHasContent("Проверка на наличие содержания в заголовке")
                validateDescriptionNotEmpty("Проверка на непустое описание")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")

                finishTicketValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика сохранения"
                repoRead("Чтение тикета из БД")
                //accessValidation("Вычисление прав доступа")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление тикета в БД")
            }
            //frontPermissions("Вычисление пользовательских разрешений для фронтенда")
            getTicketState("Вычисление состояния объявления")
            prepareResult("Подготовка ответа")
        }
        operation("Удалить тикет", GwtrCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketValidating") {
                    ticketValidating = ticketRequest.deepCopy()
                }
                worker("Очистка id") { ticketValidating.id = GwtrTicketId(ticketValidating.id.asString().trim()) }
                worker("Очистка lock") { ticketValidating.lock = GwtrTicketLock(ticketValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishTicketValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика удаления"
                repoRead("Чтение тикета из БД")
                //accessValidation("Вычисление прав доступа")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление тикета из БД")
            }
            //frontPermissions("Вычисление пользовательских разрешений для фронтенда")
            getTicketState("Вычисление состояния объявления")
            prepareResult("Подготовка ответа")
        }
        operation("Поиск тикетов", GwtrCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ticketFilterValidating") { ticketFilterValidating = ticketFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishTicketFilterValidation("Успешное завершение процедуры валидации")
            }
            //searchTypes("Подготовка поискового запроса")

            repoSearch("Поиск объявления в БД по фильтру")
            //frontPermissions("Вычисление пользовательских разрешений для фронтенда")
            getTicketStates("Вычисление состояния тикета")
            prepareResult("Подготовка ответа")
        }
    }.build()
}

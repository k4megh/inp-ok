package ru.otus.otuskotlin.marketplace.common.exceptions

import ru.otus.otuskotlin.marketplace.common.models.GwtrCommand


class UnknownGwtrCommand(command: GwtrCommand) : Throwable("Wrong command $command at mapping toTransport stage")

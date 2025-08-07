package ru.otus.otuskotlin.marketplace

//TIP Чтобы запустить код, нажмите <shortcut actionId="Run"/> или
// щёлкните значок <icon src="AllIcons.Actions.Execute"/> на полях.
fun main() {
    val name = "Kotlin"
    //TIP Наведите курсор на подсвеченный текст и нажмите <shortcut actionId="ShowIntentionActions"/>
    // чтобы увидеть, как GIGA IDE предлагает это исправить.
    println("Hello, " + name + "!")

    for (i in 1..5) {
        //TIP Нажмите <shortcut actionId="Debug"/> для начала отладки кода. Мы установили одну <icon src="AllIcons.Debugger.Db_set_breakpoint"/> точку останова
        // для вас, но вы всегда можете добавить еще, нажав <shortcut actionId="ToggleLineBreakpoint"/>.
        println("i = $i")
    }
}
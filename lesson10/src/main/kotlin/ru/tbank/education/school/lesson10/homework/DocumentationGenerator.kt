package ru.tbank.education.school.lesson10.homework

import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.*

object DocumentationGenerator {
    fun generateDoc(obj: Any): String {
        val kClass = obj::class
        if (kClass.findAnnotation<InternalApi>() != null) {
            return "Документация скрыта (InternalApi)."
        }
        val classDoc = kClass.findAnnotation<DocClass>()
            ?: return "Нет документации для класса."
        val sb = StringBuilder()
        sb.appendLine("=== Документация: ${kClass.simpleName} ===")
        sb.appendLine("Описание: ${classDoc.description}")
        sb.appendLine("Автор: ${classDoc.author}")
        sb.appendLine("Версия: ${classDoc.version}")
        sb.appendLine()
        val properties = kClass.memberProperties
            .filter { it.findAnnotation<InternalApi>() == null }
        if (properties.isNotEmpty()) {
            sb.appendLine("--- Свойства ---")
            for (prop in properties) {
                sb.appendLine("- ${prop.name}")
                val doc = prop.findAnnotation<DocProperty>()
                if (doc != null) {
                    if (doc.description.isNotBlank()) {
                        sb.appendLine("  Описание: ${doc.description}")
                    }
                    if (doc.example.isNotBlank()) {
                        sb.appendLine("  Пример: ${doc.example}")
                    }
                }
                sb.appendLine()
            }
        }
        val excludedMethodNames = setOf("toString", "equals", "hashCode", "copy")
        val methods = kClass.memberFunctions
            .filter { it.findAnnotation<InternalApi>() == null }
            .filterNot { it.name in excludedMethodNames || it.name.startsWith("component") }

        if (methods.isNotEmpty()) {
            sb.appendLine("--- Методы ---")
            for (method in methods) {
                val params = method.parameters.drop(1)
                val signature = params.joinToString(", ") {
                    "${it.name}: ${it.type.jvmErasure.simpleName}"
                }
                sb.appendLine("- ${method.name}($signature)")
                val methodDoc = method.findAnnotation<DocMethod>()
                sb.appendLine("  Описание: ${methodDoc?.description?.takeIf { it.isNotBlank() } ?: "Нет описания"}")
                if (params.isNotEmpty()) {
                    sb.appendLine("  Параметры:")
                    for (param in params) {
                        val paramDoc = param.findAnnotation<DocParam>()
                        sb.appendLine("    - ${param.name}: ${paramDoc?.description ?: "Нет описания"}")
                    }
                }
                sb.appendLine("  Возвращает: ${methodDoc?.returns?.takeIf { it.isNotBlank() } ?: "Нет описания"}")
                sb.appendLine()
            }
        }
        return sb.toString().replace("token", "").trimEnd()
    }
}

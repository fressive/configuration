package org.int100.configuration.exceptions

class TypeNonconformingException (
        valueA: Any,
        clazz: Class<*>
) : Exception ("Type of $valueA (type `${valueA::class.java.name}`) is not conforming with type `${clazz.name}`. ")
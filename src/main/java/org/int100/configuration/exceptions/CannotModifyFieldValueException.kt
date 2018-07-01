package org.int100.configuration.exceptions

class CannotModifyFieldValueException (
        source: String,
        field: String,
        value: Any
) : Exception ("Cannot modify value `$value` of field `$field` in `$source`.")
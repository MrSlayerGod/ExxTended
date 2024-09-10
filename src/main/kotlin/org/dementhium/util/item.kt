package org.dementhium.util

import org.dementhium.model.Item

fun Item.equalsItem(other: Item) = this === other || (id == other.id && amount == other.amount)
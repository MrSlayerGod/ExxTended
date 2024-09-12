package org.dementhium.util.item

import org.dementhium.model.Item

fun Item.copy(newId: Int = this.id, newAmount: Int = this.amount): Item = Item(newId, newAmount)
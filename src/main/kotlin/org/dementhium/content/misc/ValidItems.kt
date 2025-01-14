package org.dementhium.content.misc

import org.dementhium.util.random.weightedTable


/**
 * Rewritten by Nbness2
 */
object ValidItems {

    fun canSpawn(itemId: Int): Boolean = false

    @JvmField
    var itemDrops = listOf(
        55 to listOf(3840, 3842, 3844, 19613, 19615, 19617, 2412, 2413, 2414, 8844, 8845, 8846, 8847, 8848, 8849, 7453, 7454, 7455, 7456, 7457, 7458, 7459, 7460, 861, 4587, 6568, 1215, 5698, 4153, 6524, 6528, 6526, 2491, 2497, 2503, 9185, 1540, 11126, 1704, 1725, 3105, 2572, 2550, 3751, 3755, 3749, 3753, 10564, 6809, 3122, 6128, 6129, 6130, 11090, 2415, 2416, 2417, 3385, 3387, 3389, 3391, 3393, 10887, 15017),
        2 to listOf(11718, 11720, 11722, 11724, 11726, 11728, 11716, 11335, 14479, 11284, 11732, 7462, 7461, 4214, 6570, 15600, 15602, 15604, 15606, 15608, 15610, 15612, 15614, 15616, 15618, 15620, 15622, 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 6731, 6733, 6735, 6737, 15220, 15018, 15019, 15020, 13899, 13902),
        1 to listOf(11730, 20171),
        3 to listOf(2577, 2581, 6585, 4151, 8850, 10828, 11235, 6914, 6889, 10551, 6916, 6918, 6920, 6922, 6924),
        4 to listOf(15126, 4675, 3140, 4087, 4585),
        5 to listOf(1149, 1187),
    ).weightedTable()

    fun pvpDrop(modifier: Double = 1.0) = itemDrops.roll(modifier)

    @JvmStatic
    fun PvPDrop(): Int {
        return pvpDrop(1.0)
    }
}


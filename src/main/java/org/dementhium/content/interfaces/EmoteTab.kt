package org.dementhium.content.interfaces

import org.dementhium.event.Tickable
import org.dementhium.model.World
import org.dementhium.model.mask.Animation
import org.dementhium.model.mask.Graphics
import org.dementhium.model.player.Equipment.SLOT_CAPE
import org.dementhium.model.player.Player
import org.dementhium.util.Tickable

class SkillcapeEmote(
    val skillcapeId: Int,
    val skillcapeAnim: Int,
    val skillcapeGraphics: Int
)

object EmoteTab {

    @Suppress("unused")
    private val buttonIds = intArrayOf(
        2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
        12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
        22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
        32, 33, 34, 35, 36, 37, 38, 40, 41, 42,
        43, 44, 45, 46, 47, 48
    )

    private val animation = arrayOf(
        Animation.create(0x357), Animation.create(0x358), Animation.create(0x35A), Animation.create(0x35B),
        Animation.create(0x359), Animation.create(0x35F), Animation.create(0x841), Animation.create(0x35E),
        Animation.create(0x360), Animation.create(0x83D), Animation.create(0x35D), Animation.create(0x83F),
        Animation.create(0x362), Animation.create(0x83A), Animation.create(0x83B), Animation.create(0x83C),
        Animation.create(0x35C), Animation.create(0x558), Animation.create(0x839), Animation.create(0x83E),
        Animation.create(0x361), Animation.create(0x840), Animation.create(0x84F), Animation.create(0x850),
        Animation.create(0x46B), Animation.create(0x46A), Animation.create(0x469), Animation.create(0x468),
        Animation.create(0x10B3), Animation.create(0x6D1), Animation.create(0x10B8), Animation.create(0x10B4),
        Animation.create(0xDD8), Animation.create(0xDD7), Animation.create(0x1C68), Animation.create(0xB14),
        Animation.create(0x17DF), null, Animation.create(0x1D6B), Animation.create(0x96E), Animation.create(0x2242),
        Animation.create(0x2706), Animation.create(0x2922), Animation.create(0x2B24), Animation.create(0x357),
        Animation.create(0x2D16), Animation.create(0x3172)
    )

    private val graphics = arrayOf(
        Graphics.create(574), Graphics.create(1244), Graphics.create(1537), Graphics.create(1553),
        Graphics.create(1734), Graphics.create(1864), Graphics.create(1973), Graphics.create(2037)
    )

    private val skillcapeEmotes = arrayOf(
        SkillcapeEmote(9747, 4959, 823), SkillcapeEmote(10639, 4959, 823), SkillcapeEmote(9748, 4959, 823),  //Attack
        SkillcapeEmote(9753, 4961, 824), SkillcapeEmote(10641, 4961, 824), SkillcapeEmote(9754, 4961, 824),  //Defense
        SkillcapeEmote(9750, 4981, 828), SkillcapeEmote(10640, 4981, 828), SkillcapeEmote(9751, 4981, 828),  //Strength
        SkillcapeEmote(9768, 14242, 2745), SkillcapeEmote(10647, 14242, 2745), SkillcapeEmote(9769, 14242, 2745),  //Constitution
        SkillcapeEmote(9756, 4973, 832), SkillcapeEmote(10642, 4973, 832), SkillcapeEmote(9757, 4973, 832),  //Ranged
        SkillcapeEmote(9762, 4939, 813), SkillcapeEmote(10644, 4939, 813), SkillcapeEmote(9763, 4939, 813),  //Magic
        SkillcapeEmote(9759, 4979, 829), SkillcapeEmote(10643, 4979, 829), SkillcapeEmote(9760, 4979, 829),  //Prayer
        SkillcapeEmote(9801, 4955, 821), SkillcapeEmote(10658, 4955, 821), SkillcapeEmote(9802, 4955, 821),  //Cooking
        SkillcapeEmote(9807, 4957, 822), SkillcapeEmote(10660, 4957, 822), SkillcapeEmote(9808, 4957, 822),  //Woodcutting
        SkillcapeEmote(9783, 4937, 812), SkillcapeEmote(10652, 4937, 812), SkillcapeEmote(9784, 4937, 812),  //Fletching
        SkillcapeEmote(9798, 4951, 819), SkillcapeEmote(10657, 4951, 819), SkillcapeEmote(9799, 4951, 819),  //Fishing
        SkillcapeEmote(9804, 4975, 831), SkillcapeEmote(10659, 4975, 831), SkillcapeEmote(9805, 4975, 831),  //Firemaking
        SkillcapeEmote(9780, 4949, 818), SkillcapeEmote(10651, 4949, 818), SkillcapeEmote(9781, 4949, 818),  //Crafting
        SkillcapeEmote(9795, 4943, 815), SkillcapeEmote(10656, 4943, 815), SkillcapeEmote(9796, 4943, 815),  //Smithing
        SkillcapeEmote(9792, 4941, 814), SkillcapeEmote(10655, 4941, 814), SkillcapeEmote(9793, 4941, 814),  //Mining
        SkillcapeEmote(9774, 4969, 835), SkillcapeEmote(10649, 4969, 835), SkillcapeEmote(9775, 4969, 835),  //Herblore
        SkillcapeEmote(9771, 4977, 830), SkillcapeEmote(10648, 4977, 830), SkillcapeEmote(9772, 4977, 830),  //Agility
        SkillcapeEmote(9777, 4965, 826), SkillcapeEmote(10650, 4965, 826), SkillcapeEmote(9778, 4965, 826),  //Thieving
        SkillcapeEmote(9786, 4967, 1656), SkillcapeEmote(10653, 4967, 1656), SkillcapeEmote(9787, 4967, 1656),  //Slayer
        SkillcapeEmote(9810, 4963, 825), SkillcapeEmote(10661, 4963, 825), SkillcapeEmote(9811, 4963, 825),  //Farming
        SkillcapeEmote(9765, 4947, 817), SkillcapeEmote(10645, 4947, 817), SkillcapeEmote(9766, 4947, 817),  //Runecrafting
        SkillcapeEmote(9789, 4953, 820), SkillcapeEmote(10654, 4953, 820), SkillcapeEmote(9790, 4953, 820),  //Construction
        SkillcapeEmote(12524, 8525, 1515), SkillcapeEmote(12169, 8525, 1515), SkillcapeEmote(12170, 8525, 1515),  //Summoning
        SkillcapeEmote(9948, 5158, 907), SkillcapeEmote(10646, 5158, 907), SkillcapeEmote(9949, 5158, 907),  //Hunter
        SkillcapeEmote(9813, 4945, 816), SkillcapeEmote(10662, 4945, 816)
    )

    private fun doskillcapeEmote(p: Player) = skillcapeEmotes.firstOrNull { skillcape ->
        p.equipment.getSlot(SLOT_CAPE) == skillcape.skillcapeId
    }?.let { skillcape ->
        p.animate(Animation.create(skillcape.skillcapeAnim))
        p.graphics(Graphics.create(skillcape.skillcapeGraphics))
    }

    private val DungeoneeringCapes = intArrayOf(15706, 18508, 18509, 19709, 19710)

    fun handleButton(p: Player, buttonId: Int, buttonId2: Int, buttonId3: Int) {
        if (buttonId !in 0..48) { return }
        if (buttonId == 39) {
            doskillcapeEmote(p)
            if (p.equipment.getSlot(SLOT_CAPE) in DungeoneeringCapes) {
                p.animate(Animation.create(13190))
                p.graphics(Graphics.create(2442))
                val rand = (Math.random() * (2 + 1)).toInt()
                World.getWorld().submit(Tickable(2) {
                    p.appearence.npcType = (11227 + rand).toShort()
                    p.mask.isApperanceUpdate = true
                    p.animate(Animation.create(13192 + rand))
                    this.stop()
                })
                World.getWorld().submit(Tickable(7) {
                    p.appearence.npcType = (-1).toShort()
                    p.mask.isApperanceUpdate = true
                    this.stop()
                })
            }
        } else if (buttonId == 46) {
            p.animate(Animation.create(10994))
            p.graphics(Graphics.create(189))
            World.getWorld().submit(Tickable(1) {
                p.animate(Animation.create(10996))
                p.appearence.npcType = 8499.toShort()
                p.mask.isApperanceUpdate = true
                this.stop()

            })
            World.getWorld().submit(Tickable(8) {
                p.animate(Animation.create(10995))
                p.graphics(Graphics.create(189))
                p.appearence.npcType = (-1).toShort()
                p.appearence.resetAppearence()
                p.appearence.isMale = true
                p.mask.isApperanceUpdate = true
                this.stop()

            })
            //p.sendMessage("Turkey Emote coming soon!");
        } else {
            p.animate(animation[buttonId - 2])
            when (buttonId) {
                19 -> p.graphics(graphics[0])
                36 -> p.graphics(graphics[1])
                41 -> p.graphics(graphics[2])
                42 -> p.graphics(graphics[3])
                43 -> p.graphics(graphics[4])
                44 -> p.graphics(graphics[5])
                45 -> p.graphics(graphics[6])
                47 -> p.graphics(graphics[7])
            }
        }
    }
}
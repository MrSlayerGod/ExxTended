package org.dementhium.content.interfaces;

import org.dementhium.event.Tickable;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphics;
import org.dementhium.model.player.Player;


/**
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class EmoteTab {
	/**
	 * Button Id's for each emote
	 */
	@SuppressWarnings("unused")
	private static int[] buttonIds = {2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,40,41,42,43,44,45,46,47,48};
	
	/**
	 * Animation Id for emote
	 */
	private static Animation[] animation = {Animation.create(0x357),Animation.create(0x358),Animation.create(0x35A),Animation.create(0x35B),
		Animation.create(0x359),Animation.create(0x35F),Animation.create(0x841),Animation.create(0x35E),
		Animation.create(0x360),Animation.create(0x83D),Animation.create(0x35D),Animation.create(0x83F),
		Animation.create(0x362),Animation.create(0x83A),Animation.create(0x83B),Animation.create(0x83C),
		Animation.create(0x35C),Animation.create(0x558),Animation.create(0x839),Animation.create(0x83E),
		Animation.create(0x361),Animation.create(0x840),Animation.create(0x84F),Animation.create(0x850),
		Animation.create(0x46B),Animation.create(0x46A),Animation.create(0x469),Animation.create(0x468),
		Animation.create(0x10B3),Animation.create(0x6D1),Animation.create(0x10B8),Animation.create(0x10B4),
		Animation.create(0xDD8),Animation.create(0xDD7),Animation.create(0x1C68),Animation.create(0xB14),
		Animation.create(0x17DF),null,Animation.create(0x1D6B),Animation.create(0x96E),Animation.create(0x2242),
		Animation.create(0x2706),Animation.create(0x2922),Animation.create(0x2B24),Animation.create(0x357),
		Animation.create(0x2D16),Animation.create(0x3172)};
	/**
	 * Graphic Id's for emote
	 */
	private static Graphics[] graphics = {Graphics.create(574),Graphics.create(1244),Graphics.create(1537),Graphics.create(1553),Graphics.create(1734),
		Graphics.create(1864),Graphics.create(1973),Graphics.create(2037)};
	/**
	 * Holds the skillcape emote information as (Item id, emote, gfx)
	 */
	private static int[][] skillcape = {{9747,4959,823},{10639,4959,823},{9748,4959,823},//Attack
		{9753,4961,824},{10641,4961,824},{9754,4961,824},//Defense
		{9750,4981,828},{10640,4981,828},{9751,4981,828},//Strength
		{9768,14242,2745},{10647,14242,2745},{9769,14242,2745},//Constitution
		{9756,4973,832},{10642,4973,832},{9757,4973,832},//Ranged
		{9762,4939,813},{10644,4939,813},{9763,4939,813},//Magic
		{9759,4979,829},{10643,4979,829},{9760,4979,829},//Prayer
		{9801,4955,821},{10658,4955,821},{9802,4955,821},//Cooking
		{9807,4957,822},{10660,4957,822},{9808,4957,822},//Woodcutting
		{9783,4937,812},{10652,4937,812},{9784,4937,812},//Fletching
		{9798,4951,819},{10657,4951,819},{9799,4951,819},//Fishing
		{9804,4975,831},{10659,4975,831},{9805,4975,831},//Firemaking
		{9780,4949,818},{10651,4949,818},{9781,4949,818},//Crafting
		{9795,4943,815},{10656,4943,815},{9796,4943,815},//Smithing
		{9792,4941,814},{10655,4941,814},{9793,4941,814},//Mining
		{9774,4969,835},{10649,4969,835},{9775,4969,835},//Herblore
		{9771,4977,830},{10648,4977,830},{9772,4977,830},//Agility
		{9777,4965,826},{10650,4965,826},{9778,4965,826},//Thieving
		{9786,4967,1656},{10653,4967,1656},{9787,4967,1656},//Slayer
		{9810,4963,825},{10661,4963,825},{9811,4963,825},//Farming
		{9765,4947,817},{10645,4947,817},{9766,4947,817},//Runecrafting
		{9789,4953,820},{10654,4953,820},{9790,4953,820},//Construction
		{12524,8525,1515},{12169,8525,1515},{12170,8525,1515},//Summoning
		{9948,5158,907},{10646,5158,907},{9949,5158,907},//Hunter
		{9813,4945,816},{10662,4945,816}};

	private static void doskillcapeEmote(Player p){
		for(int i=0;i<skillcape.length;i++){
			if(p.getEquipment().getSlot(1) == skillcape[i][0]){
				p.animate(Animation.create(skillcape[i][1]));
				p.graphics(Graphics.create(skillcape[i][2]));
			}
		}
	}

	public static void handleButton(final Player p, int buttonId, int buttonId2, int buttonId3) {
		if(buttonId < 0 || buttonId > 48){
			return;
		}
		if(buttonId == 39){
			doskillcapeEmote(p);
			if(p.getEquipment().getSlot(1) == 15706 || p.getEquipment().getSlot(1) == 18508 || p.getEquipment().getSlot(1) == 18509 || p.getEquipment().getSlot(1) == 19709 || p.getEquipment().getSlot(1) == 19710){
				p.animate(Animation.create(13190));
				p.graphics(Graphics.create(2442));
				final int rand = (int) (Math.random() * (2 + 1));
				World.getWorld().submit(new Tickable(2){
				
					public void execute(){
						p.getAppearence().setNpcType((short) (rand==0?11227:(rand==1?11228:11229)));
						p.getMask().setApperanceUpdate(true);
						p.animate(Animation.create(rand==0?13192:(rand==1?13193:13194)));
						this.stop();
					}
				});
				World.getWorld().submit(new Tickable(7){
					
					public void execute(){
						p.getAppearence().setNpcType((short) -1);
						p.getMask().setApperanceUpdate(true);
						this.stop();
					}
				});
				
			}
		}else if(buttonId == 46){
			p.animate(Animation.create(10994));
			p.graphics(Graphics.create(189));
			World.getWorld().submit(new Tickable(1){

				public void execute(){
					p.animate(Animation.create(10996));
					p.getAppearence().setNpcType((short)8499);
					p.getMask().setApperanceUpdate(true);
					this.stop();
				}
			});
			World.getWorld().submit(new Tickable(8){

				public void execute(){
					p.animate(Animation.create(10995));
					p.graphics(Graphics.create(189));
					p.getAppearence().setNpcType((short)-1);
					p.getAppearence().resetAppearence();
					p.getAppearence().setMale(true);
					p.getMask().setApperanceUpdate(true);
					this.stop();
				}
			});
			//p.sendMessage("Turkey Emote coming soon!");
		}else{
			p.animate(animation[buttonId-2]);
			if(buttonId == 19)
				p.graphics(graphics[0]);
			else if(buttonId == 36)
				p.graphics(graphics[1]);
			else if(buttonId == 41)
				p.graphics(graphics[2]);
			else if(buttonId == 42)
				p.graphics(graphics[3]);
			else if(buttonId == 43)
				p.graphics(graphics[4]);
			else if(buttonId == 44)
				p.graphics(graphics[5]);
			else if(buttonId == 45)
				p.graphics(graphics[6]);
			else if(buttonId == 47)
				p.graphics(graphics[7]);
		}	 		
	}



}

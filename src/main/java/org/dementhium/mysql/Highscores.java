package org.dementhium.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.util.Misc;

public class Highscores extends SQLConnection {

	public Highscores() {
		super();
	}


    public boolean saveHighScore(Player player) {
        try {
            String username = Misc.formatPlayerNameForDisplay(player.getUsername());
            Skills skills = player.getSkills();
            int[] overall = getOverall(player);
            updateQuery("DELETE FROM `skills` WHERE playerName = '"+username+"';");
            updateQuery("DELETE FROM `skillsoverall` WHERE playerName = '"+username+"';");
            updateQuery("INSERT INTO `skills` (`playerName`,`Attacklvl`,`Attackxp`,`Defencelvl`,`Defencexp`,`Strengthlvl`,`Strengthxp`,`Hitpointslvl`,`Hitpointsxp`,`Rangelvl`,`Rangexp`,`Prayerlvl`,`Prayerxp`,`Magiclvl`,`Magicxp`,`Cookinglvl`,`Cookingxp`,`Woodcuttinglvl`,`Woodcuttingxp`,`Fletchinglvl`,`Fletchingxp`,`Fishinglvl`,`Fishingxp`,`Firemakinglvl`,`Firemakingxp`,`Craftinglvl`,`Craftingxp`,`Smithinglvl`,`Smithingxp`,`Mininglvl`,`Miningxp`,`Herblorelvl`,`Herblorexp`,`Agilitylvl`,`Agilityxp`,`Thievinglvl`,`Thievingxp`,`Slayerlvl`,`Slayerxp`,`Farminglvl`,`Farmingxp`,`Runecraftlvl`,`Runecraftxp`, `Hunterlvl`, `Hunterxp`, `Constructionlvl`, `Constructionxp`, `Summoninglvl`, `Summoningxp`,`Dungeoneeringlvl`,`Dungeoneeringxp`) VALUES ('"+username+"',"+skills.getLevelForXp(0)+","+skills.getXp(0)+","+skills.getLevelForXp(1)+","+skills.getXp(1)+","+skills.getLevelForXp(2)+","+skills.getXp(2)+","+skills.getLevelForXp(3)+","+skills.getXp(3)+","+skills.getLevelForXp(4)+","+skills.getXp(4)+","+skills.getLevelForXp(5)+","+skills.getXp(5)+","+skills.getLevelForXp(6)+","+skills.getXp(6)+","+skills.getLevelForXp(7)+","+skills.getXp(7)+","+skills.getLevelForXp(8)+","+skills.getXp(8)+","+skills.getLevelForXp(9)+","+skills.getXp(9)+","+skills.getLevelForXp(10)+","+skills.getXp(10)+","+skills.getLevelForXp(11)+","+skills.getXp(11)+","+skills.getLevelForXp(12)+","+skills.getXp(12)+","+skills.getLevelForXp(13)+","+skills.getXp(13)+","+skills.getLevelForXp(14)+","+skills.getXp(14)+","+skills.getLevelForXp(15)+","+skills.getXp(15)+","+skills.getLevelForXp(16)+","+skills.getXp(16)+","+skills.getLevelForXp(17)+","+skills.getXp(17)+","+skills.getLevelForXp(18)+","+skills.getXp(18)+","+skills.getLevelForXp(19)+","+skills.getXp(19)+","+skills.getLevelForXp(20)+","+skills.getXp(20)+"," + skills.getLevelForXp(21)+"," + skills.getXp(21) + "," +skills.getLevelForXp(22) + "," + skills.getXp(22) + "," + skills.getLevelForXp(23) + "," + skills.getXp(23)+"," + skills.getLevelForXp(24) + "," + skills.getXp(24)+");");
            updateQuery("INSERT INTO `skillsoverall` (`playerName`,`lvl`,`xp`) VALUES ('"+username+"'," + overall[0] +"," + overall[1] +");");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void restore(Player player) throws SQLException  {
        String username = Misc.formatPlayerNameForDisplay(player.getUsername());
      //  createConnection();
        Statement statement = getConnection().createStatement();
        String query = "SELECT * FROM skills WHERE playerName = '" + username + "'";
        ResultSet results = statement.executeQuery(query);
        if (results.next()) {
            for(int i = 0; i < Skills.SKILL_COUNT; i++) {
                //int level = (int) results.getDouble(Skills.SKILL_NAME[i] +"lvl");
                //int xp = (int) results.getDouble(Skills.SKILL_NAME[i] +"xp");
            }
        }
        closeConnection();
    }

    public int[] getOverall(Player player) {
        int totalLevel = 0;
        int totalXp = 0;
        for(int i = 0; i < 25; i++) {
            totalLevel += player.getSkills().getLevelForXp(i);
        }
        for(int i = 0; i < 25; i++) {
            totalXp += player.getSkills().getXp(i);
        }
        return new int[] {totalLevel, totalXp};
    }
} 
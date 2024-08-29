package org.dementhium.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dementhium.content.areas.impl.CircularArea;
import org.dementhium.content.areas.impl.IrregularArea;
import org.dementhium.content.areas.impl.RectangularArea;
import org.dementhium.content.skills.magic.Spell;
import org.dementhium.identifiers.Identifier;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.definition.PriceDefinition;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.definition.WeaponInterface.WeaponButton;

import com.thoughtworks.xstream.XStream;
/**
 * 
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
@SuppressWarnings("unchecked")
public final class XMLHandler {

	private XMLHandler() { }

	private static XStream xmlHandler;

	static {
		xmlHandler = new XStream();
		xmlHandler.alias("spell", Spell.class);
		xmlHandler.alias("item", Item.class);
		xmlHandler.alias("rectangle", RectangularArea.class);
		xmlHandler.alias("circle", CircularArea.class);
		xmlHandler.alias("irregular", IrregularArea.class);
		xmlHandler.alias("position", Location.class);
		xmlHandler.alias("identifier", Identifier.class);
		xmlHandler.alias("ban", String.class);
		xmlHandler.alias("npcDefinition", NPCDefinition.class);
		xmlHandler.alias("weaponInterface", WeaponInterface.class);
		xmlHandler.alias("weaponButton", WeaponButton.class);
		xmlHandler.alias("price", PriceDefinition.class);
	}

	public static void toXML(String file, Object object) throws IOException {
		OutputStream out = new FileOutputStream(file);
		try {
			xmlHandler.toXML(object, out);
			out.flush();
		} finally {
			out.close();
		}
	}

	public static <T> T fromXML(String file) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
			return (T) xmlHandler.fromXML(in);
		} finally {
			in.close();
		}
	}

}

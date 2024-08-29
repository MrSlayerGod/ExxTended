package org.dementhium.model.map;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.dementhium.cache.Cache;
import org.dementhium.cache.CacheFile;
import org.dementhium.util.BufferUtils;

/**
 * @author  'Mystic Flow
 */
public class GameObjectDefinition {

	private static Map<Integer, GameObjectDefinition> definitions = new HashMap<Integer, GameObjectDefinition>();

	private static CacheFile currentFile = null;

	private int id;
	private boolean clippingFlag;
	private int actionCount;
	private String name;
	private boolean walkable;

	private String[] actions = new String[5];

	private int sizeY = 1;
	private int sizeX = 1;

	public static GameObjectDefinition forId(int id) {
		GameObjectDefinition def = definitions.get(id);
		if(def != null) {
			return def;
		}
		def = new GameObjectDefinition();
		def.id = id;
		try {
			if (currentFile == null || currentFile.getFileId() != (id >>> 8)) {
				currentFile = Cache.INSTANCE.getFile(16, id >>> 8);
				if(currentFile == null) {
					return null;
				}
				currentFile.decompress();
			}
			byte[] data = currentFile.getSubFiles()[id & 0xFF];
			load(def, ByteBuffer.wrap(data));
		} catch (Exception e) {
			return null;
		}
		if(def.name != null) {
			if (def.clippingFlag && def.name.contains("booth")) {
				def.clippingFlag = false;
			}
		}
		if (def.clippingFlag) {
			def.actionCount = 0;
			def.walkable = false;
		}
		definitions.put(id, def);
		return def;
	}

	public int getId() {
		return id;
	}

	public boolean isClippingFlag() {
		return clippingFlag;
	}

	public int getActionCount() {
		return actionCount;
	}

	public String getName() {
		return name;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public String[] getActions() {
		return actions;
	}

	public int getSizeY() {
		return sizeY;
	}

	public int getSizeX() {
		return sizeX;
	}

	private static void load(GameObjectDefinition def, ByteBuffer buffer) {
		int opcode;
		while ((opcode = (buffer.get() & 0xff)) != 0) {
			parseOpcode(def, opcode, buffer);
		}
	}

	private static void parseOpcode(GameObjectDefinition def, int opcode, ByteBuffer buffer) {
		if (opcode != 1 && opcode != 5) {
			if (opcode == 2)
				def.name = BufferUtils.readRS2String(buffer);
			else if (opcode != 14) {
				if (opcode != 15) {
					if (opcode != 17) {
						if (opcode == 18)
							def.walkable = false;
						else if (opcode != 19) {
							if ((opcode ^ 0xffffffff) != -25) {
								if ((opcode ^ 0xffffffff) != -28) {
									if ((opcode ^ 0xffffffff) != -29) {
										if ((opcode ^ 0xffffffff) == -30)
											buffer.get();
										else if ((opcode ^ 0xffffffff) != -40) {
											if ((opcode ^ 0xffffffff) <= -31 && opcode < 35)
												def.actions[-30 + opcode] = (BufferUtils.readRS2String(buffer));
											else if (opcode != 40) {
												if ((opcode ^ 0xffffffff) == -42) {
													int i_51_ = (buffer.get() & 0xff);
													for (int i_52_ = 0; ((i_52_ ^ 0xffffffff) > (i_51_ ^ 0xffffffff)); i_52_++) {
														buffer.getShort();
														buffer.getShort();
													}
												} else if ((opcode ^ 0xffffffff) == -43) {
													int i_53_ = (buffer.get() & 0xff);
													for (int i_54_ = 0; ((i_53_ ^ 0xffffffff) < (i_54_ ^ 0xffffffff)); i_54_++)
														buffer.get();
												} else if (opcode != 62) {
													if ((opcode ^ 0xffffffff) == -65) {
														//something = false
													}
													if (opcode != 65) {
														if (opcode != 66) {
															if ((opcode ^ 0xffffffff) != -68) {
																if ((opcode ^ 0xffffffff) != -70) {
																	if (opcode == 70)
																		buffer.getShort();
																	else if (opcode == 71)
																		buffer.getShort();
																	else if ((opcode ^ 0xffffffff) == -73)
																		buffer.getShort();
																	else if ((opcode ^ 0xffffffff) != -75) {
																		if (opcode == 75)
																			buffer.get();
																		else if ((opcode ^ 0xffffffff) == -78 || opcode == 92) {
																			buffer.getShort();
																			buffer.getShort();
																			if ((opcode ^ 0xffffffff) == -93) {
																				buffer.getShort();
																			}
																			int i_56_ = (buffer.get() & 0xff);
																			for (int i_57_ = 0; (i_56_ ^ 0xffffffff) <= (i_57_ ^ 0xffffffff); i_57_++) {
																				buffer.getShort();
																			}
																		} else if ((opcode ^ 0xffffffff) == -79) {
																			buffer.getShort();
																			buffer.get();
																		} else if (opcode != 79) {
																			if ((opcode ^ 0xffffffff) == -82) {
																				buffer.get();
																			} else if ((opcode ^ 0xffffffff) != -89) {
																				if (opcode != 91) {
																					if ((opcode ^ 0xffffffff) != -94) {
																						if (opcode == 95) {
																							buffer.getShort();
																						} else if (opcode != 97) {
																							if (opcode != 98) {
																								if ((opcode ^ 0xffffffff) != -100) {
																									if ((opcode ^ 0xffffffff) != -101) {
																										if (opcode != 101) {
																											if ((opcode ^ 0xffffffff) != -103) {
																												if (opcode != 103) {
																													if (opcode == 104)
																														buffer.get();
																													else if ((opcode ^ 0xffffffff) == -107) {
																														int i_58_ = (buffer.get() & 0xff);
																														for (int i_59_ = 0; (i_58_ ^ 0xffffffff) < (i_59_ ^ 0xffffffff); i_59_++) {
																															buffer.getShort();
																															buffer.get();
																														}
																													} else if ((opcode ^ 0xffffffff) == -108)
																														buffer.getShort();
																													else if (opcode >= 150 && (opcode ^ 0xffffffff) > -156) {
																														def.actions[opcode + -150] = BufferUtils.readRS2String(buffer);
																													} else if (opcode != 160) {
																														if ((opcode ^ 0xffffffff) == -163) {
																															buffer.getInt();
																														} else if (opcode == 163) {
																															buffer.get();
																															buffer.get();
																															buffer.get();
																															buffer.get();
																														} else if (opcode != 164) {
																															if ((opcode ^ 0xffffffff) == -166)
																																buffer.getShort();
																															else if (opcode != 166) {
																																if ((opcode ^ 0xffffffff) == -168)
																																	buffer.getShort();
																																else if ((opcode ^ 0xffffffff) == -171)
																																	BufferUtils.readSmart(buffer);
																																else if (opcode != 171) {
																																	if (opcode == 173) {
																																		buffer.getShort();
																																		buffer.getShort();
																																	} else if (opcode == 249) {
																																		int i_61_ = (buffer.get() & 0xff);
																																		for (int i_63_ = 0; (i_61_ ^ 0xffffffff) < (i_63_ ^ 0xffffffff); i_63_++) {
																																			boolean bool = (buffer.get() & 0xff) == 1;
																																			@SuppressWarnings("unused") int i_64_ = (buffer.get() & 0xff) << 24 | (buffer.get() & 0xff) << 16 | (buffer.get() & 0xff);
																																			if (!bool)
																																				buffer.getInt();
																																			else
																																				BufferUtils.readRS2String(buffer);
																																		}
																																	}
																																} else
																																	BufferUtils.readSmart(buffer);
																															} else
																																buffer.getShort();
																														} else
																															buffer.getShort();
																													} else {
																														int i_65_ = (buffer.get() & 0xff);
																														for (int i_66_ = 0; (i_65_ ^ 0xffffffff) < (i_66_ ^ 0xffffffff); i_66_++)
																															buffer.getShort();
																													}
																												}
																											} else
																												buffer.getShort();
																										} else
																											buffer.get();
																									} else {
																										buffer.get();
																										buffer.getShort();
																									}
																								} else {
																									buffer.get();
																									buffer.getShort();
																								}
																							}
																						}
																					} else {
																						buffer.getShort();
																					}
																				}
																			}
																		} else {
																			buffer.getShort();
																			buffer.getShort();
																			buffer.get();
																			int i_67_ = (buffer.get() & 0xff);
																			for (int i_68_ = 0; (i_67_ ^ 0xffffffff) < (i_68_ ^ 0xffffffff); i_68_++)
																				buffer.getShort();
																		}
																	} else {
																		def.clippingFlag = true;
																	}
																} else {
																	//walkToData
																}
															} else
																buffer.getShort();
														} else
															buffer.getShort();
													} else
														buffer.getShort();
												}
											} else {
												int i_69_ = (buffer.get() & 0xff);
												for (int i_70_ = 0; ((i_70_ ^ 0xffffffff) > (i_69_ ^ 0xffffffff)); i_70_++) {
													buffer.getShort();
													buffer.getShort();
												}
											}
										} else
											buffer.get();
									} else
										buffer.get();
								} else {
									def.actionCount = 1;
								}
							} else {
								buffer.getShort();
							}
						} else
							buffer.get();
					} else {
						def.actionCount = 0;
						def.walkable = false;
					}
				} else
					def.sizeY = (buffer.get() & 0xff);
			} else
				def.sizeX = (buffer.get() & 0xff);
		} else {
			boolean aBoolean1492 = false;
			if (opcode == 5 && aBoolean1492)
				readSkip(buffer);
			int i_71_ = (buffer.get() & 0xff);
			for (int i_72_ = 0; i_71_ > i_72_; i_72_++) {
				buffer.get();
				int i_73_ = (buffer.get() & 0xff);
				for (int i_74_ = 0; (i_74_ ^ 0xffffffff) > (i_73_ ^ 0xffffffff); i_74_++)
					buffer.getShort();
			}
			if (opcode == 5 && !aBoolean1492)
				readSkip(buffer);
		}
	}

	private static void readSkip(ByteBuffer buffer) {
		int length = buffer.get() & 0xff;
		for (int index = 0; index < length; index++) {
			buffer.position(buffer.position() + 1);
			buffer.position(buffer.position() + ((buffer.get() & 0xff) * 2));
		}
	}

	public boolean hasActions() {
		if(actions == null)
			return false;
		for(int i = 0; i < actions.length; i++) {
			if(actions[i] != null && !actions[i].equals("null") && !actions[i].equals(""))
				return true;
		}
		return false;
	}

	public int getNumberOfActions() {
		if(actions == null)
			return 0;
		int amt = 0;
		for(int i = 0; i < actions.length; i++) {
			if(actions[i] != null && !actions[i].equals("null") && !actions[i].equals(""))
				amt++;
		}
		return amt;
	}
}

package de.spurtikus.vxi.mainframes.hp1300b;

/**
 * Class containing all attributes of a VXI device as returned by HP75000
 * VXI:CONF:DLIS? .
 * 
 * @author dennis
 *
 */
public class VXIDevice {
	
	public static final String MAINFRAME_ID = "E1300";

	public static final String MUXRELAY_ID  = "E1345";

	public static final String MUXFET_ID  = "E1351";

	public static final String VOLTMETER_ID  = "E1326";

	public static final String DIGIO_1330_ID  = "E1330B";

	public static final String COUNTER_1333_ID  = "E1333A";

	public static final String AFG_ID  = "E1340A";

	public enum DeviceClass {
		EXT("EXT"), HYB("HYB"), MEM("MEM"), MSG("MSG"), REG("REG"), VME("VME");

		private final String dc;

		private DeviceClass(final String dc) {
			this.dc = dc;
		}

		@Override
		public String toString() {
			return dc;
		}
	}

	public enum MemorySpace {
		A16("A16"), A24("A24"), A32("A32"), NONE("NONE"), RES("RES");

		private final String ms;

		private MemorySpace(final String ms) {
			this.ms = ms;
		}

		@Override
		public String toString() {
			return ms;
		}
	}

	/** 0.. 255 */
	private int address;
	/** -1..255 */
	private int commanderAdress;
	/** 0..4095 */
	private int manufacturer;
	/** 0..65535 */
	private int model;
	/** -1.. num slots */
	private int slot;
	/** 0..255 */
	private int slot0Adress;
	private DeviceClass devClass;
	private MemorySpace memSpace;
	private String memOffset;
	private String memSize;
	private String state;
	private String s1, s2, s3;
	private String comment;
	private int gpibSecondary;
	private String id;
	private String name;

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public int getCommanderAdress() {
		return commanderAdress;
	}

	public void setCommanderAdress(int commanderAdress) {
		this.commanderAdress = commanderAdress;
	}

	public int getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(int manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getSlot0Adress() {
		return slot0Adress;
	}

	public void setSlot0Adress(int slot0Adress) {
		this.slot0Adress = slot0Adress;
	}

	public DeviceClass getDevClass() {
		return devClass;
	}

	public void setDevClass(DeviceClass devClass) {
		this.devClass = devClass;
	}

	public MemorySpace getMemSpace() {
		return memSpace;
	}

	public void setMemSpace(MemorySpace memSpace) {
		this.memSpace = memSpace;
	}

	public String getMemOffset() {
		return memOffset;
	}

	public void setMemOffset(String memOffset) {
		this.memOffset = memOffset;
	}

	public String getMemSize() {
		return memSize;
	}

	public void setMemSize(String memSize) {
		this.memSize = memSize;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getS1() {
		return s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}

	public String getS3() {
		return s3;
	}

	public void setS3(String s3) {
		this.s3 = s3;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return id + " {" + address + "," + commanderAdress + "," + manufacturer + "," + model + "," + slot0Adress + ","
				+ devClass + "," + memSpace + "," + memOffset + "," + memSize + "," + state + "," + s1 + "," + s2 + ","
				+ s3 + "," + "," + comment + "," + gpibSecondary + "}";
	}

	public static VXIDevice materializeFrom(String s) {
		VXIDevice d = new VXIDevice();
		String l[] = s.split(",");
		// +72,+0,+4095,+65296,-1,+0,REG,A16
		// ,#H00000000,#H00000000,READY,"","","","SWITCH INSTALLED AT SECONDARY
		// ADDR 9"
		d.setAddress(Integer.parseInt(l[0]));
		d.setCommanderAdress(Integer.parseInt(l[1]));
		d.setManufacturer(Integer.parseInt(l[2]));
		d.setModel(Integer.parseInt(l[3]));
		d.setSlot(Integer.parseInt(l[4]));
		d.setSlot0Adress(Integer.parseInt(l[5]));
		d.setDevClass(DeviceClass.valueOf(l[6]));
		d.setMemSpace(MemorySpace.valueOf(l[7].trim())); // up to 4 chars
		d.setMemOffset(l[8]);
		d.setMemSize(l[9]);
		d.setState(l[10]);
		d.setS1(l[11]);
		d.setS2(l[12]);
		d.setS3(l[13]);
		d.setComment(l[14]);

		// calculated fields
		d.setGPIBSecondary(d.getAddress() / 8);
		d.setId(findIdByModel(d.getModel()));
		d.setName(findNameByModel(d.getModel()));
		return d;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns an id string depending on model value. Model value is raw value
	 * read in via GPIB from mainframe.
	 * 
	 * @param model
	 *            model value from GPIB data
	 * @return useful string
	 */
	private static String findIdByModel(int model) {
		String idFound;
		switch (model) {
		case 1300:
		case 1301:
			// E1300 and E1301 have same id, only name is different
			idFound = MAINFRAME_ID;
			break;
		case 65280:
			idFound = MUXRELAY_ID;
			break;
		case 65296:
			idFound = MUXFET_ID;
			break;
		case 65300:
			idFound = MUXFET_ID;
			break;
		case 65344:
			idFound = VOLTMETER_ID;
			break;
		case 65361:
			idFound = DIGIO_1330_ID;
			break;
		case 65380:
			idFound = COUNTER_1333_ID;
			break;
		case 65440:
			idFound = AFG_ID;
			break;
		default:
			idFound = "unknown";
			break;
		}
		return idFound;
	}

	/**
	 * Returns a name string depending on model value. Model value is raw value
	 * read in via GPIB from mainframe.
	 * 
	 * @param model
	 *            model value from GPIB data
	 * @return useful string
	 */
	private static String findNameByModel(int model) {
		String nameFound;
		switch (model) {
		case 1300:
			nameFound = "E1300 Mainframe";
			break;
		case 1301:
			nameFound = "E1301 Mainframe";
			break;
		case 65280:
			nameFound = "E1345 MUX Relay";
			break;
		case 65296:
			nameFound = "E1351 MUX FET (a)";
			break;
		case 65300:
			nameFound = "E1351 MUX FET (b)";
			break;
		case 65344:
			nameFound = "E1326 VM";
			break;
		case 65361:
			nameFound = "E1330B DIGIO";
			break;
		case 65380:
			nameFound = "E1333A Counter";
			break;
		case 65440:
			nameFound = "E1340A ARB";
			break;
		default:
			nameFound = "unknown";
			break;
		}
		return nameFound;
	}

	public int getGPIBSecondary() {
		return gpibSecondary;
	}

	private void setGPIBSecondary(int i) {
		gpibSecondary = i;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

};

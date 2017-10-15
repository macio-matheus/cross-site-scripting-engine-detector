package utilsLib.util.data;

public enum DataType {
	NUMBER, BOOL, STRING, DATE_TIME, DATE, TIME, UNDEFINED_TYPE;
	
	public static DataType getByIndex(int i) {
		DataType[] ds = DataType.values();
		
		if (i > 0 && i < ds.length) {
			return ds[i];
		}
		
		return DataType.UNDEFINED_TYPE;
	}
}
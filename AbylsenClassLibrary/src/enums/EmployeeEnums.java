package enums;


public class EmployeeEnums {
	public static final String TYPE_CONSULTANT = "Consultant";
	public static final String TYPE_MANAGER = "Manager";
	public static final String[] POSTE_ARRAY =  { TYPE_CONSULTANT, TYPE_MANAGER };
	
	public static boolean isTypeExists(String type) {
		return type.equals(TYPE_CONSULTANT) || 
			   type.equals(TYPE_MANAGER);
	}
	
	public static boolean haveRights(String targetType, String currentType) {
		return getTypePriority(targetType) <= getTypePriority(currentType);
	}
	
	public static int getTypePriority(String type) {
		switch(type) {
		case TYPE_MANAGER:
			return 0;
		case TYPE_CONSULTANT:
			return 1;
		default:
			return 2;
		}
	}
}

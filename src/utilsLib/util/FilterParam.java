package utilsLib.util;

public class FilterParam {
	public enum COMPARE_TYPE {
		BINARY_EQUAL, EQUAL, DIFF, LIKE, GREATER, LOWER, GREATER_OR_EQUAL, LOWER_OR_EQUAL, FIND_IN_SET, IS_NULL, IS_NOT_NULL, MATCH_AGAINST_IN_BOOL
	};

	public enum OPERATOR {
		OR, AND
	};

	private String name;
	private Object filter;
	private COMPARE_TYPE compareType;
	private boolean not;
	private OPERATOR operator = OPERATOR.AND;

	public FilterParam(String name, Object filter, boolean not,
			COMPARE_TYPE compareType) {
		this(name, filter, compareType);
		setNot(not);
	}

	public FilterParam(String name, Object filter, boolean not,
			OPERATOR operator, COMPARE_TYPE compareType) {
		this(name, filter, operator, compareType);
		setNot(not);
	}

	public FilterParam(String name, Object filter, OPERATOR operator,
			COMPARE_TYPE compareType) {
		this(name, filter, compareType);
		setOperator(operator);
	}

	public FilterParam(String name, Object filter, COMPARE_TYPE compareType) {
		setName(name);
		setFilter(filter);
		setCompareType(compareType);
	}
	
	public FilterParam(String name, Object filter) {
		setName(name);
		setFilter(filter);
		setCompareType(FilterParam.COMPARE_TYPE.EQUAL);
	}

	public COMPARE_TYPE getCompareType() {
		return compareType;
	}

	public String getName() {
		return name;
	}

	public Object getFilter() {
		return filter;
	}

	public boolean isNot() {
		return not;
	}

	public OPERATOR getOperator() {
		return operator;
	}

	public void setCompareType(COMPARE_TYPE compareType) {
		this.compareType = compareType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFilter(Object filter) {
		if (filter instanceof int[]) {
			int[] filterInt = (int[]) filter;

			Integer[] filter2 = new Integer[filterInt.length];
			for (int i = 0; i < filter2.length; i++) {
				filter2[i] = filterInt[i];
			}
			
			filter = filter2;
		}

		this.filter = filter;
	}

	public void setNot(boolean not) {
		this.not = not;
	}

	public void setOperator(OPERATOR operator) {
		this.operator = operator;
	}
}

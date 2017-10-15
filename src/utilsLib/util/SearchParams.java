package utilsLib.util;

import java.util.*;

import utilsLib.util.data.*;



public abstract class SearchParams implements Comparable {
	public final static int GROUP_POINT_OPEN_AND = 0;
	public final static int GROUP_POINT_OPEN_OR = 1;
	public final static int GROUP_POINT_CLOSE = 2;

	private String fieldPrefix = "";

	private ObjFactory objFactory;

	private int classID;

	private boolean caching = true; // isso aqui é implementado ou não, opcional

	private ArrayList<FilterParam> filtering;

	class GroupPoint {
		int position;
		int what;

		public GroupPoint(int position, int what) {
			this.position = position;
			this.what = what;
		}
	}

	private ArrayList<GroupPoint> groupPoints = new ArrayList<GroupPoint>(); // Posição
	// (int)
	// x O
	// que

	// (int).

	private ArrayList<OrderParam> ordering;

	private ArrayList<ReturnedFieldParam> returnFields;

	// private ArrayList<FieldModifier> fieldsModifiers;

	private PagingInfo pi;

	public SearchParams(ObjFactory objFactory, int classID) {
		setObjFactory(objFactory);
		setClassID(classID);
	}

	public GroupPoint[] getGroups() {
		if (groupPoints == null) {
			return null;
		}

		GroupPoint[] gps = (GroupPoint[]) groupPoints
				.toArray(new GroupPoint[groupPoints.size()]);

		Comparator<GroupPoint> comparatorTag = new Comparator<GroupPoint>() {
			public int compare(GroupPoint o1, GroupPoint o2) {
				return o1.position - o2.position;
			}
		};

		Arrays.sort(gps, comparatorTag);

		return gps;
	}

	/**
	 * Abre de qualquer jeito (and), qd precisa.
	 */
	public void openGroup() {
		this.openGroup(GROUP_POINT_OPEN_AND);
	}

	/**
	 * @param openType
	 *            GROUP_POINT_OPEN_AND ou GROUP_POINT_OPEN_OR
	 */
	public void openGroup(int openType) {
		if (groupPoints == null) {
			groupPoints = new ArrayList<GroupPoint>();
		}

		int position = filtering != null ? filtering.size() : 0;

		groupPoints.add(new GroupPoint(position, openType));
	}

	public void closeGroup() {
		if (groupPoints == null) {
			groupPoints = new ArrayList<GroupPoint>();
		}

		groupPoints.add(new GroupPoint(filtering.size(), GROUP_POINT_CLOSE));
	}

	public void addFilter(FilterParam fp) {
		if (filtering == null) {
			filtering = new ArrayList<FilterParam>();
		}

		filtering.add(fp);
	}

	public void addReturnField(ReturnedFieldParam rfp) {
		if (returnFields == null) {
			returnFields = new ArrayList<ReturnedFieldParam>();
		}

		returnFields.add(rfp);
	}

	public void addOrder(OrderParam fp) {
		if (ordering == null) {
			ordering = new ArrayList<OrderParam>();
		}

		ordering.add(fp);
	}

	/*
	 * public void addFieldModifier(FieldModifier fm) { if (fieldsModifiers ==
	 * null) { fieldsModifiers = new ArrayList<FieldModifier>(); }
	 * 
	 * fieldsModifiers.add(fm); }
	 */

	/**
	 * Um jeitinho é mudar o filter na sub-class e estender esse método.
	 */
	public String getFilter() {
		String sql = "";

		if (filtering != null) {
			DbConsultManager consult = new DbConsultManager();

			GroupPoint[] groups = this.getGroups();
			int currGroup = -1;

			if (groups != null && groups.length > 0) {
				currGroup = 0;
			}

			for (int i = 0; i < filtering.size(); i++) {
				FilterParam fp = (FilterParam) filtering.get(i);

				// 2012-06-28: passava, dava continue, mas não é isso, é para
				// retornar a consulta em branco, tá mais e4mbaixo.
				// // se for array e for em branco, já passa direto.
				// if (fp.getFilter() instanceof Object[]
				// && ((Object[]) fp.getFilter()).length == 0) {
				// continue;
				// }

				// 1. Operador.
				String operator = null;

				if (fp.getCompareType() == FilterParam.COMPARE_TYPE.EQUAL) {
					operator = "[#name] = [#value]";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.BINARY_EQUAL) {
					operator = "binary [#name] = [#value]";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.DIFF) {
					operator = "[#name] <> [#value]";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.LIKE) {
					operator = "[#name] like [#value]";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.GREATER) {
					operator = "[#name] > [#value]";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.LOWER) {
					operator = "[#name] < [#value]";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.GREATER_OR_EQUAL) {
					operator = "[#name] >= [#value]";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.FIND_IN_SET) {
					operator = " find_in_set([#value], [#name]) ";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.MATCH_AGAINST_IN_BOOL) {
					operator = " match([#name]) against ([#value] in boolean mode) ";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.LOWER_OR_EQUAL) {
					operator = "[#name] <= [#value]";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.IS_NULL) {
					operator = "[#name] is null ";
				} else if (fp.getCompareType() == FilterParam.COMPARE_TYPE.IS_NOT_NULL) {
					operator = "[#name] is not null ";
				}

				// 4. Agrpupamentos
				boolean openAgrouped = false; // se abriu agrupado, ignora
				// conectivo inicial. Caso
				// contrario, tem que
				// considerar.
				while (currGroup != -1 && currGroup < groups.length) {
					if (i == groups[currGroup].position) {
						if (groups[currGroup].what == GROUP_POINT_OPEN_AND) {
							if (!sql.equals("")) {
								sql += " AND (";
							} else {
								sql += "(";
							}

							openAgrouped = true;
						} else if (groups[currGroup].what == GROUP_POINT_OPEN_OR) {
							if (!sql.equals("")) {
								sql += " OR (";
							} else {
								sql += "(";
							}

							openAgrouped = true;
						} else if (groups[currGroup].what == GROUP_POINT_CLOSE) {
							sql += ")";
						}

						currGroup++;
					} else {
						break;
					}
				}

				// 2. conectivo.
				String connective = "";

				if (!openAgrouped) {
					if (fp.getOperator() == FilterParam.OPERATOR.AND) {
						connective = " AND ";
					} else {
						connective = " OR ";
					}

					if (!sql.equals("")) {
						sql += connective;
					}
				}

				// 3. Not
				String preClauseNot = "";
				String posClauseNot = "";

				if (fp.isNot()) {
					preClauseNot = " not (";
					posClauseNot = ")";
				}

				// 5. Montagem propriamente diga.
				Field field = this.getField(fp.getName());

				if (field == null) {
					throw new RuntimeException(
							"Campo não definido na fábrica: " + fp.getName());
				}

				if (fp.getFilter() instanceof Object[]) {
					Object[] objs = (Object[]) fp.getFilter();

					String justOne = "";

					for (int j = 0; j < objs.length; j++) {
						field.setValue(objs[j]);

						// de repente, pau, o que está é que dá certo.
						// if (!(fp.getCompareType() ==
						// FilterParam.COMPARE_TYPE.EQUAL && fp
						// .getOperator() == FilterParam.OPERATOR.AND)) {

						if (fp.getCompareType() != FilterParam.COMPARE_TYPE.EQUAL
								|| fp.getOperator() != FilterParam.OPERATOR.OR) {

							if (j != 0) {
								sql += connective;
							}

							sql += preClauseNot
									+ mountClause(operator, this
											.getFieldPrefix()
											+ "." + field.getName(), consult
											.formatStringByType(field
													.getValue(), field
													.getType())) + posClauseNot;
						} else {
							if (!justOne.equals("")) {
								justOne += ",";
							}

							justOne += consult.formatStringByType(field
									.getValue(), field.getType());
						}
					}

					if (!justOne.equals("")) {

						sql += preClauseNot + this.getFieldPrefix() + "."
								+ field.getName() + " in (" + justOne + ")"
								+ posClauseNot;
					} else {
						// 2012-06-28 se for array e for em branco, daí é como
						// se o array tivesse em vazio
						// e dá pau
						if (fp.getFilter() instanceof Object[]
								&& ((Object[]) fp.getFilter()).length == 0) {
							sql += "false";
						}
					}
				} else {
					if (!(fp.getFilter() instanceof FilterField)) {
						field.setValue(fp.getFilter());

						String fieldValue = "";

						if (fp.getCompareType() != FilterParam.COMPARE_TYPE.IS_NULL
								&& fp.getCompareType() != FilterParam.COMPARE_TYPE.IS_NOT_NULL) {
							fieldValue = consult.formatStringByType(field
									.getValue(), field.getType());
						}

						sql += preClauseNot
								+ mountClause(operator, this.getFieldPrefix()
										+ "." + field.getName(), fieldValue)
								+ posClauseNot;
					} else {
						FilterField ff = (FilterField) fp.getFilter();

						Field filterField = this.getField(ff.getRelatedName());
						String expression = this.getFieldPrefix() + "."
								+ filterField.getName();

						String addSql = mountClause(operator, this
								.getFieldPrefix()
								+ "." + field.getName(), expression);

						sql += preClauseNot + addSql + posClauseNot;
					}
				}

			}

			// 6. Fechamentos que ficaram em aberto.
			if (currGroup != -1 && currGroup < groups.length) {
				for (; currGroup < groups.length; currGroup++) {
					if (groups[currGroup].what == GROUP_POINT_OPEN_AND) {
						if (!sql.equals("")) {
							sql += " AND (";
						} else {
							sql += " (";
						}
					} else if (groups[currGroup].what == GROUP_POINT_OPEN_OR) {
						if (!sql.equals("")) {
							sql += " OR (";
						} else {
							sql += " (";
						}
					} else if (groups[currGroup].what == GROUP_POINT_CLOSE) {
						sql += ")";
					}
				}

			}
		}

		return sql;
	}

	/**
	 * preserverOrderFieldName mantém a ordem do array passado no filter. Ou
	 * seja, o campo vai servir para filtro e para odenação.
	 */
	public String getOrder() {
		String sql = "";

		if (ordering != null) {
			// bota as outras ordenações
			for (int i = 0; ordering != null && i < ordering.size(); i++) {
				OrderParam op = (OrderParam) ordering.get(i);

				if (!sql.equals("")) {
					sql += ",";
				}

				Field field = (Field) objFactory.getClassEntity(classID)
						.getFieldByReal(op.getName());

				if (field == null) {
					throw new IllegalArgumentException(
							"Param: name. Nome de filtro incorreto: "
									+ op.getName()
									+ " Verifique se o nome da propriedade é esta mesma.");
				}

				DbConsultManager consult = new DbConsultManager();

				// De acordo com o tipo de ordenação, monta a cláusula
				if (op.getType() == OrderParam.ORDERING_TYPE.NORMAL) {
					sql += " " + fieldPrefix + "." + field.getName();
				} else if (op.getType() == OrderParam.ORDERING_TYPE.EQUAL) {
					sql += fieldPrefix
							+ "."
							+ field.getName()
							+ " = "
							+ consult.formatStringByType(op.getValue() + "",
									field.getType());
				} else if (op.getType() == OrderParam.ORDERING_TYPE.PRESERVE_ORDER) {
					if (!(op.getValue() instanceof Integer[])) {
						throw new IllegalArgumentException(
								"!filterFp.getFilter() instanceof Integer[]");
					}

					if (((Integer[]) op.getValue()).length != 0) { // 2012-06-29:
																	// se for
																	// array em
																	// branco,
																	// não
																	// ordena
						sql = "field(" + fieldPrefix + "." + field.getName()
								+ ","
								+ Utils.toStr((Integer[]) op.getValue(), ",")
								+ ")";
					}
				} else if (op.getType() == OrderParam.ORDERING_TYPE.FIND_IN_SET) {
					String orderClause = " find_in_set([#value], [#name]) ";

					String fieldValue = consult.formatStringByType(op
							.getValue()
							+ "", field.getType());

					sql += mountClause(orderClause, field.getName(), fieldValue);
				} else if (op.getType() == OrderParam.ORDERING_TYPE.MATCH_AGAINST_IN_BOOL) {
					String orderClause = " match([#name]) against ([#value] in boolean mode) ";

					String fieldValue = consult.formatStringByType(op
							.getValue()
							+ "", field.getType());

					sql += mountClause(orderClause, field.getName(), fieldValue);
				}

				if (op.isInverse()) {
					sql += " DESC";
				}
			}
		}

		return sql;
	}

	public String getReturnFields() {
		String sql = "";

		if (returnFields != null && returnFields.size() > 0) {
			for (int i = 0; i < returnFields.size(); i++) {
				ReturnedFieldParam rfp = (ReturnedFieldParam) returnFields
						.get(i);

				if (!sql.equals("")) {
					sql += ",";
				}

				Field field = (Field) objFactory.getClassEntity(classID)
						.getFieldByReal(rfp.getName());

				sql += " " + fieldPrefix + "." + field.getName();
			}
		} else {
			sql = "*";
		}

		return sql;
	}

	public String getFieldPrefix() {
		return fieldPrefix;
	}

	public PagingInfo getPi() {
		return pi;
	}

	public ObjFactory getObjFactory() {
		return objFactory;
	}

	public int getClassID() {
		return classID;
	}

	public void setFieldPrefix(String fieldPrefix) {
		this.fieldPrefix = fieldPrefix;
	}

	public void setPi(PagingInfo pi) {
		this.pi = pi;
	}

	public void setObjFactory(ObjFactory objFactory) {
		this.objFactory = objFactory;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	/**
	 * Retorna o campo. Caso se queira implementar campos extras que nao
	 * existam, só sobrescrever este método.
	 * 
	 * @param realName
	 *            String
	 * @return Field
	 */
	public Field getField(String realName) {
		Field f = (Field) objFactory.getClassEntity(classID).getFieldByReal(
				realName);

		if (f != null) {
			return (Field) f.clone();
		} else {
			return null;
		}
	}

	public static String mountClause(String operator, String name, String value) {
		return Utils.replace(Utils.replace(operator, "[#name]", name),
				"[#value]", value);
	}

	public void setFilterParams(FilterParam[] filterParams) {
		if (filterParams == null) {
			filtering = null;
		} else {
			filtering = new ArrayList<FilterParam>();

			for (int i = 0; i < filterParams.length; i++) {
				filtering.add(filterParams[i]);
			}
		}
	}

	public void setOrderParams(OrderParam[] orderParams) {
		if (orderParams == null) {
			ordering = null;
		} else {
			ordering = new ArrayList<OrderParam>();

			for (int i = 0; i < orderParams.length; i++) {
				ordering.add(orderParams[i]);
			}
		}
	}

	public String getCountingSql() {
		String counting = "select count(*) from "
				+ objFactory.getClassEntity(classID).getName() + " "
				+ fieldPrefix;

		String where = this.getFilter();

		if (!where.equals("")) {
			counting += " where " + where;
		}

		return counting;
	}

	/**
	 * Procura FilterParam pelo nome do campo.
	 * 
	 * @param name
	 * @return
	 */
	public FilterParam searchFilterParam(String name) {
		FilterParam[] fps = this.getFilterParams();

		for (int i = 0; i < fps.length; i++) {
			if (fps[i].getName().equals(name)) {
				return fps[i];
			}
		}

		return null;
	}

	public FilterParam[] getFilterParams() {
		if (filtering != null) {
			return (FilterParam[]) filtering.toArray(new FilterParam[filtering
					.size()]);
		} else {
			return new FilterParam[0];
		}
	}

	public ReturnedFieldParam[] getReturnFieldsParams() {
		if (returnFields != null) {
			return (ReturnedFieldParam[]) returnFields
					.toArray(new ReturnedFieldParam[returnFields.size()]);
		} else {
			return new ReturnedFieldParam[0];
		}
	}

	public OrderParam[] getOrderParams() {
		if (ordering != null) {
			return (OrderParam[]) ordering.toArray(new OrderParam[ordering
					.size()]);
		} else {
			return new OrderParam[0];
		}
	}

	public abstract Object clone();

	public void copyFrom(SearchParams params) {
		this.filtering = params.filtering == null ? null
				: (ArrayList<FilterParam>) params.filtering.clone();
		this.ordering = params.ordering == null ? null
				: (ArrayList<OrderParam>) params.ordering.clone();
		this.returnFields = params.returnFields == null ? null
				: (ArrayList<ReturnedFieldParam>) params.returnFields.clone();
		this.groupPoints = params.groupPoints == null ? null
				: (ArrayList<GroupPoint>) params.groupPoints.clone();
		this.pi = params.pi == null ? null : (PagingInfo) params.pi.clone();
	}

	public String toString() {
		StringBuffer str = new StringBuffer();

		if (ordering != null && ordering.size() > 0) {
			str.append("[#o]_");

			for (OrderParam orderParam : ordering) {
				str.append(orderParam.getName() + "_"
						+ (orderParam.isInverse() ? 1 : 0) + "_");
			}
		}

		if (returnFields != null && returnFields.size() > 0) {
			str.append("_[#r]_");

			for (ReturnedFieldParam returnField : returnFields) {
				str.append(returnField.getName() + "_");
			}
		}

		if (filtering != null && filtering.size() > 0) {
			str.append("_[#f]_");

			for (FilterParam filterParam : filtering) {
				Object filter = filterParam.getFilter();

				String value = "";

				if (filter instanceof Object[]) {
					Object[] filters = (Object[]) filter;

					// 2008-12-23: pode haver buscas com "_" e haver erros. Tô
					// nem
					// aí
					for (int i = 0; i < filters.length; i++) {
						value += filters[i] + "_";
					}
				} else if (filter instanceof FilterField) {
					value = ((FilterField) filter).getRelatedName();
				} else if (filter instanceof Date) {
					Field field = this.getField(filterParam.getName());

					if (field != null) {
						if (field.getType() == DataType.DATE) {
							value = Utils.dateToStr((Date) filterParam
									.getFilter())
									+ "";
						} else if (field.getType() == DataType.DATE_TIME) {
							value = Utils.dateTimeToStr((Date) filterParam
									.getFilter())
									+ "";
						} else {
							value = filterParam.getFilter() + "";
						}
					} else {
						value = filterParam.getFilter() + "";
					}
				} else {
					value = filterParam.getFilter() + "";
				}
				str.append(filterParam.getName() + "_"
						+ filterParam.getCompareType().ordinal() + "_"
						+ filterParam.getOperator().ordinal() + "_" + value
						+ "__");
			}
		}

		if (groupPoints != null && groupPoints.size() > 0) {
			str.append("_[#g]_");

			for (GroupPoint groupPoint : groupPoints) {
				str.append(groupPoint.what + "_" + groupPoint.position + "_");
			}
		}

		if (pi != null) {
			str.append("_[#p]_");
			str.append(pi.getCurrPage() + "_" + pi.getPageSize());
		}

		return str.toString();
	}

	public boolean equals(Object o) {
		return this.toString().equals(((SearchParams) o).toString());
	}

	public int hashCode() {
		return this.toString().hashCode();
	}

	public int compareTo(Object o) {
		return this.toString().compareTo(((SearchParams) o).toString());
	}

	public boolean isCaching() {
		return caching;
	}

	public void setCaching(boolean caching) {
		this.caching = caching;
	}
}
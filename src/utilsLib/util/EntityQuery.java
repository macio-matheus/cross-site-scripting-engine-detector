package utilsLib.util;

import java.util.LinkedList;
import java.util.List;

import utilsLib.util.data.Entity;

public class EntityQuery {
	private int limit;
	private int offset;
	private Entity filter;
	private LinkedList entities;
	private boolean ascending;

	public EntityQuery(int limit, int offset, Entity filter, boolean ascending) {
		setLimit(limit);
		setOffset(offset);
		setFilter(filter);
		setAscending(ascending);
		entities = new LinkedList();
	}

	public void avail(Entity newEntity) {
		if (filter == null) {
			if (!ascending) {
				entities.addFirst(newEntity);
			} else {
				entities.add(newEntity);
			}
		} else if (newEntity.isSimilar(filter)) {
			if (!ascending) {
				entities.addFirst(newEntity);
			} else {
				entities.add(newEntity);
			}
		}

		if (entities.size() > offset + limit) {
			entities.removeLast();
		}
	}

	public Entity[] getEntities() {
		List subLL = null;
		if (offset < entities.size()) {
			subLL = entities.subList(offset, entities.size());
		} else {
			subLL = entities.subList(entities.size(), entities.size());
		}

		return (Entity[]) subLL.toArray(new Entity[subLL.size()]);
	}

	public Entity getFilter() {
		return filter;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setFilter(Entity filter) {
		this.filter = filter;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
}

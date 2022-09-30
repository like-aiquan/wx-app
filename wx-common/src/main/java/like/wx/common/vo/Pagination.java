package like.wx.common.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenaiquan
 */
public class Pagination<T> {
	/**
	 * 最大每页显示数量
	 */
	private static final int MAX_PER_PAGE = 100;
	/**
	 * 每页显示数量
	 */
	protected int perPage = 10;
	/**
	 * 当前页
	 */
	private int currentPage = 1;
	private int offset;

	/**
	 * 总数据量
	 */
	private long totalCount;

	private String order;

	/**
	 * 数据集合
	 */
	private List<T> items = new ArrayList<>();

	public Pagination(Integer currentPage, Integer perPage) {
		if (currentPage != null && currentPage > 0) {
			this.currentPage = currentPage;
		}
		if (perPage != null && perPage > 0 && (perPage <= MAX_PER_PAGE)) {
			this.perPage = perPage;
		}
	}

	/**
	 * 无分页对象
	 */
	public Pagination(boolean noPagination) {
		this.perPage = Integer.MAX_VALUE;
	}

	/**
	 * 完成分页数据
	 */
	public Pagination<T> finish(List<T> items, long totalCount) {
		if (items != null) {
			this.items = items;
		}
		this.totalCount = totalCount;
		return this;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setPerPage(Integer perPage) {
		if (perPage != null && perPage > 0) {
			this.perPage = perPage;
		}
	}

	public int getPerPage() {
		return perPage;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public List<T> getItems() {
		return items;
	}

	public int getOffset() {
		offset = (this.currentPage - 1) * this.perPage;
		return offset;
	}

	public String getOrder() {
		return order;
	}
}

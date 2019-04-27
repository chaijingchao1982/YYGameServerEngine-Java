package com.cjs;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author JasonChan
 * @date 2013-4-30
 * @param <T>
 */
public class PageBean<T extends Serializable> {

	/**
	 * 每一页显示的记录数目,默认为Global的PAGESIZE的值
	 */
	private int pageSize = 10;

	/**
	 * 当前的页号,默认为第一页
	 */
	private int currentPage = 1;

	/**
	 * 记录集总页数
	 */
	private int pageCount = 0;

	/**
	 * 总记录条数
	 */
	private long totalResults;

	/**
	 * 当前页的结果集
	 */
	private List<T> objList;

	public List<T> getObjList() {
		return objList;
	}

	public void setObjList(List<T> objList) {
		this.objList = objList;
	}

	public PageBean(int pageSize) {
		this.pageSize = pageSize;
	}

	public PageBean(int pageSize, int totalResults) {
		this.pageSize = pageSize;
		this.totalResults = totalResults;
	}

	public PageBean(int pageSize, int currentPage, int totalResults) {
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalResults = totalResults;
	}

	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}

	public boolean hasNextPage() {
		return currentPage < this.getTotalPages();
	}

	public boolean hasPreviousPage() {
		return currentPage > 1;
	}

	public int getTotalPages() {
		return (int) Math.ceil(totalResults / (double) pageSize);
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public long getTotalResults() {
		return totalResults;
	}

	public int getStartResult() {
		return (currentPage - 1) * pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
}

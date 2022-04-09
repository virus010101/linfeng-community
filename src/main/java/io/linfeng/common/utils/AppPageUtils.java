
package io.linfeng.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * App端分页工具类
 *
 */
public class AppPageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 总记录数/最后一页
	 */
	private int last_page;
	/**
	 * 每页记录数
	 */
	private int per_page;
	/**
	 * 总页数
	 */
	private int total;
	/**
	 * 当前页数
	 */
	private int current_page;
	/**
	 * 列表数据
	 */
	private List<?> data;

	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param currPage    当前页数
	 */
	public AppPageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
		this.data = list;
		this.last_page = totalCount;
		this.per_page = pageSize;
		this.current_page = currPage;
		this.total = (int)Math.ceil((double)totalCount/pageSize);
	}

	/**
	 * 分页
	 */
	public AppPageUtils(IPage<?> page) {
		this.data = page.getRecords();
		this.last_page = (int)page.getTotal();
		this.per_page = (int)page.getSize();
		this.current_page = (int)page.getCurrent();
		this.total = (int)page.getPages();
	}


	public int getLast_page() {
		return last_page;
	}

	public void setLast_page(int last_page) {
		this.last_page = last_page;
	}

	public int getPer_page() {
		return per_page;
	}

	public void setPer_page(int per_page) {
		this.per_page = per_page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrent_page() {
		return current_page;
	}

	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
}

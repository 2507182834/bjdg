package com.dagu.utils;

import java.util.List;

public class PageUtils {

    private int currentPage=1;//当前页
    private int totalPages=0;//总页数
    private boolean hasNextPage=false;//没有下一页
    private boolean hasPreviousPage=false;//没有上一页
    private int pageStartRow=0;//每页的起始行
    private int pageEndRow=0;//每页显示的终止数

    private int pageStartPage = 1;//页面显示开始页数
    private int pageEndPage = 1;//页面显示终止页数

    private int showPages=21;//分页要显示的页数
    private int pageRecorders=10;//每页记录

    private int totalRows=0;//总行数
    private List<?> list;//传过一个list，就可以对list进行分页

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public int getPageStartRow() {
        return pageStartRow;
    }

    public void setPageStartRow(int pageStartRow) {
        this.pageStartRow = pageStartRow;
    }

    public int getPageEndRow() {
        return pageEndRow;
    }

    public void setPageEndRow(int pageEndRow) {
        this.pageEndRow = pageEndRow;
    }

    public int getPageStartPage() {
        return pageStartPage;
    }

    public void setPageStartPage(int pageStartPage) {
        this.pageStartPage = pageStartPage;
    }

    public int getPageEndPage() {
        return pageEndPage;
    }

    public void setPageEndPage(int pageEndPage) {
        this.pageEndPage = pageEndPage;
    }

    public int getShowPages() {
        return showPages;
    }

    public void setShowPages(int showPages) {
        this.showPages = showPages;
    }

    public void setPageRecorders(int pageRecorders){
        this.pageRecorders = pageRecorders;
    }

    public int getPageRecorders() {
        return pageRecorders;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageUtils{" +
                "currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", hasNextPage=" + hasNextPage +
                ", hasPreviousPage=" + hasPreviousPage +
                ", pageStartRow=" + pageStartRow +
                ", pageEndRow=" + pageEndRow +
                ", pageStartPage=" + pageStartPage +
                ", pageEndPage=" + pageEndPage +
                ", showPages=" + showPages +
                ", pageRecorders=" + pageRecorders +
                ", totalRows=" + totalRows +
                ", list=" + list +
                '}';
    }
}

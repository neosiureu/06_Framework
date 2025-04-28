package edu.kh.project.board.model.dto;


/*
  
 페이지네이션이란?
 
 목록을 일정 수만큼 분할하여 원하는 페이지를 볼 수 있게 하는 것
 == 페이징 처리
 
 페이지네이션 DTO 객체 => 페이징 처리에 필요한 값을 모아두고 계산하는 객체
 
 */


// 롬복을 못씀
public class Pagination {
	
	private int currentPage;		// 현재 페이지 번호 (위에서)
	private int listCount;			// 전체 게시글 수 (아래에서)
		
	private int limit = 10;			// 한 페이지 목록에 보여지는 게시글 수
	private int pageSize = 10;		// 하나의 페이지네이션에서 보여질 페이지 번호 개수
		
	
	
	
	private int maxPage;			// 마지막 페이지 번호 (2000개면 200페이지)
	
	
	
	private int startPage;			// 보여지는 맨 앞 페이지 번호 1 11 21 31...
	private int endPage;			// 보여지는 맨 뒤 페이지 번호 10 20 30 40...
		
	
	
	private int prevPage;			// 이전 페이지 모음의 마지막 번호 
	private int nextPage;			// 다음 페이지 모음의 시작 번호
	
	
	public Pagination(int currentPage, int listCount) {
		this.currentPage = currentPage;
		this.listCount = listCount;
		
		calculate();
	}
	
	
	public Pagination(int currentPage, int listCount, int limit, int pageSize) {
		this.currentPage = currentPage;
		this.listCount = listCount;
		this.limit = limit;
		this.pageSize = pageSize;
		calculate();
	}


	public int getCurrentPage() {
		return currentPage;
	}


	public int getListCount() {
		return listCount;
	}


	public int getLimit() {
		return limit;
	}


	public int getPageSize() {
		return pageSize;
	}


	public int getMaxPage() {
		return maxPage;
	}


	public int getStartPage() {
		return startPage;
	}


	public int getEndPage() {
		return endPage;
	}


	public int getPrevPage() {
		return prevPage;
	}


	public int getNextPage() {
		return nextPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		calculate();
	}


	public void setListCount(int listCount) {
		this.listCount = listCount;
		calculate();
	}


	public void setLimit(int limit) {
		this.limit = limit;
		calculate();
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		calculate();
	}


	@Override
	public String toString() {
		return "Pagination [currentPage=" + currentPage + ", listCount=" + listCount + ", limit=" + limit
				+ ", pageSize=" + pageSize + ", maxPage=" + maxPage + ", startPage=" + startPage + ", endPage="
				+ endPage + ", prevPage=" + prevPage + ", nextPage=" + nextPage + "]";
	}
	
	
	
	
	/*
	 페이징처리에 필요한 값을 계산하여 필드에 대입하는 메서드
	 (maxPage, startPage, endPage, prevPage, nextPage)
	 */
	
	private void calculate() {
		
		// maxPage: 최대 페이지 == 마지막 페이지 
		
		// 한 페이지에 게시글이 10, limit이 10인 경우
		// 게시글 수가 95개면 => 10페이지
		// 게시글 수가 100개면 => 10페이지
		// 게시글 수가 101개면 => 11페이지

		maxPage = (int)  Math.ceil  (   (double)listCount / limit   ); 
		
		
		// startPage: 페이지 번호 목록의 시작번호
		// 페이지네이션 상 페이지 번호 목록이 10개(pageSize) 씩 보여질 경우
		
		// 현재 페이지가 22페이지면 21이 startPage 30이 endPage
		// 현재 페이지가 131페이지면 131이 startPage 140이 endPage

		startPage = (currentPage-1) / pageSize * pageSize +1;
		
		
		// endPage: 페이지번호 목록의 끝 번호
		// 페이지네이션 상 페이지 번호 목록이 10개(pageSize) 씩 보여질 경우
		
		
		endPage  = pageSize-1+ startPage;
		// 페이지 끝 번호가 최대 페이지수를 초과한 경우
		
		if(endPage > maxPage) endPage = maxPage;
		// 48페이지가 마지막이면 마지막 페이지가 50이 아닌 48로 갈아끼워짐
		
	
		
		// prev (<),next(>)

		
		// 14에서 <를 누르면 10으로, 8에서 >를 누르면 11로
		
		
		// 더 이상 이전으로 갈 페이지가 없을 경우
		
		if(currentPage <=pageSize) {
			prevPage=1;
		}
		
		else {
			prevPage = startPage-1; // 11페이지에서 1을 뺀 10페이지로 가게 함
		}
		
		
		// <클릭 시 이동할 페이지 번호
		
		
		// 더이상 넘겨도 넘기는게 아닐 때
		if(endPage == maxPage) {
			nextPage = maxPage;
		}
		
		else {
			nextPage = endPage+1;
		}
		
		
		
	/*	 	
	private int currentPage;		// 현재 페이지 번호 (위에서)
	private int listCount;			// 전체 게시글 수 (아래에서)
		
	private int limit = 10;			// 한 페이지 목록에 보여지는 게시글 수
	private int pageSize = 10;		// 하나의 페이지네이션에서 보여질 페이지 번호 개수
	
	private int maxPage;			// 마지막 페이지 번호 (1995개면 200페이지)
	
	private int startPage;			// 보여지는 맨 앞 페이지 번호 1 11 21 31...
	private int endPage;			// 보여지는 맨 뒤 페이지 번호 10 20 30 40...
		
	
	private int prevPage;			// 이전 페이지 모음의 마지막 번호 
	private int nextPage;			// 다음 페이지 모음의 시작 번호
	*/
	
		
	}
	
	
	
	
	
	
	
	

}
